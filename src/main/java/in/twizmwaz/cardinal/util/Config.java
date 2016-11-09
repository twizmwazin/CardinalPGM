package in.twizmwaz.cardinal.util;

import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.List;

public class Config {

    public static String repo = "maps";
    public static String rotation = "rotation.txt";
    public static boolean deleteMatches = true;
    public static boolean displayMapLoadErrors = false;
    public static boolean worldEditPermissions = true;
    public static String serverMessage = "Scrimmage Server";
    public static boolean customMotd = true;
    public static String motdMessage = "";
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
                        field.set(null, parser.function.apply(config, toKey(field.getName()), field.get(null)));
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
        STRING(String.class, (conf, key, o) -> conf.getString(key, (String) o)),
        INT(int.class, (conf, key, o) -> conf.getInt(key, (int) o)),
        BOOLEAN(boolean.class, (conf, key, o) -> conf.getBoolean(key, (boolean) o)),
        DOUBLE(double.class, (conf, key, o) -> conf.getDouble(key, (double) o)),
        LONG(long.class, (conf, key, o) -> conf.getLong(key, (long) o)),
        LIST(List.class, (conf, key, o) -> conf.getList(key, (List) o));

        private Class<?> clazz;
        private ConfigFunction<Object> function;

        Parser(Class<?> clazz, ConfigFunction<Object> function) {
            this.clazz = clazz;
            this.function = function;
        }

    }

    interface ConfigFunction<T>  {
        T apply(FileConfiguration conf, String key, T t);
    }

}
