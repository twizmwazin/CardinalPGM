package in.twizmwaz.cardinal.util;

import com.google.common.collect.Lists;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.List;

public class Config {

    public static List<String> repos = Lists.newArrayList("maps");
    public static String rotation = "rotation.txt";
    public static boolean deleteMatches = true;
    public static boolean debug_displayMapLoadErrors = false;
    public static boolean debug_displayModuleLoadMessages = false;
    public static boolean worldEditPermissions = true;
    public static String serverMessage = "Scrimmage Server";
    public static boolean customMotd = true;
    public static String motdMessage = "";
    public static int startDefault = 20;
    public static int cycleDefault = 20;
    public static int cycleAuto = -1;
    public static boolean matchTimeMillis = true;
    public static boolean observersReady = true;
    public static boolean resetSpawnProtection = true;
    public static boolean autoUpdate = false;
    // -- Team register --
    //
    // team-url, player-regex and filters are for team registering. This is a very flexible system, that allows a url and a
    // player regex to be found, to verify a player is in a team or not.
    //
    // There are various arguments you can use, in the url or the player regex, at the moment you can use:
    // {team} This is the team name, the one you use in the team register command, can be used in both the link and regex.
    // {uuid} This is the player's uuid, with "-", they can be removed via filters, may only be used inside player regex.
    //
    // -- Filters --
    //
    // Filters are used for string manipulation.
    // For example, while the team name may be "Team Name!" you may want it in the url as "teamname", to do so, you can
    // use a list of filters to be applied, filters consist of a regex to find, and a string to replace it with,
    // and the addition of a 'lower-case' and a 'upper-case' filter.
    // the above change would be acomplished with:
    //
    // filters:
    //   team:
    //     - case: 'lowercase'
    //     - match: "[^a-z0-9]"
    //       replace: ""
    //
    // If you want to use uuid's without "-" in them, you can use a uuid filter like this:
    //
    // filters:
    //   uuid:
    //     - match: "-"
    //       replace: ""
    public static String teamUrl = "";
    public static String playerRegex = "";
    public static boolean registeredSeeObservers = false;


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
