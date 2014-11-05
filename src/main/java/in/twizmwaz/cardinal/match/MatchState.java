package in.twizmwaz.cardinal.match;

/**
 * Created by kevin on 10/29/14.
 */
public enum MatchState {

    WAITING(),
    STARTING(),
    PLAYING(),
    ENDED(),
    CYCLING();

    public String toString() {
        switch(this) {
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
            default:
                return "";
        }
    }



}
