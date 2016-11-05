package in.twizmwaz.cardinal;

import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Config;
import in.twizmwaz.cardinal.util.GitUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpdateHandler {

    private static UpdateHandler handler;

    public static final String localGitRevision;

    static {
        handler = new UpdateHandler();
        String version = GameHandler.getGameHandler().getPlugin().getDescription().getVersion();
        localGitRevision = version.substring(version.length() - 7, version.length());
    }

    private boolean update;

    public UpdateHandler() {
        handler = this;
        Bukkit.getScheduler().runTaskTimerAsynchronously(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (Config.autoUpdate && System.currentTimeMillis() - lastUpdate > TimeUnit.HOURS.toMillis(24)) {
                    Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), UpdateHandler.getUpdateHandler().getUpdateTask(Bukkit.getConsoleSender()));
                }
            }
        }, 600L, 3600*20L); // Runs after 30s, every hour
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

    public static long lastUpdate = 0L;

    public UpdateTask getUpdateTask(CommandSender sender) {
        lastUpdate = System.currentTimeMillis() - 10000L;
        return new UpdateTask(sender);
    }

    private static final AtomicBoolean lock = new AtomicBoolean(false);
    private static String downloadedRevision = "";

    public class UpdateTask implements Runnable {

        private final CommandSender sender;

        public UpdateTask(CommandSender sender) {
            this.sender = sender;
        }

        @Override
        public void run() {
            sender.sendMessage(new LocalizedChatMessage(ChatConstant.GENERIC_CHECKING_UPDATES).getMessage(ChatUtil.getLocale(sender)));
            String last = GitUtil.getLatestGitRevision();
            if (last != null && last.startsWith(UpdateHandler.localGitRevision)) {
                sender.sendMessage(ChatColor.GOLD + ChatConstant.UI_LATEST_VERSION.asMessage().getMessage(ChatUtil.getLocale(sender)));
            } else if (downloadedRevision.equals(last)) {
                sender.sendMessage(new LocalizedChatMessage(ChatConstant.GENERIC_ALREADY_DOWNLOADED).getMessage(ChatUtil.getLocale(sender)));
            } else {
                sender.sendMessage(new LocalizedChatMessage(ChatConstant.GENERIC_DOWNLOAD_STARTING).getMessage(ChatUtil.getLocale(sender)));
                try {
                    while (!lock.compareAndSet(false, true)) {
                        try {
                            Thread.sleep(1L);
                        } catch (InterruptedException e) {
                        }
                    }
                    try {
                        File to = new File(Bukkit.getServer().getUpdateFolderFile(), Cardinal.getInstance().getPluginFileName());
                        File tmp = new File(to.getPath() + ".update");
                        if (!tmp.exists()) {
                            Bukkit.getServer().getUpdateFolderFile().mkdirs();
                            tmp.createNewFile();
                        }
                        URL url = new URL("http://ci.twizmwaz.in/job/Cardinal/lastSuccessfulBuild/artifact/target/CardinalPGM-1.0-SNAPSHOT.jar");
                        InputStream is = url.openStream();
                        OutputStream os = new FileOutputStream(tmp);
                        byte[] buffer = new byte[4096];
                        int fetched;
                        while ((fetched = is.read(buffer)) != -1)
                            os.write(buffer, 0, fetched);
                        is.close();
                        os.flush();
                        os.close();
                        if (to.exists()) to.delete();
                        tmp.renameTo(to);
                        sender.sendMessage(new LocalizedChatMessage(ChatConstant.GENERIC_DOWNLOAD_READY, ChatColor.GREEN + "Cardinal" + ChatColor.WHITE).getMessage(ChatUtil.getLocale(sender)));
                        downloadedRevision = last;
                    } catch (Exception e) {
                        sender.sendMessage(new LocalizedChatMessage(ChatConstant.GENERIC_DOWNLOAD_FAILED, ChatColor.GREEN + "Cardinal" + ChatColor.WHITE).getMessage(ChatUtil.getLocale(sender)));
                    }
                    lock.set(false);
                } catch (Throwable t) {
                }
            }
        }
    }

}