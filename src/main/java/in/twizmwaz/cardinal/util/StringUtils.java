package in.twizmwaz.cardinal.util;

import java.text.ParseException;

public class StringUtils {

    public static int timeStringToSeconds(String input) throws ParseException {
        if (input.endsWith("h")) {
            input = input.replaceAll("h", "");
            return Integer.parseInt(input) * 60 * 60;

        }
        if (input.endsWith("m")) {
            input = input.replaceAll("m", "");
            return Integer.parseInt(input) * 60;
        }
        if (input.endsWith("s")) {
            input = input.replaceAll("s", "");
            return Integer.parseInt(input);
        } else return Integer.parseInt(input);
    }
}
