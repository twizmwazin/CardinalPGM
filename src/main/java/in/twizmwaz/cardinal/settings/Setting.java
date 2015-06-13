package in.twizmwaz.cardinal.settings;

import in.twizmwaz.cardinal.Cardinal;
import org.bukkit.entity.Player;

import java.util.List;

public class Setting {

    private List<String> names;
    private String description;
    private List<SettingValue> values;

    public Setting(List<String> names, String description, List<SettingValue> values) {
        this.names = names;
        this.description = description;
        this.values = values;

        boolean hasDefault = false;
        for (SettingValue value : values) {
            if (value.isDefault()) hasDefault = true;
        }
        if (!hasDefault) values.get(0).setDefault(true);
        Settings.addSetting(this);
    }

    public List<String> getNames() {
        return names;
    }

    public List<SettingValue> getValues() {
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
        if (Cardinal.getCardinalDatabase().get(player, "setting_" + this.names.get(0)).equals("")) {
            SettingValue defaultValue = null;
            for (SettingValue value : this.values) {
                if (value.isDefault()) defaultValue = value;
            }
            if (defaultValue != null)
                Cardinal.getCardinalDatabase().put(player, "setting_" + this.names.get(0), defaultValue.getValue());
        }
        return this.getSettingValueByName(Cardinal.getCardinalDatabase().get(player, "setting_" + this.names.get(0)));
    }

    public void setValueByPlayer(Player player, SettingValue value) {
        Cardinal.getCardinalDatabase().put(player, "setting_" + this.names.get(0), value.getValue());
    }

    public String getDescription() {
        return description;
    }
}
