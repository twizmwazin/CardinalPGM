package in.twizmwaz.cardinal.tabList.entries;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.EnumChatFormat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;

public class PlayerTabEntry extends SkinTabEntry {

    private Player player;

    public PlayerTabEntry(Player player) {
        super(getProfile(getPlayerSkin(player)));
        this.player = player;
        load();
    }

    private String getDisplayName() {
        // The method is broken, removes black color, https://hub.spigotmc.org/jira/browse/SPIGOT-2711
        //return player.getPlayerListName();
        return CraftChatMessage.fromComponent(((CraftPlayer) player).getHandle().listName, EnumChatFormat.WHITE);
    }

    @Override
    public String getDisplayName(Player viewer) {
        if (viewer == player) return getDisplayName().replace(viewer.getName(), ChatColor.BOLD + viewer.getName());
        return getDisplayName();
    }

    @Override
    public int getPing() {
        return ((CraftPlayer) player).getHandle().ping;
    }

    private static Property getPlayerSkin(Player player) {
        return getPlayerSkin(((CraftPlayer) player).getProfile());
    }

    private static Property getPlayerSkin(GameProfile profile) {
        for (Property property : profile.getProperties().get("textures")) {
            return new Property("textures", property.getValue(), property.getSignature());
        }
        return DEFAULT_PROPERTY;
    }


}
