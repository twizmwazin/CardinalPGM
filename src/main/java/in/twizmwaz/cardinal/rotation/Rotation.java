package in.twizmwaz.cardinal.rotation;

import in.twizmwaz.cardinal.rotation.exception.RotationLoadException;
import in.twizmwaz.cardinal.util.DomUtils;
import net.minecraft.util.org.apache.commons.codec.Charsets;
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
                    List<String> authors = new ArrayList<>();
                    for (Element authorsElement : xml.getRootElement().getChildren("authors")) {
                        for (Element author : authorsElement.getChildren()) {
                            authors.add(author.getText());
                        }
                    }
                    loaded.add(new LoadedMap(name, authors, map));
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
            for (int i = 0; i < lines.size(); i++) {
                for (LoadedMap map : loaded) {
                    if (map.getName().replaceAll(" ", "").equalsIgnoreCase(lines.get(i).replaceAll(" ", ""))) {
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
    public void move() {
        position++;
        if (position > rotation.size() - 1) {
            position = 0;
        }
        return;
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

    public int getNextIndex() {
        if (position + 1 >= rotation.size()) {
            return position + 1;
        } else {
            return 0;
        }
    }
}
