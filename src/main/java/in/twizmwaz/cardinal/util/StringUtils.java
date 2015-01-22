package in.twizmwaz.cardinal.util;

import java.text.DecimalFormat;

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
        return value;
    }

    public static String formatTime(double time) {
        int hours = (int) time / 3600;
        int minutes = (int) (time - (hours * 3600)) / 60;
        int seconds = (int) time - (hours * 3600) - (minutes * 60);
        String hoursString = hours + "";
        String minutesString = minutes + "";
        String secondsString = seconds + "";
        while (minutesString.length() < 2) {
            minutesString = "0" + minutesString;
        }
        while (secondsString.length() < 2) {
            secondsString = "0" + secondsString;
        }
        return (hours == 0 ? "" : hoursString + ":") + minutesString + ":" + secondsString;
    }

    public static String formatTimeWithMillis(double time) {
        int hours = (int) time / 3600;
        int minutes = (int) (time - (hours * 3600)) / 60;
        int seconds = (int) time - (hours * 3600) - (minutes * 60);
        double millis = time - (hours * 3600) - (minutes * 60) - seconds;
        String hoursString = hours + "";
        String minutesString = minutes + "";
        String secondsString = seconds + "";
        String millisString = new DecimalFormat(".000").format(millis);
        millisString = millisString.substring(1);
        while (minutesString.length() < 2) {
            minutesString = "0" + minutesString;
        }
        while (secondsString.length() < 2) {
            secondsString = "0" + secondsString;
        }
        return (hours == 0 ? "" : hoursString + ":") + minutesString + ":" + secondsString + "." + millisString;
    }
}
