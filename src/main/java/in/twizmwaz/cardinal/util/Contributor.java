package in.twizmwaz.cardinal.util;

import java.util.UUID;

public class Contributor {
    
    private String name;
    private String contribution;
    private UUID uniqueId;
    
    public Contributor(String name, String contribution) {
        this.name = name;
        this.contribution = contribution;
    }
    
    public Contributor(String name) {
        this(name, null);
    }
    
    public Contributor(UUID uniqueId, String contribution) {
        this.uniqueId = uniqueId;
        this.contribution = contribution;
    }
    
    public Contributor(UUID uniqueId) {
        this(uniqueId, null);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (this.name != null) throw new UnsupportedOperationException("Contributor name is already set");
        this.name = name;
    }

    public String getContribution() {
        return contribution;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }
}
