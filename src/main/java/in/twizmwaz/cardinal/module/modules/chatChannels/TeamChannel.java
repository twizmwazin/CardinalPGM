package in.twizmwaz.cardinal.module.modules.chatChannels;

import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.permissions.Permission;

public class TeamChannel extends PrivateChannel {

    private final TeamModule team;

    public TeamChannel(TeamModule team, PermissionModule permissionModule) {
        super(new Permission("cardinal." + team.getId()), permissionModule);
        this.team = team;
    }

    @Override
    public void sendMessage(String string) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission)) player.sendMessage(string);
        }
    }

    @Override
    public void sendLocalizedMessage(ChatMessage message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission))
                player.sendMessage(team.getColor() + "[" + team.getName() + "] " + message.getMessage(player.getLocale()));
        }
    }

    @Override
    public void unload() {
        resetMembers();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (team.equals(event.getNewTeam())) addMember(event.getPlayer());
        else removeMember(event.getPlayer());
    }

    public TeamModule getTeam() {
        return team;
    }
}
