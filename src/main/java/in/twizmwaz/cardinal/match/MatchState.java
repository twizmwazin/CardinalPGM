package in.twizmwaz.cardinal.match;

public enum MatchState {

    WAITING(),
    STARTING(),
    PLAYING(),
    ENDED(),
    CYCLING(),
    CLOSING();

    public String toString() {
        switch (this) {
            case WAITING:
                return "Waiting";
            case STARTING:
                return "Starting";
            case PLAYING:
                return "Playing";
            case ENDED:
                return "Ended";
            case CYCLING:
                return "Cycling";
            case CLOSING:
                return "Shutting down";
            default:
                return "";
        }
    }
}
