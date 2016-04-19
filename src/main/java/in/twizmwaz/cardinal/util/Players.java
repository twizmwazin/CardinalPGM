package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.rank.Rank;
import in.twizmwaz.cardinal.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.potion.PotionEffect;

import java.util.UUID;

public class Players {

    public static void resetPlayer(Player player) {
        resetPlayer(player, true);
    }

    public static void resetPlayer(Player player, boolean heal) {
        if (heal) player.setHealth(player.getMaxHealth());
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
        player.setLevel(0);
        player.setPotionParticles(false);
        player.setWalkSpeed(0.2F);
        player.setFlySpeed(0.1F);
        player.setArrowsStuck(0);

        player.hideTitle();

        player.setFastNaturalRegeneration(false);

        for (Attribute attribute : Attribute.values()) {
            if (player.getAttribute(attribute) == null) continue;
            for (AttributeModifier modifier : player.getAttribute(attribute).getModifiers()) {
                player.getAttribute(attribute).removeModifier(modifier);
            }
        }
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 4.001D, AttributeModifier.Operation.ADD_SCALAR));
        player.getAttribute(Attribute.ARROW_ACCURACY).addModifier(new AttributeModifier(UUID.randomUUID(), "sportbukkit.arrowAccuracy", -1D, AttributeModifier.Operation.ADD_NUMBER));
        player.getAttribute(Attribute.ARROW_VELOCITY_TRANSFER).addModifier(new AttributeModifier(UUID.randomUUID(), "sportbukkit.arrowVelocityTransfer", -1D, AttributeModifier.Operation.ADD_NUMBER));
    }

    public static void canInteract(Player player, boolean state) {
        player.setAffectsSpawning(state);
        player.setCollidesWithEntities(state);
        player.setCanPickupItems(state);
    }

    public static void setObserver(Player player) {
        player.setGameMode(GameMode.CREATIVE);
        player.setAllowFlight(true);
        player.setFlying(true);
        canInteract(player, false);
        resetPlayer(player, false);
        player.closeInventory();
    }

    public static void playSoundEffect(Player player, Location loc, Sound sound, float volume, float pitch) {
        if (Settings.getSettingByName("Sounds") != null && Settings.getSettingByName("Sounds").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
            player.playSound(loc, sound, volume, pitch);
        }
    }

    public static void playSoundEffect(Player player, Sound sound, float volume, float pitch) {
        playSoundEffect(player, player.getLocation(), sound, volume, pitch);
    }

    public static void broadcastSoundEffect(Location loc, Sound sound, float volume, float pitch) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            playSoundEffect(player, loc, sound, volume, pitch);
        }
    }

    public static void broadcastSoundEffect(Sound sound, float volume, float pitch) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            playSoundEffect(player, sound, volume, pitch);
        }
    }

    public static void broadcastSoundEffect(TeamModule team, Location loc, Sound sound, float volume, float pitch) {
        if (team == null) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (team.contains(player)) playSoundEffect(player, loc, sound, volume, pitch);
        }
    }

    public static void broadcastSoundEffect(TeamModule team, Sound sound, float volume, float pitch) {
        if (team == null) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (team.contains(player)) playSoundEffect(player, sound, volume, pitch);
        }
    }

    public static double getSnowflakeMultiplier(OfflinePlayer player) {
        if (player.isOp()) return 2.5;
        if (PermissionModule.isDeveloper(player.getUniqueId())) return 2.0;
        return 1.0;
    }

    public static String getName(ServerOperator who, boolean flairs) {
        if (who instanceof OfflinePlayer) {
            OfflinePlayer player = (OfflinePlayer) who;
            return player.isOnline() ? (flairs ? player.getPlayer().getDisplayName() : Teams.getTeamColorByPlayer(player) + player.getPlayer().getName()) : Rank.getPrefix(player.getUniqueId()) + ChatColor.DARK_AQUA + player.getName();
        } else {
            return ChatColor.GOLD + "\u2756" + ChatColor.DARK_AQUA + "Console";
        }
    }

    public static String getName(ServerOperator who) {
        return getName(who, true);
    }

}
