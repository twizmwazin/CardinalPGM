package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.Cardinal;

public class VersionUtil {

    public static String getJarVersion() {
        return Cardinal.class.getPackage().getImplementationVersion();
    }
}
