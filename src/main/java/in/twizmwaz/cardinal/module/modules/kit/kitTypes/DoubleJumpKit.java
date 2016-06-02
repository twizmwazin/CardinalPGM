package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.module.modules.kit.KitRemovable;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerOnGroundEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Parts of code borrowed from
 * https://github.com/rmsy/DoubleJump
 *
 * Modified for use with CardinalPGM
 */

public class DoubleJumpKit implements KitRemovable {

    private boolean enabled;
    private int power;
    private double rechargeTime;
    private boolean rechargeBeforeLanding;
    private long lastUpdate = 0L;

    List<UUID> players = new ArrayList<>();
    List<UUID> landed = new ArrayList<>();

    public DoubleJumpKit(boolean enabled, int power, double rechargeTime, final boolean rechargeBeforeLanding) {
        this.enabled = enabled;
        this.power = power;
        this.rechargeTime = rechargeTime;
        this.rechargeBeforeLanding = rechargeBeforeLanding;

        Bukkit.getScheduler().runTaskTimer(Cardinal.getInstance(), new Runnable() {
            public void run() {
                DoubleJumpKit.this.update();
            }
        }, 0, 2);
    }

    private void update() {
        int diff = (int) (System.currentTimeMillis() - lastUpdate);
        lastUpdate = System.currentTimeMillis();
        float toAddExp = rechargeTime > 0 ? (float) (diff / (rechargeTime * 1000)) : 1.0f;
        for(UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if(player.getExp() < 1.0f && (rechargeBeforeLanding || landed.contains(uuid))) {
                player.setExp(player.getExp() + toAddExp > 1.0f ? 1.0f : player.getExp() + toAddExp);
            } else if(player.getExp() > 1.0f) {
                player.setExp(1.0f);
            }
            if(player.getExp() >= 1.0f) {
                player.setAllowFlight(true);
            }
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        if (!enabled) return;
        players.add(player.getUniqueId());
        player.setExp(1.0f);
        player.setAllowFlight(true);
    }

    @Override
    public void remove(Player player) {
        if (!players.contains(player.getUniqueId())) return;
        players.remove(player.getUniqueId());
        player.setExp(0.0f);
        player.setAllowFlight(false);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMatchEnd(MatchEndEvent event) {
        enabled = false;
        for(UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            player.setExp(0.0f);
            player.setAllowFlight(false);
        }
        landed.clear();
        players.clear();
    }

    @EventHandler
    public void onPlayerGround(PlayerOnGroundEvent event) {
        if (!enabled || rechargeBeforeLanding) return;
        UUID id = event.getPlayer().getUniqueId();
        if (!players.contains(id) || landed.contains(id)) return;
        if (event.getOnGround()) landed.add(id);
    }

    @EventHandler
    public void onPlayerToggleFly(PlayerToggleFlightEvent event) {
        if (!enabled) return;
        Player player = event.getPlayer();
        if (!players.contains(player.getUniqueId()) || player.getExp() > 1.0f || !event.isFlying()) return;
        player.setAllowFlight(false);
        player.setExp(0.0f);
        event.setCancelled(true);

        Vector normal = player.getEyeLocation().getDirection();
        normal.setY(0.75 + Math.max(normal.getY() * 0.5, 0));
        normal.multiply(power / 2);
        event.getPlayer().setVelocity(normal);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 0.5f, 1.8f);

        update();
    }

}
