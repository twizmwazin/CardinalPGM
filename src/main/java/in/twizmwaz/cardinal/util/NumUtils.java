package in.twizmwaz.cardinal.util;

/**
 * Created by kevin on 10/26/14.
 */
public class NumUtils {

    public static boolean checkInterval(double test, double bound1, double bound2) {
        return (test >= bound1 && test <= bound2) || (test >= bound2 && test <= bound1);
    }

    public static double hypotsphere(double x, double y, double z) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

}
