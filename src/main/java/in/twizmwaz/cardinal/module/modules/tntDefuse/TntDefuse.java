package in.twizmwaz.cardinal.module.modules.tntDefuse;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.chatChannels.AdminChannel;
import in.twizmwaz.cardinal.module.modules.chatChannels.ChatChannel;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerAttackEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TntDefuse implements Module {
    private Map<String, UUID> tntPlaced = new HashMap<>();

    protected TntDefuse() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerLeftClick(PlayerAttackEntityEvent event) {
        if (event.getLeftClicked() instanceof TNTPrimed) {
            if (TntTracker.getWhoPlaced(event.getLeftClicked()) != null) {
                UUID player = TntTracker.getWhoPlaced(event.getLeftClicked());
                Optional<TeamModule> playerTeam = Teams.getTeamByPlayer(Bukkit.getPlayer(player));
                if (Bukkit.getOfflinePlayer(player).isOnline()) {
                    if (playerTeam.isPresent() && playerTeam.get().equals(Teams.getTeamByPlayer(event.getPlayer()).orNull()) && Teams.getTeamByPlayer(Bukkit.getPlayer(player)) != null && !playerTeam.get().isObserver()) {
                        if (!event.getLeftClicked().getLocation().getBlock().isLiquid()) {
                            if (!Bukkit.getPlayer(player).equals(event.getPlayer())) {
                                event.getLeftClicked().remove();
                                event.getPlayer().sendMessage(ChatColor.RED + "You defused " + playerTeam.get().getColor() + Bukkit.getPlayer(player).getDisplayName() + ChatColor.RED + "'s TNT.");
                                ChatChannel channel = GameHandler.getGameHandler().getMatch().getModules().getModule(AdminChannel.class);
                                channel.sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + Teams.getTeamByPlayer(event.getPlayer()).get().getColor() + event.getPlayer().getDisplayName() + ChatColor.RESET + " defused " + playerTeam.get().getColor() + Bukkit.getPlayer(player).getDisplayName() + ChatColor.RESET + "'s " + ChatColor.DARK_RED + "TNT");
                            }
                        } else {
                            ChatUtil.sendWarningMessage(event.getPlayer(), "You may not defuse TNT in water!");
                        }
                    } else {
                        ChatUtil.sendWarningMessage(event.getPlayer(), "You may not defuse enemy TNT.");
                    }
                } else {
                    ChatUtil.sendWarningMessage(event.getPlayer(), "You may not defuse enemy TNT.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof TNTPrimed && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().equals(Material.SHEARS)) {
            if (TntTracker.getWhoPlaced(event.getRightClicked()) != null) {
                UUID player = TntTracker.getWhoPlaced(event.getRightClicked());
                Optional<TeamModule> playerTeam = Teams.getTeamByPlayer(Bukkit.getPlayer(player));
                if (Bukkit.getOfflinePlayer(player).isOnline()) {
                    if (event.getPlayer().hasPermission("tnt.defuse") || Teams.getTeamByPlayer(Bukkit.getPlayer(player)).orNull() == Teams.getTeamByPlayer(event.getPlayer()).orNull()) {
                        if (!event.getRightClicked().getLocation().getBlock().isLiquid()) {
                            if (!Bukkit.getPlayer(player).equals(event.getPlayer())) {
                                event.getRightClicked().remove();
                                ChatColor color = Teams.getTeamColorByPlayer(Bukkit.getOfflinePlayer(player));
                                event.getPlayer().sendMessage(ChatColor.RED + "You defused " + color + Bukkit.getPlayer(player).getDisplayName() + ChatColor.RED + "'s TNT.");
                                ChatChannel channel = GameHandler.getGameHandler().getMatch().getModules().getModule(AdminChannel.class);
                                channel.sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + Teams.getTeamColorByPlayer(event.getPlayer()) + event.getPlayer().getDisplayName() + ChatColor.RESET + " defused " + color + Bukkit.getPlayer(player).getDisplayName() + ChatColor.RESET + "'s " + ChatColor.DARK_RED + "TNT");
                            }
                        } else {
                            ChatUtil.sendWarningMessage(event.getPlayer(), "You may not defuse TNT in water!");
                        }
                    } else {
                        ChatUtil.sendWarningMessage(event.getPlayer(), "You may not defuse enemy TNT.");
                    }
                } else {
                    ChatUtil.sendWarningMessage(event.getPlayer(), "You may not defuse enemy TNT.");
                }
            }
        }
    }
}
