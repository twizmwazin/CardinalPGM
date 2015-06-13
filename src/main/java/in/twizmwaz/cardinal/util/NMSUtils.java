package in.twizmwaz.cardinal.util;

import org.bukkit.Bukkit;

public class NMSUtils {

    public static Class<?> getNMS(String name) {
        try {
            return Class.forName("net.minecraft.server." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getCraft(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + (name.startsWith(".") ? name : "." + name));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().replace(".", "," + "" + "").split(",")[3];
    }

}
