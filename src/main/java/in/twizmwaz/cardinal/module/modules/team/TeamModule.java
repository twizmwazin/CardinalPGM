package in.twizmwaz.cardinal.module.modules.team;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

import java.util.HashSet;
import java.util.Set;

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

    public boolean add(Player player, boolean force, String message) {
        TeamModule old = null;
        for (TeamModule team : match.getModules().getModules(TeamModule.class)) {
            if (team.contains(player)) {
                old = team;
                break;
            }
        }
        if (Blitz.matchIsBlitz() && GameHandler.getGameHandler().getMatch().isRunning() && !this.isObserver() && !force) {
            player.sendMessage(ChatColor.RED + "You may not join during a " + ChatColor.AQUA + "" + ChatColor.ITALIC + GameHandler.getGameHandler().getMatch().getModules().getModule(Blitz.class).getTitle() + ChatColor.RED + " match.");
            return false;
        }
        PlayerChangeTeamEvent event = new PlayerChangeTeamEvent(player, force, this, old);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (message != null) {
            if (!message.equals("")) {
                event.getPlayer().sendMessage(message);
            }
        }
        return !event.isCancelled() || force;
    }

    public boolean add(Player player, boolean force) {
        return add(player, force, ChatColor.GRAY + "You joined " + this.getCompleteName());
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onTeamSwitch(PlayerChangeTeamEvent event) {
        if (!event.isCancelled()) {
            this.remove(event.getPlayer());
        }
        if (event.getNewTeam() == this) {
            this.add(event.getPlayer());
        }
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

    public Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (TeamUtils.getTeamByPlayer(player) == this) {
                players.add(player);
            }
        }
        return players;
    }

}
