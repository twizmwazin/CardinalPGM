package in.twizmwaz.cardinal.permissions;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class Setting {

    private List<String> names;
    private Set<SettingValue> values;

    public Setting(List<String> names, Set<SettingValue> values) {
        this.names = names;
        this.values = values;

        boolean hasDefault = false;
        for (SettingValue value : values) {
            if (value.isDefault()) hasDefault = true;
        }
        if (!hasDefault) {
            for (SettingValue value : values) {
                value.setDefault(true);
                break;
            }
        }
    }

    public List<String> getNames() {
        return names;
    }

    public Set<SettingValue> getValues() {
        return values;
    }

    public SettingValue getSettingValueByName(String name) {
        for (SettingValue settingValue : values) {
            if (settingValue.getValue().equalsIgnoreCase(name)) return settingValue;
        }
        for (SettingValue settingValue : values) {
            if (settingValue.getValue().toLowerCase().startsWith(name.toLowerCase())) return settingValue;
        }
        return null;
    }

    public boolean containsValue(String value) {
        for (SettingValue settingValue : values) {
            if (settingValue.getValue().equalsIgnoreCase(value)) return true;
        }
        for (SettingValue settingValue : values) {
            if (settingValue.getValue().toLowerCase().startsWith(value.toLowerCase())) return true;
        }
        return false;
    }

    public SettingValue getValueByPlayer(Player player) {
        for (SettingValue settingValue : values) {
            if (player.hasPermission("setting." + names.get(0) + "." + settingValue.getValue())) return settingValue;
        }
        return null;
    }

}
