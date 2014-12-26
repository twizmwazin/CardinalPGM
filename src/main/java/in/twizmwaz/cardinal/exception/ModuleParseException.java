package in.twizmwaz.cardinal.exception;

/**
 * Created by kevin on 12/6/14.
 */
public class ModuleParseException extends Exception {

    private final String message;

    public ModuleParseException(String message) {
        this.message = message;
    }

    public ModuleParseException() {
        this("");
    }

    @Override
    public String getMessage() {
        return message;
    }
}
