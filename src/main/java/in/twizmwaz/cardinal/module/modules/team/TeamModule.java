package in.twizmwaz.cardinal.module.modules.team;

import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

import java.util.HashSet;

public class TeamModule<P extends Player> extends HashSet<Player> implements Module {

    private final Match match;
    private String name;
    private final String id;
    private int max;
    private int maxOverfill;
    private int respawnLimit;
    private ChatColor color;
    private final boolean observer;

    protected TeamModule(Match match, String name, String id, int max, int maxOverfill, int respawnLimit, ChatColor color, boolean observer) {
        this.match = match;
        this.name = name;
        this.id = id;
        this.max = max;
        this.maxOverfill = maxOverfill;
        this.respawnLimit = respawnLimit;
        this.color = color;
        this.observer = observer;
    }

    public boolean add(Player player, boolean force) {
        TeamModule old = null;
        for (TeamModule team : match.getModules().getModules(TeamModule.class)) {
            if (team.contains(player)) {
                old = team;
                break;
            }
        }
        PlayerChangeTeamEvent event = new PlayerChangeTeamEvent(player, force, this, old);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            this.add(player);
            return true;
        } else return false;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeamSwitch(PlayerChangeTeamEvent event) {
        if (!event.isCancelled()) remove(event.getPlayer());
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public String getCompleteName() {
        return this.color + this.name;
    }

    public Match getMatch() {
        return match;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMaxOverfill() {
        return maxOverfill;
    }

    public void setMaxOverfill(int maxOverfill) {
        this.maxOverfill = maxOverfill;
    }

    public int getRespawnLimit() {
        return respawnLimit;
    }

    public void setRespawnLimit(int respawnLimit) {
        this.respawnLimit = respawnLimit;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public boolean isObserver() {
        return observer;
    }
}
