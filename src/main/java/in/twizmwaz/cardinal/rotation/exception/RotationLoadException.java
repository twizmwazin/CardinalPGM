package in.twizmwaz.cardinal.rotation.exception;

/**
 * Created by kevin on 11/16/14.
 */
public class RotationLoadException extends Exception {

    private final String message;

    public RotationLoadException(String message) {
        this.message = message;

    }

    public String getMessage() {
        return message;
    }


}
