package in.twizmwaz.cardinal.rotation;

import in.twizmwaz.cardinal.util.Contributor;
import org.bukkit.util.CachedServerIcon;

import java.io.File;
import java.util.List;

public class LoadedMap {

    private final String name, version, objective;
    private final List<Contributor> authors, contributors;
    private final List<String> rules;
    private final int maxPlayers;
    private final File folder;
    private final CachedServerIcon icon;

    /**
     * @param name    The name of the map
     * @param authors The authors of the map
     * @param folder  The folder where the map can be found
     */
    public LoadedMap(String name, String version, String objective, List<Contributor> authors, List<Contributor> contributors, List<String> rules, int maxPlayers, File folder, CachedServerIcon icon) {
        this.name = name;
        this.version = version;
        this.objective = objective;
        this.authors = authors;
        this.contributors = contributors;
        this.rules = rules;
        this.maxPlayers = maxPlayers;
        this.folder = folder;
        this.icon = icon;
    }

    /**
     * @return Returns the name of the map
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the map version as a String
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return Returns the objective of the map
     */
    public String getObjective() {
        return objective;
    }

    /**
     * @return Returns the authors of the map with their contributions
     */
    public List<Contributor> getAuthors() {
        return authors;
    }

    /**
     * @return Returns the contributors of the map with their contributions
     */
    public List<Contributor> getContributors() {
        return contributors;
    }

    /**
     * @return Returns the custom map rules
     */
    public List<String> getRules() {
        return rules;
    }

    /**
     * @return Returns the maximum number of players for the map
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @return Returns the folder where the map can be found
     */
    public File getFolder() {
        return folder;
    }

    /**
     * @return Returns server icon for the map.
     */
    public CachedServerIcon getIcon() {
        return icon;
    }
}
