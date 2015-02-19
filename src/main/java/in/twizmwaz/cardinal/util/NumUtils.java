package in.twizmwaz.cardinal.util;

import java.util.Random;

public class NumUtils {

    public static boolean checkInterval(double test, double bound1, double bound2) {
        return (test >= bound1 && test <= bound2) || (test >= bound2 && test <= bound1);
    }

    public static double hypotSphere(double x, double y, double z) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public static double getRandom(double min, double max) {
        return new Random().nextInt((int) (max - min) + 1) + min;
    }

    public static double parseDouble(String string) {
        if (string.equalsIgnoreCase("oo")) return Double.POSITIVE_INFINITY;
        if (string.equalsIgnoreCase("-oo")) return Double.NEGATIVE_INFINITY;
        return Double.parseDouble(string);
    }

    public static boolean parseBoolean(String string) {
        if (string.equalsIgnoreCase("on")) return true;
        return !string.equalsIgnoreCase("off") && Boolean.parseBoolean(string);
    }

    public static String convertToSubscript(double number) {
        if (number == Double.POSITIVE_INFINITY) return "\u221E";
        if (number == Double.NEGATIVE_INFINITY) return "-\u221E";
        return (number + "").replaceAll("0", "\u2080").replaceAll("1", "\u2081").replaceAll("2", "\u2082").replaceAll("3", "\u2083").replaceAll("4", "\u2084").replaceAll("5", "\u2085").replaceAll("6", "\u2086").replaceAll("7", "\u2087").replaceAll("8", "\u2088").replaceAll("9", "\u2089");
    }
}
