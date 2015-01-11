package in.twizmwaz.cardinal.module.modules.mapInfo;

import in.twizmwaz.cardinal.module.modules.mapInfo.contributor.Contributor;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.HandlerList;

import java.util.List;

public class Info implements Module {
    public static Info mapInfo;

    private String name;
    private String version;
    private String objective;
    private List<Contributor> authors;
    private List<Contributor> contributors;
    private List<String> rules;

    protected Info(String name, String version, String objective, List<Contributor> authors, List<Contributor> contributors, List<String> rules) {
        this.name = name;
        this.version = version;
        this.objective = objective;
        this.authors = authors;
        this.contributors = contributors;
        this.rules = rules;
        mapInfo = this;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getObjective() {
        return objective;
    }

    public List<Contributor> getAuthors() {
        return authors;
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public List<String> getRules() {
        return rules;
    }
}
