package in.twizmwaz.cardinal.match;

/**
 * Created by kevin on 12/5/14.
 */
public enum JoinType {

    VOLUNTARY,
    FORCED,
    JOIN;

    @Override
    public String toString() {
        switch (this) {
            case VOLUNTARY: return "voluntary";
            case FORCED: return "forced";
            case JOIN: return "join";
            default: return null;
        }
    }
}
