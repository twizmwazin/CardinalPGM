package in.twizmwaz.cardinal.util;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.chatChannels.ChatChannel;
import in.twizmwaz.cardinal.module.modules.chatChannels.GlobalChannel;
import in.twizmwaz.cardinal.module.modules.chatChannels.TeamChannel;
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.spawn.SpawnModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Teams {

    public static Optional<TeamModule> getTeamWithFewestPlayers(Match match) {
        TeamModule result = null;
        double percent = Double.POSITIVE_INFINITY;
        for (TeamModule team : getTeams()) {
            if (!team.isObserver() && (team.size() / (double) team.getMax()) < percent) {
                result = team;
                percent = team.size() / (double) team.getMax();
            }
        }
        if (result == null) return Optional.absent();
        else return Optional.of(result);
    }

    public static Optional<TeamModule> getTeamByName(String name) {
        if (name == null) return null;
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getName().replaceAll(" ", "").toLowerCase().startsWith(name.replaceAll(" ", "").toLowerCase())) {
                return Optional.of(team);
            }
        }
        return Optional.absent();
    }

    public static Optional<TeamModule> getTeamById(String id) {
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getId().replaceAll(" ", "").equalsIgnoreCase(id.replaceAll(" ", ""))) {
                return Optional.of(team);
            }
        }
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getId().replaceAll(" ", "").toLowerCase().startsWith(id.replaceAll(" ", "").toLowerCase())) {
                return Optional.of(team);
            }
        }
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getId().replaceAll(" ", "-").equalsIgnoreCase(id.replaceAll(" ", "-"))) {
                return Optional.of(team);
            }
        }
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getId().replaceAll(" ", "-").toLowerCase().startsWith(id.replaceAll(" ", "-").toLowerCase())) {
                return Optional.of(team);
            }
        }
        return Optional.absent();
    }

    public static Optional<TeamModule> getTeamByPlayer(Player player) {
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.contains(player)) {
                return Optional.of(team);
            }
        }
        return Optional.absent();
    }

    public static ModuleCollection<TeamModule> getTeams() {
        return GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class);
    }

    public static ModuleCollection<GameObjective> getObjectives(TeamModule team) {
        ModuleCollection<GameObjective> objectives = new ModuleCollection<>();
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (objective instanceof WoolObjective) {
                if (objective.getTeam() == team) {
                    objectives.add(objective);
                }
            } else if(objective instanceof HillObjective) {
                objectives.add(objective);
            } else if (objective.getTeam() != team) {
                objectives.add(objective);
            }
        }
        return objectives;
    }

    public static ModuleCollection<GameObjective> getShownObjectives(TeamModule team) {
        ModuleCollection<GameObjective> objectives = new ModuleCollection<>();
        for (GameObjective objective : getObjectives(team)) {
            if (objective.showOnScoreboard() && !(objective instanceof HillObjective) && !(objective instanceof FlagObjective && ((FlagObjective) objective).multipleAttackers())) {
                objectives.add(objective);
            }
        }
        return objectives;
    }

    public static ModuleCollection<GameObjective> getShownSharedObjectives() {
        ModuleCollection<GameObjective> objectives = new ModuleCollection<>();
        for (FlagObjective flag : GameHandler.getGameHandler().getMatch().getModules().getModules(FlagObjective.class)) {
            if (flag.showOnScoreboard() && flag.multipleAttackers()) objectives.add(flag);
        }
        return objectives;
    }

    public static ModuleCollection<GameObjective> getRequiredObjectives(TeamModule team) {
        ModuleCollection<GameObjective> objectives = new ModuleCollection<>();
        for (GameObjective objective : getObjectives(team)) {
            if (objective.isRequired()) {
                objectives.add(objective);
            }
        }
        return objectives;
    }

    public static ChatChannel getTeamChannel(Optional<TeamModule> team) {
        if (team.isPresent()) {
            for (TeamChannel channel : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamChannel.class)) {
                if (channel.getTeam() == team.get()) return channel;
            }
        }
        return GameHandler.getGameHandler().getMatch().getModules().getModule(GlobalChannel.class);
    }

    public static ChatColor getTeamColorByPlayer(OfflinePlayer player) {
        if (player.isOnline()) {
            Optional<TeamModule> team = getTeamByPlayer(player.getPlayer());
            if (team.isPresent()) return team.get().getColor();
            else return ChatColor.YELLOW;
        } else return ChatColor.DARK_AQUA;
    }

    public static boolean teamsReady() {
        for (TeamModule team : getTeams()) {
            if (!team.isReady()) return false;
        }
        return true;
    }

    public static boolean teamsNoObsReady() {
        for (TeamModule team : getTeams()) {
            if (!team.isReady() && !team.isObserver()) return false;
        }
        return true;
    }

    public static ModuleCollection<SpawnModule> getSpawns(TeamModule team) {
        ModuleCollection<SpawnModule> spawns = new ModuleCollection<>();
        for (SpawnModule spawnModule : GameHandler.getGameHandler().getMatch().getModules().getModules(SpawnModule.class)) {
            if (team.isObserver()) {
                if (spawnModule.getTeam() != null && spawnModule.getTeam().isObserver()) spawns.add(spawnModule);
            } else if (spawnModule.getTeam() == null || spawnModule.getTeam().equals(team)) {
                spawns.add(spawnModule);
            }
        }
        return spawns;
    }

    public static boolean isFFA(Match match) {
        for (TeamModule team : match.getModules().getModules(TeamModule.class)) {
            if (!team.isObserver()) return false;
        }
        return true;
    }

    public static void setPlayerTeam(Player player, String team) throws Exception {
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.CYCLING)) {
            throw new Exception(ChatUtil.getWarningMessage(new LocalizedChatMessage(ChatConstant.ERROR_MATCH_OVER).getMessage(player.getLocale())));
        }
        Optional<TeamModule> originalTeam = Teams.getTeamByPlayer(player);
        if (team.equals("") && originalTeam.isPresent() && !originalTeam.get().isObserver()) {
            throw new Exception(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_JOINED, Teams.getTeamByPlayer(player).get().getCompleteName() + ChatColor.RED).getMessage(player.getLocale())));
        }
        Optional<TeamModule> destinationTeam;
        if (!team.equals("")) {
            destinationTeam = getTeamByName(team);
            if (!destinationTeam.isPresent()) {
                if ("observers".startsWith(team.toLowerCase())) {
                    setPlayerTeam(player, getTeamById("observers").get().getName());
                    return;
                }
                throw new Exception(ChatConstant.ERROR_NO_TEAM_MATCH.getMessage(ChatUtil.getLocale(player)));
            }
            if (destinationTeam.get().contains(player)) {
                throw new Exception(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_JOINED, destinationTeam.get().getCompleteName() + ChatColor.RED).getMessage(ChatUtil.getLocale(player))));
            }
            destinationTeam.get().add(player, false);
        } else {
            destinationTeam = Teams.getTeamWithFewestPlayers(GameHandler.getGameHandler().getMatch());
            if (destinationTeam.isPresent()) {
                destinationTeam.get().add(player, false);
            } else {
                throw new Exception(ChatConstant.ERROR_TEAMS_FULL.getMessage(ChatUtil.getLocale(player)));
            }
        }
    }
}
