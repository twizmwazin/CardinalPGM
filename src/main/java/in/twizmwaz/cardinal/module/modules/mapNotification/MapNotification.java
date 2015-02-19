package in.twizmwaz.cardinal.module.modules.mapNotification;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.chatChannels.GlobalChannel;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.util.List;
import java.util.Locale;

public class MapNotification implements TaskedModule {

    private long startTime;
    private int nextMessage;

    protected MapNotification() {
        this.nextMessage = 600;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * @return The current time stored in the module.
     */

    public double getTimeInSeconds() {
        return ((double) System.currentTimeMillis() - startTime) / 1000.0;
    }

    @Override
    public void run() {
        if (getTimeInSeconds() >= this.nextMessage) {
            LoadedMap map = GameHandler.getGameHandler().getMatch().getLoadedMap();
            for (Player player : Bukkit.getOnlinePlayers()) {
                String locale = player.getLocale();
                String result = "";
                List<Pair<String, String>> authors = map.getAuthors();
                for (Pair<String, String> author : authors) {
                    if (authors.indexOf(author) < authors.size() - 2) {
                        result = result + org.bukkit.ChatColor.RED + author.getLeft() + org.bukkit.ChatColor.DARK_PURPLE + ", ";
                    } else if (authors.indexOf(author) == authors.size() - 2) {
                        result = result + org.bukkit.ChatColor.RED + author.getLeft() + org.bukkit.ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_AND).getMessage(locale) + " ";
                    } else if (authors.indexOf(author) == authors.size() - 1) {
                        result = result + org.bukkit.ChatColor.RED + author.getLeft();
                    }
                }
                player.sendMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "{0}", new LocalizedChatMessage(ChatConstant.UI_MAP_PLAYING, ChatColor.GOLD + map.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_BY).getMessage(locale) + " " + result)).getMessage(locale));
            }
            String locale = Locale.getDefault().toString();
            String result = "";
            List<Pair<String, String>> authors = map.getAuthors();
            for (Pair<String, String> author : authors) {
                if (authors.indexOf(author) < authors.size() - 2) {
                    result = result + org.bukkit.ChatColor.RED + author.getLeft() + org.bukkit.ChatColor.DARK_PURPLE + ", ";
                } else if (authors.indexOf(author) == authors.size() - 2) {
                    result = result + org.bukkit.ChatColor.RED + author.getLeft() + org.bukkit.ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_AND).getMessage(locale) + " ";
                } else if (authors.indexOf(author) == authors.size() - 1) {
                    result = result + org.bukkit.ChatColor.RED + author.getLeft();
                }
            }
            Bukkit.getLogger().info(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "{0}", new LocalizedChatMessage(ChatConstant.UI_MAP_PLAYING, ChatColor.GOLD + map.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_BY).getMessage(locale) + " " + result)).getMessage(locale));
            this.nextMessage += 600;
        }
    }
}
