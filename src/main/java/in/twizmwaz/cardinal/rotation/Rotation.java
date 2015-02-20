package in.twizmwaz.cardinal.rotation;

import in.twizmwaz.cardinal.rotation.exception.RotationLoadException;
import in.twizmwaz.cardinal.util.DomUtils;
import in.twizmwaz.cardinal.util.NumUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Rotation {

    private JavaPlugin plugin;
    private File rotationFile;
    private List<LoadedMap> rotation;
    private List<LoadedMap> loaded;
    private int position;
    private File repo;

    public Rotation(JavaPlugin plugin) throws RotationLoadException {
        this.plugin = plugin;
        this.rotationFile = new File(plugin.getConfig().getString("rotation"));
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
        this.repo = new File(plugin.getConfig().getString("repo"));
        List<String> requirements = Arrays.asList("map.xml", "region", "level.dat");
        for (File map : repo.listFiles()) {
            if (map.isFile()) continue;
            if (Arrays.asList(map.list()).containsAll(requirements)) {
                try {
                    Document xml = DomUtils.parse(new File(map.getPath() + "/map.xml"));
                    String name = xml.getRootElement().getChild("name").getText();
                    String version = xml.getRootElement().getChild("version").getText();
                    String objective = xml.getRootElement().getChild("objective").getText();
                    List<Pair<String, String>> authors = new ArrayList<>();
                    for (Element authorsElement : xml.getRootElement().getChildren("authors")) {
                        for (Element author : authorsElement.getChildren()) {
                            String authorName = author.getAttributeValue("uuid") == null ? author.getText() : Bukkit.getOfflinePlayer(UUID.fromString(author.getAttributeValue("uuid"))).getName();
                            authors.add(new ImmutablePair<>(authorName, author.getAttributeValue("contribution")));
                        }
                    }
                    List<Pair<String, String>> contributors = new ArrayList<>();
                    for (Element contributorsElement : xml.getRootElement().getChildren("contributors")) {
                        for (Element contributor : contributorsElement.getChildren()) {
                            contributors.add(new ImmutablePair<>(contributor.getText(), contributor.getAttributeValue("contribution")));
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
                }
            }
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
}
