package in.twizmwaz.cardinal.util;

import java.text.DecimalFormat;

public class StringUtils {

    public static int timeStringToSeconds(String input) {
        int time = 0;
        String currentUnit = "";
        String current = "";
        boolean negative = false;
        for (int i = 0; i < input.length(); i ++) {
            char c = input.charAt(i);
            if (Character.isDigit(c) && !currentUnit.equals("")) {
                time += convert(Integer.parseInt(current) * (negative ? -1 : 1), currentUnit);
                current = "";
                currentUnit = "";
            }
            if (c == '-') {
                negative = true;
            } else if (Character.isDigit(c)) {
                current += Integer.parseInt(c + "");
            } else {
                currentUnit += c + "";
            }
        }
        time += convert(Integer.parseInt(current) * (negative ? -1 : 1), currentUnit);
        return time;
    }

    private static int convert(int value, String unit) {
        switch (unit) {
            case "y": return value * 365 * 60 * 60 * 24;
            case "mo": return value * 31 * 60 * 60 * 24;
            case "d": return value * 60 * 60 * 24;
            case "h": return value * 60 * 60;
            case "m": return value * 60;
            case "s": return value;
        }
        return value;
    }

    public static String formatTime(double time) {
        boolean negative = false;
        if (time < 0) {
            negative = true;
            time *= -1;
        }
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
        return (negative ? "-" : "") + (hours == 0 ? "" : hoursString + ":") + minutesString + ":" + secondsString;
    }

    public static String formatTimeWithMillis(double time) {
        boolean negative = false;
        if (time < 0) {
            negative = true;
            time *= -1;
        }
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
        return (negative ? "-" : "") + (hours == 0 ? "" : hoursString + ":") + minutesString + ":" + secondsString + "." + millisString;
    }
}
