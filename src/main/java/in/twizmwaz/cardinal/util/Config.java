package in.twizmwaz.cardinal.util;

import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiFunction;

public class Config {

    public static String repo = "maps";
    public static String rotation = "rotation.txt";
    public static boolean deleteMatches = true;
    public static boolean displayMapLoadErrors = false;
    public static boolean worldEditPermissions = true;
    public static String serverMessage = "Scrimmage Server";
    public static boolean customMotd = true;
    public static String motdMessage = null;
    public static int cycleAuto = -1;
    public static boolean matchTimeMillis = true;
    public static boolean observersReady = true;
    public static boolean resetSpawnProtection = true;
    public static boolean autoUpdate = false;

    public static void reload(FileConfiguration config) {
        try {
            for (Field field : Config.class.getFields()) {
                Class<?> t = field.getType();
                for (Parser parser : Parser.values()) {
                    if (t == parser.clazz) {
                        Object obj = parser.function.apply(config, toKey(field.getName()));
                        if (obj != null) field.set(null, obj);
                        break;
                    }
                }
                config.set(toKey(field.getName()), field.get(null));
            }
        } catch (IllegalAccessException e) {
        }
    }

    public static String toKey(String string) {
        return string.replace("_", ".");
    }

    private enum Parser {
        STRING(String.class, FileConfiguration::getString),
        INT(int.class, FileConfiguration::getInt),
        BOOLEAN(boolean.class, FileConfiguration::getBoolean),
        DOUBLE(double.class, FileConfiguration::getDouble),
        LONG(long.class, FileConfiguration::getLong),
        LIST(List.class, FileConfiguration::getList);

        private Class<?> clazz;
        private BiFunction<FileConfiguration, String, ?> function;

        Parser(Class<?> clazz, BiFunction<FileConfiguration, String, ?> function) {
            this.clazz = clazz;
            this.function = function;
        }

    }

}
