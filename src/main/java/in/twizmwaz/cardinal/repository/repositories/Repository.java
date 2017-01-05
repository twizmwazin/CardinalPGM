package in.twizmwaz.cardinal.repository.repositories;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.repository.LoadedMap;
import in.twizmwaz.cardinal.repository.exception.RotationLoadException;
import in.twizmwaz.cardinal.util.CollectionUtils;
import in.twizmwaz.cardinal.util.Config;
import in.twizmwaz.cardinal.util.Contributor;
import in.twizmwaz.cardinal.util.DomUtil;
import in.twizmwaz.cardinal.util.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public abstract class Repository {

    private static final List<String> requirements = Arrays.asList("map.xml", "region", "level.dat");

    private List<LoadedMap> loaded = Lists.newArrayList();
    private Map<String, File> includes = new HashMap<>();
    private String path;
    private File root;
    private int id = -1;

    private static int maxId = 0;

    public int getId() {
        return id;
    }

    public String getSource(boolean show) {
        return show ? path : null;
    }

    protected String getPath() {
        return path;
    }

    public File getRoot() {
        return root;
    }

    public String toChatMessage(boolean op) {
        String source = getSource(op);
        return ChatColor.YELLOW + "#" + getId() + " " + ChatColor.GOLD +
                (source == null ? ChatColor.STRIKETHROUGH + "Hidden" : source);
    }

    public File getInclude(String name) {
        return includes.containsKey(name) ? includes.get(name) : null;
    }

    Repository(String path) throws RotationLoadException, IOException {
        this.path = path;
        this.root = new File(path);
    }

    public void refreshRepo() throws RotationLoadException, IOException {
        if (id == -1) id = maxId++;
        includes.clear();
        root = new File(path);
        if (!root.exists()) root.mkdirs();
        loadIncludes(root);
        loaded = CollectionUtils.updateAndRun(loaded, loadMapsIn(root), LoadedMap::update)
                .peek(LoadedMap::load).collect(Collectors.toList());
    }

    private void loadIncludes(File file) {
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                if (child.isFile()) {
                    if (child.getName().endsWith(".xml") && !child.getName().equals("map.xml")) {
                        includes.put(child.getName(), child);
                    }
                } else
                    loadIncludes(child);
            }
        }
    }

    private List<LoadedMap> loadMapsIn(File file) {
        List<LoadedMap> result = new LinkedList<>();
        if (file == null || file.listFiles() == null) {
            return result;
        }
        for (File map : file.listFiles()) {
            if (map.isFile() || map.list() == null) continue;
            if (Arrays.asList(map.list()).containsAll(requirements)) {
                try {
                    result.add(loadMap(map));
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.WARNING, "Failed to load map at " + map.getAbsolutePath());
                    if (Config.debug_displayMapLoadErrors) {
                        Bukkit.getLogger().log(Level.INFO, "Showing error, this can be disabled in the config: ");
                        e.printStackTrace();
                    }
                }
            } else {
                result.addAll(loadMapsIn(map));
            }
        }
        return result;
    }

    private LoadedMap loadMap(File map) throws Exception {
        Document xml = DomUtil.parseMap(this, new File(map.getPath() + "/map.xml"));
        String name = xml.getRootElement().getChild("name").getTextNormalize();
        String version = xml.getRootElement().getChild("version").getTextNormalize();
        String objective = xml.getRootElement().getChild("objective").getTextNormalize();
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
                rules.add(rule.getTextNormalize());
            }
        }
        int maxPlayers = 0;
        for (Element teams : xml.getRootElement().getChildren("teams")) {
            for (Element team : teams.getChildren()) {
                maxPlayers = maxPlayers + Numbers.parseInt(team.getAttributeValue("max"));
            }
        }
        return new LoadedMap(name, version, objective, authors, contributors, rules, maxPlayers, map);
    }

    /**
     * @return Returns all loaded maps
     */
    public List<LoadedMap> getLoaded() {
        return loaded;
    }

    private static Contributor parseContributor(Element element) {
        if (element.getAttributeValue("uuid") != null) {
            return new Contributor(UUID.fromString(element.getAttributeValue("uuid")), element.getAttributeValue("contribution"));
        } else return new Contributor(element.getText(), element.getAttributeValue("contribution"));
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " id:" + id + ", Path:" + path;
    }

    @Override
    public boolean equals(Object other) {
        return other.getClass().equals(this.getClass()) && ((Repository) other).path.equals(this.path);
    }

}
