package in.twizmwaz.cardinal.module.modules.teamRegister;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.TeamNameChangeEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Authenticator;
import in.twizmwaz.cardinal.util.Config;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TeamRegisterModule implements Module {

    private static final Map<UUID, RegisteredTeam> playersTeams = Maps.newHashMap();
    private static final List<RegisteredTeam> teams = Lists.newArrayList();

    private static StringFilters filters;

    public TeamRegisterModule() {
        filters = new StringFilters(Cardinal.getInstance().getConfig().getConfigurationSection("filters"));
    }

    @Override
    public void unload() {
        playersTeams.clear();
        teams.clear();
        HandlerList.unregisterAll(this);
    }

    public static Set<UUID> getRegisteredPlayers() {
        return playersTeams.keySet();
    }

    public static int getRegisteredSize() {
        return teams.size();
    }

    public static List<String> getRegisteredTeamNames() {
        List<String> names = Lists.newArrayList();
        for (RegisteredTeam team : teams) {
            names.add(team.getName());
        }
        return names;
    }

    public static boolean canSeeObs(Player player) {
        return Config.registeredSeeObservers || !playersTeams.containsKey(player.getUniqueId());
    }

    public static boolean isRegistered(String team) {
        return getRegisteredTeam(team) != null;
    }

    public static boolean isRegistered(TeamModule team) {
        return getRegisteredTeam(team) != null;
    }

    public static RegisteredTeam getRegisteredTeam(TeamModule team) {
        for (RegisteredTeam registeredTeam : teams) {
            if (registeredTeam.getTeam().equals(team)) {
                return registeredTeam;
            }
        }
        return null;
    }

    public static RegisteredTeam getRegisteredTeam(String name) {
        for (RegisteredTeam registeredTeam : teams) {
            if (registeredTeam.getName().equalsIgnoreCase(name)) {
                return registeredTeam;
            }
        }
        return null;
    }

    public static String filterTeam(String teamName) {
        return filters.getTeam(teamName);
    }

    public static String filterUuid(String uuid) {
        return filters.getUuid(uuid);
    }

    public static void addRegisteredTeam(String name, TeamModule team) {
        TeamModule observers = Teams.getTeamById("observers").get();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (team.contains(player)) observers.add(player, true, false);
        }
        RegisteredTeam registeredTeam = new RegisteredTeam(name, team);
        teams.add(registeredTeam);
        team.setName(name);
        Bukkit.getPluginManager().callEvent(new TeamNameChangeEvent(team));
        for (Player player : Bukkit.getOnlinePlayers()) {
            addRegisteredPlayer(player.getUniqueId(), registeredTeam);
        }
    }

    public static void unregisterTeam(String name) {
        RegisteredTeam team = getRegisteredTeam(name);
        if (team != null) {
            List<UUID> toRemove = new ArrayList<>();
            for (UUID uuid : playersTeams.keySet()) {
                if (playersTeams.get(uuid).equals(team)) toRemove.add(uuid);
            }
            for (UUID uuid : toRemove) {
                removeRegisteredPlayer(uuid);
            }
            teams.remove(team);
            team.getTeam().setName(team.getOldName());
            Bukkit.getPluginManager().callEvent(new TeamNameChangeEvent(team.getTeam()));
            Authenticator.removeTeamDocument(name);
        }
    }

    public static TeamState addRegisteredPlayer(UUID uuid, RegisteredTeam team) {
        if (Authenticator.authenticateTeam(uuid, team.getName())) {
            if (team.getTeam().size() >= team.getTeam().getMax()) {
                return TeamState.FULL;
            }
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if (player.isOnline()) {
                team.getTeam().add((Player) player, false, false);
            }
            playersTeams.put(uuid, team);
            return TeamState.ALLOW;
        } else {
            return TeamState.DENY;
        }
    }

    public static void removeRegisteredPlayer(UUID uuid) {
        playersTeams.remove(uuid);
        Player player = Bukkit.getPlayer(uuid);
        if (player.willBeOnline()) {
            Teams.getTeamById("observers").get().add(Bukkit.getPlayer(uuid));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPreLogin(PlayerLoginEvent e) {
        for (RegisteredTeam team : teams) {
            TeamState allow = addRegisteredPlayer(e.getPlayer().getUniqueId(), team);
            if (allow.equals(TeamState.DENY)) {
                continue;
            }
            if (allow.equals(TeamState.ALLOW)) {
                e.allow();
            } else if (allow.equals(TeamState.FULL)) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED +
                        new LocalizedChatMessage(ChatConstant.ERROR_CANT_JOIN_TEAM_FULL,
                                ChatColor.AQUA + "" + team.getTeam().size() + ChatColor.RED
                        ).getMessage(e.getPlayer().getLocale()));
            }
            break;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (playersTeams.containsKey(event.getPlayer().getUniqueId())) {
            playersTeams.get(event.getPlayer().getUniqueId()).getTeam().add(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        removeRegisteredPlayer(e.getPlayer().getUniqueId());
    }

    private enum TeamState {
        ALLOW(),
        FULL(),
        DENY()
    }

    private static class RegisteredTeam {

        private String name;
        private String oldTeamName;
        private TeamModule team;

        public RegisteredTeam(String name, TeamModule team) {
            this.name = name;
            this.oldTeamName = team.getName();
            this.team = team;
        }

        public String getName() {
            return name;
        }

        public String getOldName() {
            return oldTeamName;
        }

        public TeamModule getTeam() {
            return team;
        }

    }

}
