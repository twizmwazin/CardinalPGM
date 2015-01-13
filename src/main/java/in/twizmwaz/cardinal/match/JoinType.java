package in.twizmwaz.cardinal.match;

public enum JoinType {

    VOLUNTARY,
    FORCED,
    JOIN;

    @Override
    public String toString() {
        switch (this) {
            case VOLUNTARY:
                return "voluntary";
            case FORCED:
                return "forced";
            case JOIN:
                return "join";
            default:
                return null;
        }
    }
}
