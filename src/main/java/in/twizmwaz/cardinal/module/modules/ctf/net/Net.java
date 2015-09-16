package in.twizmwaz.cardinal.module.modules.ctf.net;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.ctf.Flag;
import in.twizmwaz.cardinal.module.modules.ctf.event.PlayerCaptureFlagEvent;
import in.twizmwaz.cardinal.module.modules.ctf.event.PlayerEnterNetEvent;
import in.twizmwaz.cardinal.module.modules.ctf.event.PlayerLeaveNetEvent;
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

import java.util.List;
import java.util.Set;

public class Net implements Module {

    private String id;
    private RegionModule region;            // required
    private TeamModule owner;
    private int points;                     // Default: 0
    private Post post;
    private Set<Flag> flags;                // Default: ALL FLAGS
    private Set<Flag> rescue;
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
               Set<Flag> flags,
               Set<Flag> rescue,
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
        this.flags = flags;
        this.rescue = rescue;
        this.sticky = sticky;
        this.captureFilter = captureFilter;
        this.denyMessage = denyMessage;
        this.respawnTogether = respawnTogether;
        this.respawnFilter = respawnFilter;
        this.respawnMessage = respawnMessage;
    }

    public List<String> debug() {
        List<String> debug = Lists.newArrayList();
        debug.add("---------- DEBUG Flag Net ----------");
        debug.add("String id = " + id);
        debug.add("Region region = " + region.getName());
        debug.add("TeamModule owner = " + (owner == null ? "None" : owner.getName()));
        debug.add("int points = " + points);
        debug.add("Post post = " + (post == null ? "None" : post.getId()));
        debug.add("Set<Flag> flags = " + flags);
        debug.add("Set<Flag> rescue = " + rescue);
        debug.add("boolean sticky = " + sticky);
        debug.add("FilterModule captureFilter = " + (captureFilter == null ? "None" : captureFilter.getName()));
        debug.add("String denyMessage = " + denyMessage);
        debug.add("boolean respawnTogether = " + respawnTogether);
        debug.add("FilterModule respawnFilter = " + (respawnFilter == null ? "None" : respawnFilter.getName()));
        debug.add("Respawn Message = " + respawnMessage);
        debug.add("------------------------------------");
        return debug;
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
        if (region.contains(event.getTo()) && !region.contains(event.getFrom())) {
            PlayerEnterNetEvent e = new PlayerEnterNetEvent(event.getPlayer(), this);
            Bukkit.getServer().getPluginManager().callEvent(e);
        } else if (!region.contains(event.getTo()) && region.contains(event.getFrom())) {
            PlayerLeaveNetEvent e = new PlayerLeaveNetEvent(event.getPlayer(), this);
            Bukkit.getServer().getPluginManager().callEvent(e);
        }
    }

    @EventHandler
    public void onEnterNet(PlayerEnterNetEvent event) {
        if (event.getNet().equals(this)) {
            TeamModule own = owner == null ? Teams.getTeamByPlayer(event.getPlayer()).get() : owner;
            Flag flag = Flags.getFlag(this);
            if (flag != null && flag.getPicker() != null && flag.getPicker().equals(event.getPlayer())) {
                if (captureFilter == null || captureFilter.evaluate(event.getPlayer()).equals(FilterState.ALLOW)) {
                    PlayerCaptureFlagEvent e = new PlayerCaptureFlagEvent(event.getPlayer(), Flags.getFlag(this), this);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                }
            }
        }
    }
}
