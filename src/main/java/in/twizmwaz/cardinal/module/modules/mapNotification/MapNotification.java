package in.twizmwaz.cardinal.module.modules.mapNotification;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;

public class MapNotification implements TaskedModule {

    private long startTime;
    private int nextMessage;

    protected MapNotification() {
        this.startTime = System.currentTimeMillis();
        this.nextMessage = 600;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
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
            ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "{0}",
                    new LocalizedChatMessage(ChatConstant.UI_MAP_PLAYING,
                            GameHandler.getGameHandler().getMatch().getLoadedMap().toChatMessage(false))));
            this.nextMessage += 600;
        }
    }
}
