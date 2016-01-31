package in.twizmwaz.cardinal.module.modules.teamManager;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.classModule.ClassModule;
import in.twizmwaz.cardinal.module.modules.respawn.RespawnModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.tutorial.Tutorial;
import in.twizmwaz.cardinal.util.Contributor;
import in.twizmwaz.cardinal.util.Items;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
        Teams.getTeamById("observers").get().add(player, true, false);
        GameHandler.getGameHandler().getMatch().getModules().getModule(RespawnModule.class).giveObserversKit(player);
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
    }

    private void sendMapMessage(Player player) {
        player.sendMessage(ChatColor.STRIKETHROUGH + "--------" + ChatColor.AQUA + ChatColor.BOLD + " " + GameHandler.getGameHandler().getMatch().getLoadedMap().getName() + " " + ChatColor.RESET + ChatColor.STRIKETHROUGH + "--------");
        String line = "";
        if (GameHandler.getGameHandler().getMatch().getLoadedMap().getObjective().contains(" ")) {
            for (String word : GameHandler.getGameHandler().getMatch().getLoadedMap().getObjective().split(" ")) {
                line += word + " ";
                if (line.trim().length() > 32) {
                    line = ChatColor.BLUE + "" + ChatColor.ITALIC + line.trim();
                    player.sendMessage(" " + line);
                    line = "";
                }
            }
            if (!line.trim().equals("")) {
                line = ChatColor.BLUE + "" + ChatColor.ITALIC + line.trim();
                player.sendMessage(" " + line);
                line = "";
            }
        } else {
            line = ChatColor.BLUE + "" + ChatColor.ITALIC + GameHandler.getGameHandler().getMatch().getLoadedMap().getObjective();
            player.sendMessage(" " + line);
        }
        String locale = player.getLocale();
        String result = ChatColor.DARK_GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_CREATED_BY).getMessage(locale) + " ";
        List<Contributor> authors = GameHandler.getGameHandler().getMatch().getLoadedMap().getAuthors();
        for (Contributor author : authors) {
            if (authors.indexOf(author) < authors.size() - 2) {
                result = result + author.getDisplayName() + ChatColor.DARK_GRAY + ", ";
            } else if (authors.indexOf(author) == authors.size() - 2) {
                result = result + author.getDisplayName() + ChatColor.DARK_GRAY + " " + new LocalizedChatMessage(ChatConstant.MISC_AND).getMessage(locale) + " ";
            } else if (authors.indexOf(author) == authors.size() - 1) {
                result = result + author.getDisplayName();
            }
        }
        if (result.contains(" ")) {
            for (String word : result.split(" ")) {
                line += word + " ";
                if (line.trim().length() > 32) {
                    line = line.trim();
                    player.sendMessage(ChatColor.DARK_GRAY + " " + line);
                    line = "";
                }
            }
            if (!line.trim().equals("")) {
                line = line.trim();
                player.sendMessage(ChatColor.DARK_GRAY + " " + line);
            }
        } else {
            line = result;
            player.sendMessage(ChatColor.DARK_GRAY + " " + line);
        }
        player.sendMessage(ChatColor.STRIKETHROUGH + "---------------------------");
    }

    @EventHandler
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (event.getNewTeam().isPresent() && !event.getNewTeam().get().isObserver() && GameHandler.getGameHandler().getMatch().isRunning()) {
            Bukkit.dispatchCommand(event.getPlayer(), "match");
        }
    }

    private void removePlayer(Player player) {
        for (TeamModule team : match.getModules().getModules(TeamModule.class)) {
            if (team.contains(player)) {
                team.remove(player);
            }
        }
        this.clearHeldAttribute(player);
    }

    private void clearHeldAttribute(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        if (player.getInventory().getItemInHand() != null && !player.getInventory().getItemInHand().getType().equals(Material.AIR)) {
            craftPlayer.getHandle().getAttributeMap().a(((CraftInventoryPlayer) craftPlayer.getInventory()).getInventory().getItemInHand().B());
        }
    }

}
