package in.twizmwaz.cardinal.exception;

/**
 * Created by kevin on 12/6/14.
 */
public class ModuleLoadException extends Exception {

    private final String message;

    public ModuleLoadException(String message) {
        this.message = message;
    }

    public ModuleLoadException() {
        this("");
    }

    @Override
    public String getMessage() {
        return message;
    }
}
