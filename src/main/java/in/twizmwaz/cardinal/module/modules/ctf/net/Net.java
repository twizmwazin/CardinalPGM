package in.twizmwaz.cardinal.module.modules.ctf.net;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.event.flag.FlagCaptureEvent;
import in.twizmwaz.cardinal.event.flag.NetEnterEvent;
import in.twizmwaz.cardinal.event.flag.NetLeaveEvent;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Flags;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;

public class Net implements Module {

    private String id;
    private RegionModule region;            // required
    private TeamModule owner;
    private int points;                     // Default: 0
    private Post post;
    private Set<FlagObjective> flagObjectives;                // Default: ALL FLAGS
    private Set<FlagObjective> rescue;
    private boolean sticky;                 // Default: true
    private FilterModule captureFilter;
    private String denyMessage;
    private boolean respawnTogether;        // Default: false
    private FilterModule respawnFilter;
    private String respawnMessage;

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
               String respawnMessage) {
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
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public FilterModule getCaptureFilter() {
        return captureFilter;
    }

    public int getPoints() {
        return points;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (Flags.hasFlag(event.getPlayer())) {
            FlagObjective flagObjective = Flags.getFlag(event.getPlayer());
            if (region.contains(event.getTo()) && !region.contains(event.getFrom())) {
                NetEnterEvent e = new NetEnterEvent(event.getPlayer(), this, flagObjective);
                Bukkit.getServer().getPluginManager().callEvent(e);
            } else if (!region.contains(event.getTo()) && region.contains(event.getFrom())) {
                NetLeaveEvent e = new NetLeaveEvent(event.getPlayer(), this, flagObjective);
                Bukkit.getServer().getPluginManager().callEvent(e);
            }
        }
    }

    @EventHandler
    public void onEnterNet(NetEnterEvent event) {
        if (event.getNet().equals(this)) {
            TeamModule own = owner == null ? Teams.getTeamByPlayer(event.getPlayer()).get() : owner;
            FlagObjective flagObjective = Flags.getFlag(this);
            if (flagObjective != null && flagObjective.getPicker() != null && flagObjective.getPicker().equals(event.getPlayer())) {
                if (captureFilter == null || captureFilter.evaluate(event.getPlayer()).equals(FilterState.ALLOW)) {
                    FlagCaptureEvent e = new FlagCaptureEvent(event.getPlayer(), Flags.getFlag(this), this);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                }
            }
        }
    }
}
