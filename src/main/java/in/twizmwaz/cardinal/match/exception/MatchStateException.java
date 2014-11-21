package in.twizmwaz.cardinal.match.exception;

/**
 * Created by kevin on 11/19/14.
 */
public class MatchStateException extends Exception {

    private String message;

    public MatchStateException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
