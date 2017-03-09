package in.twizmwaz.cardinal.tabList.entries;

import com.mojang.authlib.GameProfile;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.util.PacketUtils;
import in.twizmwaz.cardinal.util.Watchers;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutEntityDestroy;
import net.minecraft.server.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public abstract class SkinTabEntry extends TabEntry {

    private int id = Bukkit.allocateEntityId();
    private boolean hat = false;

    public SkinTabEntry(GameProfile profile) {
        super(profile);
    }

    @Override
    protected void load() {
        this.hide();
        broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        broadcastCreateSkinParts();
    }

    @Override
    public boolean delete(Player viewer) {
        if (super.delete(viewer)) {
            PacketUtils.sendPacket(viewer, deleteSkinParts());
            return true;
        }
        return false;
    }

    public void broadcastCreateSkinParts() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                PacketUtils.broadcastPacket(createSkinPartsPacket());
            }
        });
    }

    public void createSkinParts(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                PacketUtils.sendPacket(player, createSkinPartsPacket());
            }
        });
    }

    public void setHat(boolean hat) {
        if (hat == this.hat) {
            return;
        }
        this.hat = hat;
        updateSkinParts();
    }

    private List<DataWatcher.Item<?>> getDataList() {
        return Watchers.toList(hat ? Watchers.HAT_ON : Watchers.HAT_OFF);
    }

    private void updateSkinParts() {
        PacketUtils.broadcastPacket(PacketUtils.createMetadataPacket(id, getDataList()));
    }

    private Packet createSkinPartsPacket() {
        UUID uuid = getProfile().getId();
        return new PacketPlayOutNamedEntitySpawn(id, uuid, 0, -1000, 0, (byte) 0, (byte) 0, getDataList());
    }

    private Packet deleteSkinParts() {
        return new PacketPlayOutEntityDestroy(id);
    }

}
