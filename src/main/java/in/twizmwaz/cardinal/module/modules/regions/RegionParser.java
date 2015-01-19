package in.twizmwaz.cardinal.module.modules.regions;

public abstract class RegionParser {
    
    protected final String name;

    protected RegionParser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
