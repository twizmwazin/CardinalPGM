package in.twizmwaz.cardinal.util;

import org.apache.commons.lang.math.NumberUtils;

public class TimeUtils {

    public static int timePeriodsToSeconds(String timePeriod) {
        int timeInSeconds;
        if (timePeriod.endsWith("s")) {
            String[] split = timePeriod.split("s");
            timeInSeconds = Integer.parseInt(split[0]);
            return timeInSeconds;
        } else if (timePeriod.endsWith("m")) {
            String[] split = timePeriod.split("m");
            timeInSeconds = Integer.parseInt(split[0]) * 60;
            return timeInSeconds;
        } else if (timePeriod.endsWith("h")) {
            String[] split = timePeriod.split("h");
            timeInSeconds = (Integer.parseInt(split[0]) * 60) * 60;
            return timeInSeconds;
        } else if (timePeriod.endsWith("d")) {
            String[] split = timePeriod.split("d");
            timeInSeconds = ((Integer.parseInt(split[0]) * 24) * 60) * 60;
            return timeInSeconds;
        } else if (timePeriod.endsWith("mo")) {
            String[] split = timePeriod.split("mo");
            timeInSeconds = ((Integer.parseInt(split[0]) * 60) * 60) * 60;
            return timeInSeconds;
        } else if (timePeriod.endsWith("y")) {
            String[] split = timePeriod.split("y");
            timeInSeconds = (((Integer.parseInt(split[0]) * 365) * 24) * 60) * 60;
            return timeInSeconds;
        } else if (NumberUtils.isNumber(timePeriod)) {
            timeInSeconds = Integer.parseInt(timePeriod);
        } else {
            return 0;
        }
        return timeInSeconds;
    }

}

