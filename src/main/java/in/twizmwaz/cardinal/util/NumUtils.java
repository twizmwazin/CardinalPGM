package in.twizmwaz.cardinal.util;

import java.util.Random;

/**
 * Created by kevin on 10/26/14.
 */
public class NumUtils {

    public static boolean checkInterval(double test, double bound1, double bound2) {
        return (test >= bound1 && test <= bound2) || (test >= bound2 && test <= bound1);
    }

    public static double hypotSphere(double x, double y, double z) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public static double randomInterval(double min, double max) {
        double high, low;
        if (min < max) {
            low = min;
            high = max;
        }
        if (max < min) {
            low = max;
            high = min;
        }
        Random random = new Random();
        return min + (random.nextGaussian() * (max - min));
    }

    public static double parseDouble(String string) {
        if (string.equalsIgnoreCase("oo")) return Double.POSITIVE_INFINITY;
        if (string.equalsIgnoreCase("-oo")) return Double.NEGATIVE_INFINITY;
        return Double.parseDouble(string);
    }

}
