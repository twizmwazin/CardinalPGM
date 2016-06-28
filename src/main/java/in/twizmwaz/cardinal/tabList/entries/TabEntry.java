package in.twizmwaz.cardinal.tabList.entries;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import in.twizmwaz.cardinal.tabList.TabList;
import in.twizmwaz.cardinal.util.PacketUtils;
import in.twizmwaz.cardinal.util.Strings;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.EnumGamemode;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class TabEntry {

    public static final String defaultName = "                        ";
    public static final Property DEFAULT_PROPERTY = new Property("textures", "eyJ0aW1lc3RhbXAiOjE0MTEyNjg3OTI3NjUsInByb2ZpbGVJZCI6IjNmYmVjN2RkMGE1ZjQwYmY5ZDExODg1YTU0NTA3MTEyIiwicHJvZmlsZU5hbWUiOiJsYXN0X3VzZXJuYW1lIiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg0N2I1Mjc5OTg0NjUxNTRhZDZjMjM4YTFlM2MyZGQzZTMyOTY1MzUyZTNhNjRmMzZlMTZhOTQwNWFiOCJ9fX0=", "u8sG8tlbmiekrfAdQjy4nXIcCfNdnUZzXSx9BE1X5K27NiUvE1dDNIeBBSPdZzQG1kHGijuokuHPdNi/KXHZkQM7OJ4aCu5JiUoOY28uz3wZhW4D+KG3dH4ei5ww2KwvjcqVL7LFKfr/ONU5Hvi7MIIty1eKpoGDYpWj3WjnbN4ye5Zo88I2ZEkP1wBw2eDDN4P3YEDYTumQndcbXFPuRRTntoGdZq3N5EBKfDZxlw4L3pgkcSLU5rWkd5UH4ZUOHAP/VaJ04mpFLsFXzzdU4xNZ5fthCwxwVBNLtHRWO26k/qcVBzvEXtKGFJmxfLGCzXScET/OjUBak/JEkkRG2m+kpmBMgFRNtjyZgQ1w08U6HHnLTiAiio3JswPlW5v56pGWRHQT5XWSkfnrXDalxtSmPnB5LmacpIImKgL8V9wLnWvBzI7SHjlyQbbgd+kUOkLlu7+717ySDEJwsFJekfuR6N/rpcYgNZYrxDwe4w57uDPlwNL6cJPfNUHV7WEbIU1pMgxsxaXe8WSvV87qLsR7H06xocl2C0JFfe2jZR4Zh3k9xzEnfCeFKBgGb4lrOWBu1eDWYgtKV67M2Y+B3W5pjuAjwAxn0waODtEn/3jKPbc/sxbPvljUCw65X+ok0UUN1eOwXV5l2EGzn05t3Yhwq19/GxARg63ISGE8CKw=");

    protected GameProfile profile;

    private Packet deletePacket;

    protected TabEntry(GameProfile profile) {
        this.profile = profile;
    }

    protected void load() {
        this.hide();
        broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
    }

    public void destroy() {
        deletePacket = getTabListPacket(null, PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
    }

    public GameProfile getProfile() {
        return profile;
    }

    public String getName() {
        return getProfile().getName();
    }

    public String getDisplayName(Player viewer) {
        return defaultName;
    }

    public int getPing() {
        return 1000;
    }

    protected void hide() {
        PacketUtils.broadcastPacket(TabList.getTeamPacket(profile.getName(), 80, 3));
    }

    public boolean delete(Player viewer) {
        if (deletePacket != null) {
            PacketUtils.sendPacket(viewer, deletePacket);
            return true;
        }
        return false;
    }

    public void setSlot(Player viewer, int i) {
        setSlot(viewer, i, 3);
    }

    public void setSlot(Player viewer, int i, int action) {
        PacketUtils.sendPacket(viewer, TabList.getTeamPacket(getName(), i, action));
    }

    public void broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction action) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketUtils.sendPacket(player, getTabListPacket(player, action));
        }
    }

    protected Packet getTabListPacket(Player viewer, PacketPlayOutPlayerInfo.EnumPlayerInfoAction action) {
        PacketPlayOutPlayerInfo listPacket = new PacketPlayOutPlayerInfo(action);
        listPacket.add(getPlayerInfo(viewer, listPacket));
        return listPacket;
    }

    public PacketPlayOutPlayerInfo.PlayerInfoData getPlayerInfo(Player viewer, PacketPlayOutPlayerInfo listPacket) {
        int ping = getPing();
        return listPacket.new PlayerInfoData(getProfile(), ping < 0 ? 1000 : ping, EnumGamemode.SURVIVAL,
                new ChatComponentText(viewer != null ? getDisplayName(viewer) : ""));
    }

    public static GameProfile getProfile(Property texture) {
        GameProfile game = new GameProfile(UUID.randomUUID(), Strings.trimTo(UUID.randomUUID().toString(), 0, 8));
        game.getProperties().put("textures", texture);
        return game;
    }

}
