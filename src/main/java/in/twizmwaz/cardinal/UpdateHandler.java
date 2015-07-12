package in.twizmwaz.cardinal;

import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.GitUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class UpdateHandler {

    private static UpdateHandler handler;

    static {
        handler = new UpdateHandler();
    }

    private final String localGitRevision;
    private boolean update;

    public UpdateHandler() {
        handler = this;
        this.localGitRevision = GameHandler.getGameHandler().getPlugin().getDescription().getVersion().substring(GameHandler.getGameHandler().getPlugin().getDescription().getVersion().length() - 7, GameHandler.getGameHandler().getPlugin().getDescription().getVersion().length());
    }

    public static UpdateHandler getUpdateHandler() {
        return handler;
    }

    /**
     * @return Returns true/false if an there is a new update in the update.txt file on github
     */
    public boolean checkUpdates() {
        String revision = GitUtil.getLatestGitRevision();
        if (revision != null && !revision.startsWith(localGitRevision)) {
            update = true;
        }
        return update;
    }

    public NotificationTask getNotificationTask(CommandSender sender) {
        return new NotificationTask(sender);
    }

    public class NotificationTask implements Runnable {

        private final CommandSender sender;

        public NotificationTask(CommandSender sender) {
            this.sender = sender;
        }

        @Override
        public void run() {
            if (UpdateHandler.getUpdateHandler().checkUpdates())
                sender.sendMessage(ChatColor.GOLD + ChatConstant.UI_UPDATE_AVAILABLE.asMessage().getMessage(ChatUtil.getLocale(sender)));
            else
                sender.sendMessage(ChatColor.GOLD + ChatConstant.UI_LATEST_VERSION.asMessage().getMessage(ChatUtil.getLocale(sender)));
        }
    }


}