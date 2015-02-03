package in.twizmwaz.cardinal.permissions;

import in.twizmwaz.cardinal.GameHandler;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Settings {

    private static List<Setting> settings = new ArrayList<>();
    private static HashMap<UUID, PermissionAttachment> playerPermissions = new HashMap<>();

    public static List<Setting> getSettings() {
        return settings;
    }

    public static Setting getSettingByName(String name) {
        for (Setting setting : settings) {
            for (String settingName : setting.getNames()) if (settingName.equalsIgnoreCase(name)) return setting;
        }
        for (Setting setting : settings) {
            for (String settingName : setting.getNames()) if (settingName.toLowerCase().startsWith(name.toLowerCase())) return setting;
        }
        return null;
    }

    public static PermissionAttachment getAttatchmentByPlayer(Player player) {
        if (!playerPermissions.containsKey(player.getUniqueId())) playerPermissions.put(player.getUniqueId(), player.addAttachment(GameHandler.getGameHandler().getPlugin()));
        return playerPermissions.get(player.getUniqueId());
    }

}
