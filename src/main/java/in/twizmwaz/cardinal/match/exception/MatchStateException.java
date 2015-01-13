package in.twizmwaz.cardinal.match.exception;

public class MatchStateException extends Exception {

    private String message;

    public MatchStateException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
