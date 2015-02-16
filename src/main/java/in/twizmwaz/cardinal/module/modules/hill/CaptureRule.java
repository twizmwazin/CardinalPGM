package in.twizmwaz.cardinal.module.modules.hill;

public enum CaptureRule {
    
    EXCLUSIVE(),
    MAJORITY(),
    LEAD();
    
    public static CaptureRule parseCaptureRule(String string) {
        switch (string) {
            case "exclusive": return EXCLUSIVE;
            case "majority": return MAJORITY;
            case "lead": return LEAD;
            default: return null;
        }
    }
    
    @Override
    public String toString() {
        switch (this) {
            case EXCLUSIVE: return "exclusive";
            case MAJORITY: return "majority";
            case LEAD: return "lead";
            default: return null;
        }
    }
}
