package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.settings.Setting;
import in.twizmwaz.cardinal.settings.SettingValue;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerSettingChangeEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Setting setting;
    private final SettingValue newValue;
    private final SettingValue oldValue;

    public PlayerSettingChangeEvent(Player player, Setting setting, SettingValue oldValue, SettingValue newValue) {
        super(player);

        this.setting = setting;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Setting getSetting() {
        return setting;
    }

    public SettingValue getOldValue() {
        return oldValue;
    }

    public SettingValue getNewValue() {
        return newValue;
    }

}