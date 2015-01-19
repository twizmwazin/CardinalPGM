package in.twizmwaz.cardinal.module.modules.teamManager;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.PlayerUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class TeamManagerModule implements Module {

    private final JavaPlugin plugin;
    private final Match match;

    protected TeamManagerModule(Match match) {
        this.plugin = GameHandler.getGameHandler().getPlugin();
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        TeamUtils.getTeamById("observers").add(player, true);
        //event.getPlayer().setScoreboard(TeamUtil.getTeamById("observers").getScoreboard());
        PlayerUtils.resetPlayer(player);

        event.getPlayer().getInventory().setItem(0, new ItemStack(Material.COMPASS));
        ItemStack howTo = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta howToMeta = howTo.getItemMeta();
        howToMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Coming Soon");
        howTo.setItemMeta(howToMeta);
        BookMeta howToBookMeta = (BookMeta) howTo.getItemMeta();
        howToBookMeta.setAuthor(ChatColor.GOLD + "CardinalPGM");
        howTo.setItemMeta(howToBookMeta);
        event.getPlayer().getInventory().setItem(1, howTo);
        if (!GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
            ItemStack picker = new ItemStack(Material.LEATHER_HELMET);
            ItemMeta pickerMeta = picker.getItemMeta();
            pickerMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Team Selection");
            pickerMeta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Join the game!"));
            picker.setItemMeta(pickerMeta);
            event.getPlayer().getInventory().setItem(2, picker);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerKickEvent event) {
        removePlayer(event.getPlayer());
    }

    private void removePlayer(Player player) {
        for (TeamModule team : match.getModules().getModules(TeamModule.class)) {
            if (team.contains(player)) {
                team.remove(player);
            }
        }
    }


}
