package in.twizmwaz.cardinal.data;

/**
 * Created by kevin on 11/17/14.
 */
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
}
