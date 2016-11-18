package in.twizmwaz.cardinal.module.modules.ctf.net;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.event.flag.FlagCaptureEvent;
import in.twizmwaz.cardinal.event.flag.NetEnterEvent;
import in.twizmwaz.cardinal.event.flag.NetLeaveEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Fireworks;
import in.twizmwaz.cardinal.util.Flags;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.util.Set;

public class Net implements Module {

    private String id;
    private RegionModule region;            // required
    private TeamModule owner;
    private int points;                     // Default: 0
    private Post post;
    private Set<FlagObjective> flagObjectives; // Default: ALL FLAGS
    private Set<FlagObjective> rescue;
    private boolean sticky;                 // Default: true
    private FilterModule captureFilter;
    private String denyMessage;
    private boolean respawnTogether;        // Default: false
    private FilterModule respawnFilter;
    private String respawnMessage;
    private Vector location;

    public Net(String id,
               RegionModule region,
               TeamModule owner,
               int points,
               Post post,
               Set<FlagObjective> flagObjectives,
               Set<FlagObjective> rescue,
               boolean sticky,
               FilterModule captureFilter,
               String denyMessage,
               boolean respawnTogether,
               FilterModule respawnFilter,
               String respawnMessage,
               Vector location) {
        this.id = id;
        this.region = region;
        this.owner = owner;
        this.points = points;
        this.post = post;
        this.flagObjectives = flagObjectives;
        this.rescue = rescue;
        this.sticky = sticky;
        this.captureFilter = captureFilter;
        this.denyMessage = denyMessage;
        this.respawnTogether = respawnTogether;
        this.respawnFilter = respawnFilter;
        this.respawnMessage = respawnMessage;
        this.location = location;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public String getId() {
        return id;
    }

    public FilterModule getCaptureFilter() {
        return captureFilter;
    }

    public Set<FlagObjective> getFlags() {
        return flagObjectives;
    }

    public void setFlag(FlagObjective flag) {
        flagObjectives.add(flag);
    }

    public Post getPost() {
        return post;
    }

    public RegionModule getRegion() {
        return region;
    }

    public Vector getLocation() {
        return location;
    }

    private boolean rescueCarried() {
        int i = 0;
        for (FlagObjective flag : rescue) {
            if (flag.isCarried()) i++;
        }
        return i > 1;
    }

    private boolean allFlagsWaiting() {
        int i = 0;
        for (FlagObjective flag : flagObjectives) {
            if (!flag.isWaitingToRespawn()) i++;
        }
        return i == 0;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        playerMove(event.getPlayer(), event.getFrom().position(), event.getTo().position());
    }

    @EventHandler
    public void onPlayerMove(PlayerTeleportEvent event) {
        playerMove(event.getPlayer(), event.getFrom().position(), event.getTo().position());
    }

    private void playerMove(Player player, Vector from, Vector to) {
        FlagObjective flag = Flags.getFlag(player);
        if (flag == null) return;
        if (region.contains(to) && !region.contains(from)) {
            NetEnterEvent e = new NetEnterEvent(player, this, flag);
            Bukkit.getServer().getPluginManager().callEvent(e);
        } else if (!region.contains(to) && region.contains(from)) {
            NetLeaveEvent e = new NetLeaveEvent(player, this, flag);
            Bukkit.getServer().getPluginManager().callEvent(e);
        }
    }

    @EventHandler
    public void onEnterNet(NetEnterEvent event) {
        if (event.getNet().equals(this)) {
            tryCapture(event.getFlag());
            if (event.getFlag().isCarried() && sticky) event.getFlag().setLastNet(this);
        }
    }
    
    public void tryCapture(FlagObjective flag) {
        Player player = flag.getPicker();
        if (flagObjectives.contains(flag) && (owner == null || owner.equals(Teams.getTeamByPlayer(player).get()))) {
            FilterModule captureFilt = null;
            if (getCaptureFilter() != null || flag.getCaptureFilter() != null) {
                captureFilt = getCaptureFilter() != null ? getCaptureFilter() : flag.getCaptureFilter();
            }
            if (captureFilt == null || captureFilt.evaluate(player).equals(FilterState.ALLOW)) {
                if (rescueCarried()) {
                    player.sendMessage(denyMessage);
                    return;
                }
                flag.setRespawnFilter(this.respawnFilter);
                flag.setRespawnMessage(this.respawnMessage);
                FlagCaptureEvent e = new FlagCaptureEvent(player, flag, this);
                Bukkit.getServer().getPluginManager().callEvent(e);
            } else if (denyMessage != null) {
                player.sendMessage(denyMessage);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCaptureFlag(FlagCaptureEvent event) {
        if (event.getNet().equals(this) && respawnTogether) {
            event.getFlag().setCanRespawn(false);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCaptureFlag2(FlagCaptureEvent event) {
        if (event.getNet().equals(this) && respawnTogether) {
            if (allFlagsWaiting()) {
                for (FlagObjective flag : flagObjectives) flag.setCanRespawn(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCaptureFlag3(FlagCaptureEvent event) {
        Player player = event.getPlayer();
        FlagObjective flag = event.getFlag();
        if (event.getNet().equals(this)) {
            Fireworks.spawnFireworks(region.getCenterBlock().getAlignedVector(), (region.getMax().minus(region.getMin()).length()) * 0.55 + 1, 6, MiscUtil.convertChatColorToColor(flag.getChatColor()), 1);
            for (Player message : Bukkit.getOnlinePlayers()) {
                message.sendMessage(new LocalizedChatMessage(ChatConstant.UI_FLAG_CAPTURED, flag.getDisplayName() + ChatColor.RESET, Teams.getTeamByPlayer(player).get().getColor() + player.getName()).getMessage(player.getLocale()));
            }
            int pointsToAdd = points > 0 ? points : flag.getPoints();
            if (pointsToAdd > 0) {
                TeamModule team = owner == null ? Teams.getTeamByPlayer(player).get() : owner;
                for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                    if (score.getTeam().equals(team)) {
                        score.setScore(score.getScore() + pointsToAdd);
                        Bukkit.getServer().getPluginManager().callEvent(new ScoreUpdateEvent(score));
                    }
                }
            }
        }
    }
}
