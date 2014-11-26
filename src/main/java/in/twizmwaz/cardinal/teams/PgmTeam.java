package in.twizmwaz.cardinal.teams;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PlayerJoinTeamEvent;
import in.twizmwaz.cardinal.regions.point.PointRegion;
import in.twizmwaz.cardinal.teams.spawns.Spawn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by kevin on 11/17/14.
 */
public class PgmTeam {

    private Team scoreboardTeam;
    private String name;
    private String id;
    private int max;
    private int maxOverfill;
    private int respawnLimit;
    private ChatColor color;
    private boolean observer;
    private List<Spawn> spawns;

    PgmTeam(String name, String id, int max, int maxOverfill, int respawnLimit, ChatColor color, boolean obs, Scoreboard scoreboard, List<Spawn> spawns) {
        this.observer = obs;
        this.name = name;
        this.id = id;
        this.max = max;
        this.maxOverfill = maxOverfill;
        this.respawnLimit = respawnLimit;
        this.color = color;
        this.scoreboardTeam = scoreboard.registerNewTeam(id);
        scoreboardTeam.setDisplayName(color + name);
        scoreboardTeam.setPrefix(color + "");
        this.spawns = spawns;

    }

    public void add(Player player) {
        PlayerJoinTeamEvent event = new PlayerJoinTeamEvent(player, this);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            scoreboardTeam.addPlayer(player);
        }

    }

    public void remove(Player player) {
        scoreboardTeam.removePlayer(player);
    }

    public boolean hasPlayer(Player player) {
        return scoreboardTeam.hasPlayer(player);
    }

    public Set<OfflinePlayer> getPlayers() {
        return scoreboardTeam.getPlayers();
    }

    public int getMax() {
        return max;
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

    public ChatColor getColor() {
        return color;
    }

    public Location getSpawnPoint() {
        Random random = new Random();
        int index = random.nextInt(spawns.size());
        PointRegion point = spawns.get(index).getPoint();
        return point.toLocation(GameHandler.getGameHandler().getMatchWorld());
    }
}
