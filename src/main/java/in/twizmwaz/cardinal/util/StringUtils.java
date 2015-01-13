package in.twizmwaz.cardinal.util;

import java.text.ParseException;

public class StringUtils {

    public static int timeStringToSeconds(String input) throws ParseException {
        if (input.endsWith("h")) {
            String[] strip = input.split("h");
            return Integer.getInteger(strip[0]) * 60 * 60;

        }
        if (input.endsWith("m")) {
            String[] strip = input.split("m");
            return Integer.getInteger(strip[0]) * 60;

        }
        if (input.endsWith("s")) {
            String[] strip = input.split("s");
            return Integer.getInteger(strip[0]);

        } else throw new ParseException("Could not parse value " + input + " correctly.", 0);
    }
}
