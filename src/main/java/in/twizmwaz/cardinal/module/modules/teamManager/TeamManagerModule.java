package in.twizmwaz.cardinal.module.modules.teamManager;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.ScoreboardUpdateEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.classModule.ClassModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ItemUtils;
import in.twizmwaz.cardinal.util.PlayerUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerUtils.resetPlayer(player);
        TeamUtils.getTeamById("observers").add(player, true, false);
        event.getPlayer().getInventory().setItem(0, new ItemStack(Material.COMPASS));
        ItemStack howTo = ItemUtils.createBook(Material.WRITTEN_BOOK, 1, ChatColor.AQUA.toString() + ChatColor.BOLD + "Coming Soon", ChatColor.GOLD + "CardinalPGM");
        event.getPlayer().getInventory().setItem(1, howTo);
        if (!GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
            ItemStack picker = ItemUtils.createItem(Material.LEATHER_HELMET, 1, (short)0,
                    ChatColor.GREEN + "" + ChatColor.BOLD + (GameHandler.getGameHandler().getMatch().getModules().getModule(ClassModule.class) != null ? new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(player.getLocale()) : new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(player.getLocale())),
                    Arrays.asList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_TIP).getMessage(player.getLocale())));
            player.getInventory().setItem(2, picker);
        }
        if (player.hasPermission("tnt.defuse")) {
            ItemStack shears = ItemUtils.createItem(Material.SHEARS, 1, (short)0, ChatColor.RED + new LocalizedChatMessage(ChatConstant.UI_TNT_DEFUSER).getMessage(player.getLocale()));
            player.getInventory().setItem(4, shears);
        }
        event.setJoinMessage(null);
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            if (!player1.equals(player)) {
                player1.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.UI_PLAYER_JOIN, TeamUtils.getTeamByPlayer(player).getColor() + player.getDisplayName() + ChatColor.YELLOW)).getMessage(player1.getLocale()));
            }
        }
        Bukkit.getLogger().info(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.UI_PLAYER_JOIN, TeamUtils.getTeamByPlayer(player).getColor() + player.getDisplayName() + ChatColor.YELLOW)).getMessage(Locale.getDefault().toString()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            if (!player1.equals(player)) {
                player1.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.UI_PLAYER_LEAVE, TeamUtils.getTeamByPlayer(player).getColor() + player.getDisplayName() + ChatColor.YELLOW)).getMessage(player1.getLocale()));
            }
        }
        Bukkit.getLogger().info(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.UI_PLAYER_LEAVE, TeamUtils.getTeamByPlayer(player).getColor() + player.getDisplayName() + ChatColor.YELLOW)).getMessage(Locale.getDefault().toString()));
        removePlayer(player);
    }

    private void removePlayer(Player player) {
        for (TeamModule team : match.getModules().getModules(TeamModule.class)) {
            if (team.contains(player)) {
                team.remove(player);
            }
        }
        Bukkit.getServer().getPluginManager().callEvent(new ScoreboardUpdateEvent());
    }


}
