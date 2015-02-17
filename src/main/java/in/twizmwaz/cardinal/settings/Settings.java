package in.twizmwaz.cardinal.settings;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    private static List<Setting> settings = new ArrayList<>();

    public static List<Setting> getSettings() {
        return settings;
    }

    public static Setting getSettingByName(String name) {
        for (Setting setting : settings) {
            if (setting.getNames().get(0).equalsIgnoreCase(name)) return setting;
        }
        for (Setting setting : settings) {
            if (setting.getNames().get(0).toLowerCase().startsWith(name.toLowerCase())) return setting;
        }
        for (Setting setting : settings) {
            for (String settingName : setting.getNames()) if (settingName.equalsIgnoreCase(name)) return setting;
        }
        for (Setting setting : settings) {
            for (String settingName : setting.getNames()) if (settingName.toLowerCase().startsWith(name.toLowerCase())) return setting;
        }
        return null;
    }

    public static void addSetting(Setting setting) {
        settings.add(setting);
    }

}
