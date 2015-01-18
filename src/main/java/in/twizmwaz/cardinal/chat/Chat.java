package in.twizmwaz.cardinal.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Chat implements Listener{

    private static String channel = "team"; //default is team chat, can be the following [team, global, admin]

    public Chat(JavaPlugin plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        if(channel.equalsIgnoreCase("team")){
            TeamChat.sendTeamMessage(event.getMessage(), event.getPlayer());
            event.setCancelled(true);
        } else if(channel.equalsIgnoreCase("global")){
            GlobalChat.sendGlobalMessage(event.getMessage(), event.getPlayer());
            event.setCancelled(true);
        } else if(channel.equalsIgnoreCase("admin")){
            AdminChat.sendAdminessage(event.getMessage(), event.getPlayer());
            event.setCancelled(true);
        }
    }

    public static void setChannelGloabl(){
        channel = "global";
    }

    public static void setChannelTeam(){
        channel = "team";
    }

    public static void setChannelAdmin() {
        channel = "admin";
    }
}
