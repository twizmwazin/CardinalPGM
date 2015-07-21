package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.potion.PotionEffect;

public class Players {

    public static void resetPlayer(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)});
        for (PotionEffect effect : player.getActivePotionEffects()) {
            try {
                player.removePotionEffect(effect.getType());
            } catch (NullPointerException ignored) {
            }
        }
        player.setTotalExperience(0);
        player.setExp(0);
        player.setPotionParticles(true);
        player.setWalkSpeed(0.2F);
        player.setFlySpeed(0.2F);
    }

    public static double getSnowflakeMultiplier(OfflinePlayer player) {
        if (player.isOp()) return 2.5;
        if (PermissionModule.isDeveloper(player.getUniqueId())) return 2.0;
        return 1.0;
    }

    public static String getName(ServerOperator who, boolean flairs) {
        if (who instanceof OfflinePlayer) {
            OfflinePlayer player = (OfflinePlayer) who;
            if (player.isOnline()) {
                if (flairs) {
                    return player.getPlayer().getDisplayName();
                } else {
                    return Teams.getTeamColorByPlayer(player) + player.getPlayer().getName();
                }
            }
            return ChatColor.DARK_AQUA + player.getName();
        } else {
            return ChatColor.GOLD + "\u2756" + ChatColor.DARK_AQUA + "Console";
        }
    }

    public static String getName(ServerOperator who) {
        return getName(who, true);
    }

}
