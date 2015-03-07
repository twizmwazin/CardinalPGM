package in.twizmwaz.cardinal.settings;

public class SettingValue {

    private String value;
    private boolean isDefault;

    public SettingValue(String value, boolean isDefault) {
        this.value = value;
        this.isDefault = isDefault;
    }

    public String getValue() {
        return value;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
