package in.twizmwaz.cardinal.module.modules.chat;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.chatChannels.TeamChannel;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatModule implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        //get default channel
        for (TeamChannel channel : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamChannel.class)) {
            if (channel.getTeam().equals(TeamUtils.getTeamByPlayer(event.getPlayer()))) {
                channel.sendMessage(channel.getTeam().getColor() + "[Team] " + event.getPlayer().getDisplayName() + ChatColor.RESET + ": " + event.getMessage());
                ConsoleCommandSender console = GameHandler.getGameHandler().getPlugin().getServer().getConsoleSender();
                console.sendMessage(channel.getTeam().getColor() + "[" + channel.getTeam().getName() + " Team] " + event.getPlayer().getDisplayName() + ChatColor.RESET + ": " + event.getMessage());
            }
        }
    }
}
