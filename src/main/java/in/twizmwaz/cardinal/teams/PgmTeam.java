package in.twizmwaz.cardinal.teams;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.JoinType;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.regions.type.PointRegion;
import in.twizmwaz.cardinal.teams.spawns.Spawn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
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
    private Set<GameObjective> objectives;

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

    public void add(Player player, JoinType joinType) {
        PlayerChangeTeamEvent event = new PlayerChangeTeamEvent(player, this, joinType);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            this.set(event);
        }

    }

    public void force(Player player, JoinType joinType) {
        PlayerChangeTeamEvent event = new PlayerChangeTeamEvent(player, this, joinType);
        Bukkit.getServer().getPluginManager().callEvent(event);
        this.set(event);
    }

    private void set(PlayerChangeTeamEvent event) {
        scoreboardTeam.addPlayer(event.getPlayer());
        event.getPlayer().setScoreboard(scoreboardTeam.getScoreboard());
        if (!event.getJoinType().equals(JoinType.JOIN)) {
            event.getPlayer().sendMessage(ChatColor.GRAY + "You have joined " + event.getNewTeam().getColor() + event.getNewTeam().getName());
        }
    }

    public void remove(Player player) {
        scoreboardTeam.removePlayer(player);
    }

    public boolean hasPlayer(Player player) {
        return scoreboardTeam.hasPlayer(player);
    }

    public Set<Player> getPlayers() {
        Set<Player> result = new HashSet<>(maxOverfill);
        for (OfflinePlayer offlinePlayer : scoreboardTeam.getPlayers()) {
            if (offlinePlayer instanceof Player) result.add((Player) offlinePlayer);
        }
        return result;
    }

    public Set<String> getPlayerNames() {
        return scoreboardTeam.getEntries();
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

    @Deprecated
    public Location getSpawnPoint() {
        Random random = new Random();
        int index = random.nextInt(spawns.size());
        PointRegion point = spawns.get(index).getPoint();
        return point.toLocation(GameHandler.getGameHandler().getMatchWorld());
    }

    public Spawn getSpawn() {
        Random random = new Random();
        return spawns.get(random.nextInt(spawns.size()));
    }

    public boolean isObserver() {
        return observer;
    }

    public Set<GameObjective> getObjectives() {
        return objectives;
    }

    public void setObjectives(Set<GameObjective> objectives) {
        this.objectives = objectives;
    }

    public Team getScoreboardTeam() {
        return scoreboardTeam;
    }
}
