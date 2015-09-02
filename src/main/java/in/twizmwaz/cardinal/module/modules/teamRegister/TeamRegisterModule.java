package in.twizmwaz.cardinal.module.modules.teamRegister;

import com.google.common.collect.Maps;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Authenticator;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Map;

public class TeamRegisterModule implements Module {

    private final Match match;

    protected TeamRegisterModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    Map<String, String> playersTeams = Maps.newHashMap();
    Map<String, TeamModule> usedTeams = Maps.newHashMap();
    ArrayList<String> registeredTeams = new ArrayList<>();
    ArrayList<String> whitelistedPlayers = new ArrayList<>();

    public Map<String, String> getPlayersTeams() {
        return playersTeams;
    }

    public Map<String, TeamModule> getUsedTeams() {
        return usedTeams;
    }

    public String addRegisteredPlayer(String playerName, String ocnTeam) {
        TeamModule team = Teams.getTeamByName(ocnTeam).get();
        Player p = Bukkit.getPlayer(playerName);
        if(team.size() >= team.getMax()) {
            return "full " + team.size();
        }
        if(Authenticator.authenticateTeam(playerName, ocnTeam)) {
            if(p == null) {
                OfflinePlayer player = Players.matchSinglePlayer("@" + playerName.toLowerCase());
                if(!player.isWhitelisted()) {
                    player.setWhitelisted(true);
                    whitelistedPlayers.add(playerName);
                }
            } else {
                if(!p.isWhitelisted()) {
                    p.setWhitelisted(true);
                    whitelistedPlayers.add(playerName);
                }
                team.add(p);
            }
            playersTeams.put(playerName, ocnTeam);
            return "true";

        } else {
            return "false";
        }
    }

    public void removeRegisteredPlayer(Player player) {
        playersTeams.remove(player.getName());
        if(whitelistedPlayers.contains(player.getName())) {
            player.setWhitelisted(false);
        }
        Teams.getTeamById("observers").get().add(player);
    }

    public ArrayList getRegisteredTeams() {
        return registeredTeams;
    }


    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        String playerName = e.getName();
        OfflinePlayer player = Players.matchSinglePlayer("@" + playerName.toLowerCase());
        for (String ocnTeam : registeredTeams) {
            String allow = addRegisteredPlayer(playerName, ocnTeam);
            if(allow.equals("true")) {
                e.allow();
            } else if(allow.contains("full")){
                String players = allow.replace("full", "").replace(" ", "");
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, new LocalizedChatMessage(ChatConstant.ERROR_CANT_JOIN_TEAM_FULL, players).getMessage(player.getPlayer().getLocale()));
            }
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        removeRegisteredPlayer(e.getPlayer());
    }
}
