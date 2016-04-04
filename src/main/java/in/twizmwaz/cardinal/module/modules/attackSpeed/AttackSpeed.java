package in.twizmwaz.cardinal.module.modules.attackSpeed;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.Teams;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAttackEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AttackSpeed implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onClickEvent(PlayerInteractEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (GameHandler.getGameHandler().getMatch().isRunning() && (!team.isPresent() || !team.get().isObserver())
                && (event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_AIR))) {
            sendActionBar(event.getPlayer());
        }
    }
    
    @EventHandler
    public void onPlayerAttack(PlayerAttackEntityEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (GameHandler.getGameHandler().getMatch().isRunning() && (!team.isPresent() || !team.get().isObserver())) {
            sendActionBar(event.getPlayer());
        }
    }

    private ChatColor getColor(int i) {
        return i >= 100 ? ChatColor.GREEN : i >= 80 ? ChatColor.YELLOW : i >= 50 ? ChatColor.GOLD : ChatColor.RED;
    }

    private void sendActionBar(Player player) {
        if (!Settings.getSettingByName("AttackSpeedometer").getValueByPlayer(player).getValue().equalsIgnoreCase("off")) {
            int percent = (int)(player.getAttackCooldownCoefficient() * 100);
            double cps = Math.round((20D / Math.max(1, player.getAttackCooldownTicks())) * 10D);
            String damage = "Damage: " + getColor(percent) + ChatColor.BOLD + percent + "%" + ChatColor.RESET;
            String clicks = "CPS: " + ChatColor.AQUA + ChatColor.BOLD + cps / 10 + ChatColor.RESET + "+  ";
            player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(damage + "   " + clicks));
        }
    }
    
}
