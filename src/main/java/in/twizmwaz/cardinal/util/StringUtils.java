package in.twizmwaz.cardinal.util;

public class StringUtils {

    public static int timeStringToSeconds(String input) {
        int result = 0;
        String number = "";
        for (int i = 0; i < input.length(); i ++) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                number += c;
            } else if (Character.isLetter(c) && !number.isEmpty()) {
                result += convert(Integer.parseInt(number), c);
                number = "";
            }
        }
        return result;
    }

    private static long convert(int value, char unit) {
        switch (unit) {
            case 'd': return value * 60 * 60 * 24;
            case 'h': return value * 60 * 60;
            case 'm': return value * 60;
            case 's': return value;
        }
        return 0;
    }
}
