package in.twizmwaz.cardinal.module.modules.freeze;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.chatChannels.AdminChannel;
import in.twizmwaz.cardinal.module.modules.titleRespawn.TitleRespawn;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Players;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerAttackEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class FreezeModule implements Module {

    private TitleRespawn titleRespawn;
    private Set<Player> frozenPlayers = new HashSet<>();

    protected FreezeModule(Match match) {
        this.titleRespawn = match.getModules().getModule(TitleRespawn.class);
    }

    public Set<Player> getFrozenPlayers() {
        return frozenPlayers;
    }

    @Override
    public void unload() {
        for (Player player : frozenPlayers.toArray(new Player[frozenPlayers.size()])) unfreezePlayer(player, Bukkit.getConsoleSender());
        HandlerList.unregisterAll(this);
    }

    public void togglePlayerFreeze(Player player, CommandSender freezer) {
        if (!frozenPlayers.contains(player)) {
            freezePlayer(player, freezer);
        } else {
            unfreezePlayer(player, freezer);
        }
    }

    private void freezePlayer(Player player, CommandSender freezer) {
        if (frozenPlayers.contains(player)) return;
        ChatUtil.sendWarningMessage(player, new LocalizedChatMessage(ChatConstant.GENERIC_FROZEN_BY, Players.getName(freezer)));
        ChatUtil.getAdminChannel().sendLocalizedMessage(new UnlocalizedChatMessage(AdminChannel.getPrefix() + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_FROZE, Players.getName(freezer) + ChatColor.RED, player.getDisplayName())));
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
        titleRespawn.sendArmorStandPacket(player);
        player.showTitle(new TextComponent(""), new TextComponent(ChatColor.RED + new LocalizedChatMessage(ChatConstant.GENERIC_FROZEN_BY, Players.getName(freezer)).getMessage(player.getLocale())), 0, Integer.MAX_VALUE, 0);
        frozenPlayers.add(player);
    }

    private void unfreezePlayer(Player player, CommandSender freezer) {
        if (!frozenPlayers.contains(player)) return;
        player.sendMessage(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.GENERIC_UNFROZEN_BY, Players.getName(freezer)).getMessage(player.getLocale()));
        ChatUtil.getAdminChannel().sendLocalizedMessage(new UnlocalizedChatMessage(AdminChannel.getPrefix() + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_UNFROZE, Players.getName(freezer) + ChatColor.RED, player.getDisplayName())));
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 2);
        titleRespawn.destroyArmorStandPacket(player);
        player.hideTitle();
        frozenPlayers.remove(player);
    }

    @EventHandler
    public void onPlayerClick(PlayerAttackEntityEvent event) {
        if (event.getPlayer().hasPermission("cardinal.punish.freeze") && event.getLeftClicked() instanceof Player) {
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            if (item != null && item.getType() == Material.ICE
                    && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null
                    && item.getItemMeta().getDisplayName().equals(ChatColor.AQUA + ChatConstant.UI_FREEZE_ITEM.getMessage(event.getPlayer().getLocale()))) {
                togglePlayerFreeze((Player) event.getLeftClicked(), event.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteractEvent(PlayerInteractEntityEvent event) {
        if (event.getHand().equals(EquipmentSlot.HAND) && event.getPlayer().hasPermission("cardinal.punish.freeze") && event.getRightClicked() instanceof Player) {
            ItemStack item = event.getPlayer().getInventory().getItem(event.getHand());
            if (item != null && item.getType() == Material.ICE
                    && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null
                    && item.getItemMeta().getDisplayName().equals(ChatColor.AQUA + ChatConstant.UI_FREEZE_ITEM.getMessage(event.getPlayer().getLocale()))) {
                event.setCancelled(true);
                togglePlayerFreeze((Player) event.getRightClicked(), event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (frozenPlayers.contains(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        unfreezePlayer(event.getPlayer(), Bukkit.getConsoleSender());
    }

}
