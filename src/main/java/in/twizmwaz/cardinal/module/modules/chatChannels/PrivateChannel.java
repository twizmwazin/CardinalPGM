package in.twizmwaz.cardinal.module.modules.chatChannels;

import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Collection;
import java.util.HashSet;

public abstract class PrivateChannel implements ChatChannel {

    protected final Permission permission;
    protected final PermissionModule permissionModule;

    public PrivateChannel(Permission permission, PermissionModule permissionModule) {
        this.permission = permission;
        this.permissionModule = permissionModule;
    }

    @Override
    public Collection<? extends Player> getMembers() {
        Collection<Player> results = new HashSet<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission)) results.add(player);
        }
        return results;
    }

    @Override
    public void addMember(Player player) {
        permissionModule.getPlayerAttachment(player).setPermission(permission, true);
    }

    @Override
    public void removeMember(Player player) {
        permissionModule.getPlayerAttachment(player).setPermission(permission, false);
    }

    @Override
    public void resetMembers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeMember(player);
        }
    }
}
