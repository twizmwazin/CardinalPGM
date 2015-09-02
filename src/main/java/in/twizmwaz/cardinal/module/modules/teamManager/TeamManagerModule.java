package in.twizmwaz.cardinal.module.modules.teamManager;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Align;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.module.modules.teamRegister.TeamRegisterModule;
import in.twizmwaz.cardinal.util.Contributor;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.Map;

public class TeamManagerModule implements Module {

    private final Match match;

    protected TeamManagerModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Players.resetPlayer(player);
        String playerName = event.getPlayer().getName();
        Map<String, String> playerTeams = GameHandler.getGameHandler().getMatch().getModules().getModule(TeamRegisterModule.class).getPlayersTeams();
        Teams.getTeamById("observers").get().add(player, true, false);
        if(playerTeams.containsKey(playerName)) {
            Teams.getTeamByName(playerTeams.get(playerName)).get().add(player);
        }
        event.setJoinMessage(null);
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            if (!player1.equals(player)) {
                player1.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.UI_PLAYER_JOIN, Teams.getTeamColorByPlayer(player) + player.getDisplayName() + ChatColor.YELLOW)).getMessage(player1.getLocale()));
            }
        }
        Bukkit.getConsoleSender().sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.UI_PLAYER_JOIN, Teams.getTeamColorByPlayer(player) + player.getDisplayName() + ChatColor.YELLOW)).getMessage(Locale.getDefault().toString()));

        sendMapMessage(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            if (!player1.equals(player)) {
                player1.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.UI_PLAYER_LEAVE, Teams.getTeamColorByPlayer(player) + player.getDisplayName() + ChatColor.YELLOW)).getMessage(player1.getLocale()));
            }
        }
        Bukkit.getConsoleSender().sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.UI_PLAYER_LEAVE, Teams.getTeamColorByPlayer(player) + player.getDisplayName() + ChatColor.YELLOW)).getMessage(Locale.getDefault().toString()));
        removePlayer(player);
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendMapMessage(player);
        }
        sendMapMessage(Bukkit.getConsoleSender());
    }

    private void sendMapMessage(CommandSender player) {
        List<String> result = Lists.newArrayList();
        result.addAll(Align.wordWrap("" + ChatColor.BLUE + ChatColor.ITALIC + match.getLoadedMap().getObjective(), 192));
        result.addAll(Align.wordWrap(ChatColor.DARK_GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_CREATED_BY,
                ChatUtil.toChatMessage(match.getLoadedMap().getAuthors().stream().map(Contributor::getDisplayName)
                        .collect(Collectors.toList()), ChatColor.DARK_AQUA, ChatColor.DARK_GRAY))
                .getMessage(ChatUtil.getLocale(player)), 192));
        player.sendMessage(Align.padMessage("" + ChatColor.AQUA + ChatColor.BOLD +
                GameHandler.getGameHandler().getMatch().getLoadedMap().getName(), ChatColor.WHITE, 200));
        for (String line : result) player.sendMessage(" " + line);
        player.sendMessage(Align.getDash(ChatColor.WHITE, 200));
    }

    @EventHandler
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (event.getNewTeam().isPresent() && !event.getNewTeam().get().isObserver() && GameHandler.getGameHandler().getMatch().isRunning()) {
            Bukkit.dispatchCommand(event.getPlayer(), "match");
        }
    }

    private void removePlayer(Player player) {
        TeamModule observers = Teams.getTeamById("observers").get();
        observers.add(player, true, false);
        observers.remove(player);
        Players.resetPlayer(player);
    }

}
