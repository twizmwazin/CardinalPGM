package in.twizmwaz.cardinal.module.modules.chatChannels;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.module.modules.matchTranscript.MatchTranscript;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Locale;

public class GlobalChannel implements ChatChannelModule {
    
    @Override
    public void sendMessage(String string) {
        Bukkit.broadcastMessage(string);
        if (Cardinal.getInstance().getConfig().getBoolean("enableHTML")) {
        	GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTranscript.class).log(ChatColor.stripColor(string));
        }
    }

    @Override
    public void sendLocalizedMessage(ChatMessage message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message.getMessage(player.getLocale()));
        }
        Bukkit.getServer().getConsoleSender().sendMessage(message.getMessage(message.getMessage(Locale.getDefault().toString())));
        if (Cardinal.getInstance().getConfig().getBoolean("enableHTML")) {
        	GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTranscript.class).log(ChatColor.stripColor(message.getMessage(Locale.getDefault().toString())));
        }
    }

    @Override
    public Collection<? extends Player> getMembers() {
        return Bukkit.getOnlinePlayers();
    }

    @Override
    public void addMember(Player player) {
    }

    @Override
    public void removeMember(Player player) {
    }

    @Override
    public void resetMembers() {
    }

    @Override
    public void unload() {
        resetMembers();
    }
}
