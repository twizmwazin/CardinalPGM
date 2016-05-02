package in.twizmwaz.cardinal.util.polls;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.bossBar.BossBars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class Poll {

    private int id;
    private CommandSender sender;
    private String command;
    private boolean any;
    private int time;
    private final int originalTime;
    private int taskId;
    private String bossBar;
    private Set<UUID> votedYes = new HashSet<>();
    private Set<UUID> votedNo = new HashSet<>();

    protected Poll(int id, CommandSender sender, String command, int time, boolean any) {
        this.id = id;
        this.sender = sender;
        this.command = command;
        this.any = any;
        this.time = time * 20;
        this.originalTime = this.time;
        this.bossBar = BossBars.addBroadcastedBossBar(new UnlocalizedChatMessage(""), BarColor.YELLOW, BarStyle.SOLID, true);
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                Poll.this.update();
            }
        }, 0L, 1L);
    }

    protected boolean vote(UUID uuid, boolean vote) {
        if ((vote && votedYes.contains(uuid)) || (!vote && votedNo.contains(uuid))) {
            return false;
        }
        if (vote) {
            if (votedNo.contains(uuid)) votedNo.remove(uuid);
            votedYes.add(uuid);
        } else {
            if (votedYes.contains(uuid)) votedYes.remove(uuid);
            votedNo.add(uuid);
        }
        return true;
    }

    protected boolean any() {
        return any;
    }

    protected void updateTitle () {
        int intTime = time % 20 == 0 ? (time / 20) : (time / 20) + 1;
        BossBars.setTitle(bossBar, new LocalizedChatMessage(ChatConstant.UI_POLL_BOSSBAR,
                new UnlocalizedChatMessage((Polls.multiple() ? ChatColor.RED + "" + id + ": " : "") + ChatColor.YELLOW),
                new UnlocalizedChatMessage(command() + ChatColor.YELLOW),
                intTime == 1 ? new LocalizedChatMessage(ChatConstant.UI_SECOND, ChatColor.DARK_RED + "1" + ChatColor.YELLOW) :
                        new LocalizedChatMessage(ChatConstant.UI_SECONDS, ChatColor.DARK_RED + "" + intTime + ChatColor.YELLOW),
                new UnlocalizedChatMessage(result())));
    }

    private void update() {
        if (time == 0) {
            end();
            return;
        }
        if (time % 20 == 0) updateTitle();
        BossBars.setProgress(bossBar, (double)time / originalTime);
        time--;
    }

    private String command() {
        return ChatColor.RED + "\"" + ChatColor.GOLD + command + ChatColor.RED + "\"";
    }

    private String result() {
        return ChatColor.YELLOW + "[" + ChatColor.DARK_GREEN + votedYes.size() + " " + ChatColor.DARK_RED + votedNo.size() + ChatColor.YELLOW + "]";
    }

    private void end() {
        boolean succeed = votedYes.size() > votedNo.size();
        ChatColor color = succeed ? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(color + "{0}", new LocalizedChatMessage(succeed ? ChatConstant.GENERIC_POLL_SUCCEEDED : ChatConstant.GENERIC_POLL_FAILED, command() + color, result())));
        stop(null);
        if (succeed) Bukkit.dispatchCommand(sender, command);
    }

    protected void stop(CommandSender sender) {
        if (sender != null) ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_POLL_VETOED, command() + ChatColor.RED, Players.getName(sender))));
        Bukkit.getScheduler().cancelTask(taskId);
        BossBars.removeBroadcastedBossBar(bossBar);
        Polls.removePoll(id);
        this.votedYes = null;
        this.votedNo = null;
        this.bossBar = null;
    }

}

