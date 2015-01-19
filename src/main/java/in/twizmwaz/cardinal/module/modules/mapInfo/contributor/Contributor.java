package in.twizmwaz.cardinal.module.modules.mapInfo.contributor;

public class Contributor {

    private String name;
    private String contribution;

    public Contributor(String name, String contribution) {
        this.name = name;
        this.contribution = contribution;
    }

    public Contributor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getContribution() {
        try {
            return contribution;
        } catch (NullPointerException ex) {
            return null;
        }

    }

    public boolean hasContribution() {
        return (contribution != null);
    }
}
