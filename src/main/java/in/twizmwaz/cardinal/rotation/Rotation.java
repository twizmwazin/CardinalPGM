package in.twizmwaz.cardinal.rotation;

import com.google.common.collect.Maps;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.rotation.exception.RotationLoadException;
import in.twizmwaz.cardinal.util.Contributor;
import in.twizmwaz.cardinal.util.DomUtils;
import in.twizmwaz.cardinal.util.MojangUtils;
import in.twizmwaz.cardinal.util.NumUtils;
import org.apache.commons.io.Charsets;
import org.bukkit.Bukkit;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;

public class Rotation {

    private File rotationFile;
    private List<LoadedMap> rotation;
    private List<LoadedMap> loaded;
    private int position;
    private File repo;

    public Rotation() throws RotationLoadException {
        this.rotationFile = new File(Cardinal.getInstance().getConfig().getString("rotation"));
        refreshRepo();
        refreshRotation();
    }

    /**
     * Refreshes the maps in the repository and the data associated with them
     *
     * @throws RotationLoadException
     */
    public void refreshRepo() throws RotationLoadException {
        loaded = new ArrayList<>();
        this.repo = new File(Cardinal.getInstance().getConfig().getString("repo"));
        List<String> requirements = Arrays.asList("map.xml", "region", "level.dat");
        for (File map : repo.listFiles()) {
            if (map.isFile()) continue;
            if (Arrays.asList(map.list()).containsAll(requirements)) {
                try {
                    Document xml = DomUtils.parse(new File(map.getPath() + "/map.xml"));
                    String name = xml.getRootElement().getChild("name").getText();
                    String version = xml.getRootElement().getChild("version").getText();
                    String objective = xml.getRootElement().getChild("objective").getText();
                    List<Contributor> authors = new ArrayList<>();
                    for (Element authorsElement : xml.getRootElement().getChildren("authors")) {
                        for (Element author : authorsElement.getChildren()) {
                            authors.add(parseContributor(author));
                        }
                    }
                    List<Contributor> contributors = new ArrayList<>();
                    for (Element contributorsElement : xml.getRootElement().getChildren("contributors")) {
                        for (Element contributor : contributorsElement.getChildren()) {
                            contributors.add(parseContributor(contributor));
                        }
                    }
                    List<String> rules = new ArrayList<>();
                    for (Element rulesElement : xml.getRootElement().getChildren("rules")) {
                        for (Element rule : rulesElement.getChildren()) {
                            rules.add(rule.getText().trim());
                        }
                    }
                    int maxPlayers = 0;
                    for (Element teams : xml.getRootElement().getChildren("teams")) {
                        for (Element team : teams.getChildren()) {
                            maxPlayers = maxPlayers + NumUtils.parseInt(team.getAttributeValue("max"));
                        }
                    }
                    loaded.add(new LoadedMap(name, version, objective, authors, contributors, rules, maxPlayers, map));
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.WARNING, "Failed to load map at " + map.getAbsolutePath());
                    if (Cardinal.getInstance().getConfig().getBoolean("displayMapLoadErrors")) {
                    	Bukkit.getLogger().log(Level.INFO, "Showing error, this can be disabled in the config: ");
                    	e.printStackTrace();
                    }
                }
            }
        }
        try {
            updatePlayers();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the plugin's default rotation
     *
     * @throws RotationLoadException
     */
    public void refreshRotation() throws RotationLoadException {
        rotation = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(rotationFile.toPath(), Charsets.UTF_8);
            for (String line : lines) {
                for (LoadedMap map : loaded) {
                    if (map.getName().replaceAll(" ", "").equalsIgnoreCase(line.replaceAll(" ", ""))) {
                        rotation.add(map);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            List<String> lines = new ArrayList<>();
            for (int x = 0; x < 8; x++) {
                try {
                    rotation.add(loaded.get(x));
                } catch (IndexOutOfBoundsException ex) {
                }
            }
            Bukkit.getLogger().log(Level.WARNING, "Failed to load rotation file, using a temporary rotation instead.");
        }
        position = 0;
    }

    /**
     * Move the position in the rotation by one. If the end of the rotation is reached, it will be automatically reset.
     */
    public int move() {
        position++;
        if (position > rotation.size() - 1) {
            position = 0;
        }
        return position;
    }

    /**
     * @return Returns the next map in the rotation
     */
    public LoadedMap getNext() {
        return rotation.get(position);
    }

    /**
     * @return Returns the rotation
     */
    public List<LoadedMap> getRotation() {
        return rotation;
    }

    /**
     * @return Returns all loaded maps
     */
    public List<LoadedMap> getLoaded() {
        return loaded;
    }

    /**
     * @return Gets the rotation index of the next map
     */
    public int getNextIndex() {
        return position;
    }

    /**
     * @param string The name of map to be searched for
     * @return The map
     */
    public LoadedMap getMap(String string) {
        for (LoadedMap map : loaded) {
            if (map.getName().toLowerCase().startsWith(string.toLowerCase())) return map;
        }
        return null;
    }

    public LoadedMap getCurrent() {
        try {
            return rotation.get(position - 1);
        } catch (IndexOutOfBoundsException e) {
            return rotation.get(0);
        }
    }
    
    private Contributor parseContributor(Element element) {
        if (element.getAttributeValue("uuid") != null) {
            return new Contributor(UUID.fromString(element.getAttributeValue("uuid")), element.getAttributeValue("contribution"));
        } else return new Contributor(element.getText(), element.getAttributeValue("contribution"));
    }
    
    @SuppressWarnings("unchecked")
    private void updatePlayers() throws IOException, ClassNotFoundException {
        HashMap<UUID, String> names;
        try {
             names = (HashMap<UUID, String>) new ObjectInputStream(new FileInputStream(new File(Cardinal.getInstance().getDataFolder().getPath() + ".names.ser"))).readObject();
        } catch (FileNotFoundException e) {
            names = Maps.newHashMap();
        }
        for (LoadedMap map : loaded) {
            for (Contributor contributor : map.getAuthors()) {
                if (contributor.getName() == null) {
                    String localName = Bukkit.getOfflinePlayer(contributor.getUniqueId()).getName();
                    if (localName != null) {
                        contributor.setName(localName);
                    } else if (names.containsKey(contributor.getUniqueId())) {
                        contributor.setName(names.get(contributor.getUniqueId()));
                    } else {
                        names.put(contributor.getUniqueId(), MojangUtils.getNameByUUID(contributor.getUniqueId()));
                        contributor.setName(names.get(contributor.getUniqueId()));
                    }
                }
            }
            for (Contributor contributor : map.getContributors()) {
                if (contributor.getName() == null) {
                    String localName = Bukkit.getOfflinePlayer(contributor.getUniqueId()).getName();
                    if (localName != null) {
                        contributor.setName(localName);
                    } else if (names.containsKey(contributor.getUniqueId())) {
                        contributor.setName(names.get(contributor.getUniqueId()));
                    } else {
                        names.put(contributor.getUniqueId(), MojangUtils.getNameByUUID(contributor.getUniqueId()));
                        contributor.setName(names.get(contributor.getUniqueId()));
                    }
                }
            }
        }
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(Cardinal.getInstance().getDataFolder().getPath() + "/.names.ser")));
        out.writeObject(names);
        out.close();
    }

}
