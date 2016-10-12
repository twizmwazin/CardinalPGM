package in.twizmwaz.cardinal.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.modules.header.HeaderModule;
import in.twizmwaz.cardinal.repository.exception.RotationLoadException;
import in.twizmwaz.cardinal.repository.repositories.DefaultRepository;
import in.twizmwaz.cardinal.repository.repositories.FileRepository;
import in.twizmwaz.cardinal.repository.repositories.GitRepository;
import in.twizmwaz.cardinal.repository.repositories.Repository;
import in.twizmwaz.cardinal.util.CollectionUtils;
import in.twizmwaz.cardinal.util.Config;
import in.twizmwaz.cardinal.util.Contributor;
import in.twizmwaz.cardinal.util.MojangUtil;
import in.twizmwaz.cardinal.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepositoryManager {

    private Rotation rotation = new Rotation();
    private List<Repository> repos = Lists.newArrayList();

    public void setupRotation() throws RotationLoadException {
        try {
            refreshRepos();
        } catch (IOException e) {
            throw new RotationLoadException("Could not access the repository. Make sure java has sufficient read and write privileges.");
        }
        refreshRotation();
    }

    /**
     * Refreshes the maps in the repository and the data associated with them
     *
     * @throws RotationLoadException
     */
    public void refreshRepos() throws RotationLoadException, IOException {
        List<Repository> newRepos = Lists.newArrayList(new DefaultRepository());
        for (String repo : Config.repos) {
            if (repo.startsWith("git:")) {
                newRepos.add(GitRepository.fromURL(repo.replaceFirst("git:", "")));
            } else {
                newRepos.add(new FileRepository(repo));
            }
        }
        repos = CollectionUtils.update(repos, newRepos).collect(Collectors.toList());
        // Reload maps inside the repos
        for (Repository repo : repos) repo.refreshRepo();
        updatePlayers();
        if (!getLoadedStream().findFirst().isPresent())
            throw new RotationLoadException("No maps were loaded. Are there any maps in the repositories?");
    }

    /**
     * Refreshes the plugin's default rotation
     */
    public void refreshRotation() throws RotationLoadException {
        try {
            rotation.refreshRotation(getLoadedStream());
        } catch (RotationLoadException | IOException e) {
            Bukkit.getLogger().log(Level.WARNING, e.getMessage());
            Bukkit.getLogger().log(Level.WARNING, "Failed to load rotation file, using a temporary rotation instead.");
            rotation.add(getLoadedStream().findFirst().get());
        }
    }

    public static RepositoryManager get() {
        return GameHandler.getGameHandler().getRepositoryManager();
    }

    /**
     * @return Returns the rotation
     */
    public Rotation getRotation() {
        return rotation;
    }

    public Iterable<LoadedMap> getLoadedIterable() {
        return () -> getLoadedStream().iterator();
    }

    public Stream<LoadedMap> getLoadedStream() {
        return repos.stream().flatMap(repository -> repository.getLoaded().stream());
    }

    public List<Repository> getRepos() {
        return repos;
    }

    public int getMapSize() {
        return repos.stream().mapToInt(repo -> repo.getLoaded().size()).sum();
    }

    public Repository getRepo(LoadedMap map) {
        return repos.stream().filter(repository -> repository.getLoaded().contains(map)).findFirst().orElse(null);
    }

    /**
     * @param string The name of map to be searched for
     * @return The map
     */
    public List<LoadedMap> getMap(String string) {
        return getLoadedStream().filter(map -> Strings.matchString(map.getName(), string)).collect(Collectors.toList());
    }

    /**
     * @param id The id of map to be searched for
     * @return The map
     */
    public LoadedMap getMap(int id) {
        return getLoadedStream().filter(map -> map.getId() == id).findFirst().orElse(null);
    }

    /*public class LoadedMapIterator implements Iterator<LoadedMap> {
        private final Queue<Repository> repoQueue = new LinkedList<>(repos);
        private Iterator<LoadedMap> currIter = null;
        private LoadedMap nextMap = null;

        @Override
        public boolean hasNext() {
            return this.nextMap != null || setNext();
        }

        @Override
        public LoadedMap next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            LoadedMap next = this.nextMap;
            this.nextMap = null;
            setNext();
            return next;
        }

        private boolean setNext() {
            while (true) {
                if (currIter == null) {
                    if (repoQueue.isEmpty()) return false;
                    if (!repoQueue.isEmpty()) currIter = repoQueue.poll().getLoaded().iterator();
                } else if (currIter.hasNext()) {
                    this.nextMap = currIter.next();
                    return true;
                } else if (!repoQueue.isEmpty()) {
                    currIter = repoQueue.poll().getLoaded().iterator();
                } else {
                    currIter = null;
                }
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }*/

    private void updatePlayers() {
        Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), new PlayerNameUpdater());
    }

    private class PlayerNameUpdater implements Runnable {

        @Override
        public void run() {
            try {
                Bukkit.getLogger().log(Level.INFO, "Setting known map author names.");
                Map<UUID, String> names;
                try {
                    names = (Map<UUID, String>) new ObjectInputStream(new FileInputStream(new File(Cardinal.getInstance().getDataFolder().getPath() + "/.names.ser"))).readObject();
                } catch (FileNotFoundException | ClassCastException e) {
                    names = Maps.newHashMap();
                }
                for (LoadedMap map : getLoadedIterable()) {
                    List<Contributor> contributors = new ArrayList<>();
                    contributors.addAll(map.getAuthors());
                    contributors.addAll(map.getContributors());
                    for (Contributor contributor : contributors) {
                        if (contributor.getName() == null && contributor.getUniqueId() != null) {
                            if (!names.containsKey(contributor.getUniqueId()))
                                names.put(contributor.getUniqueId(), "Unknown");
                            contributor.setName(names.get(contributor.getUniqueId()));
                        }
                    }
                }
                Bukkit.getLogger().log(Level.INFO, "Updating map author names.");
                List<UUID> requested = Lists.newArrayList();
                for (LoadedMap map : getLoadedIterable()) {
                    List<Contributor> contributors = new ArrayList<>();
                    contributors.addAll(map.getAuthors());
                    contributors.addAll(map.getContributors());
                    for (Contributor contributor : contributors) {
                        if (contributor.getUniqueId() == null) continue;
                        if (requested.contains(contributor.getUniqueId())) {
                            contributor.setName(names.get(contributor.getUniqueId()));
                        } else {
                            OfflinePlayer player = Bukkit.getOfflinePlayer(contributor.getUniqueId());
                            if (player != null && player.getLastPlayed() + 180000 > System.currentTimeMillis()) {
                                names.put(contributor.getUniqueId(), player.getName());
                            } else {
                                String name = MojangUtil.getName(contributor.getUniqueId());
                                if (name != null) {
                                    names.put(contributor.getUniqueId(), name);
                                    requested.add(contributor.getUniqueId());
                                }
                            }
                            contributor.setName(names.get(contributor.getUniqueId()));
                        }
                    }
                    Match match = GameHandler.getGameHandler().getMatch();
                    if (match != null && map.equals(match.getLoadedMap()))
                        match.getModules().getModule(HeaderModule.class).updateAll(HeaderModule.HeaderPart.HEADER);
                }
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(Cardinal.getInstance().getDataFolder().getPath() + "/.names.ser")));
                out.writeObject(names);
                out.close();
                Bukkit.getLogger().log(Level.INFO, "Finished updating map author names.");
            } catch (IOException | ClassNotFoundException | ConcurrentModificationException e) {
                e.printStackTrace();
            }
        }

    }

}
