package in.twizmwaz.cardinal.module.modules.scorebox;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Scorebox implements Module {

    private final RegionModule region;
    private final int points;
    private final FilterModule filter;
    private final boolean silent;
    private final Map<ItemStack, Integer> redeemables;

    protected Scorebox(final RegionModule region, final int points, final FilterModule filter, final boolean silent, final Map<ItemStack, Integer> redeemables) {
        this.region = region;
        this.points = points;
        this.filter = filter;
        this.silent = silent;
        this.redeemables = redeemables;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (!ObserverModule.testObserverOrDead(event.getPlayer()) && region.contains(event.getPlayer().getLocation()) &&
                (filter == null || filter.evaluate(event.getPlayer()).equals(FilterState.ALLOW))) {
            addScore(event.getPlayer(), redeem(event.getPlayer()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!ObserverModule.testObserverOrDead(event.getPlayer()) && region.contains(event.getTo().toVector()) && !region.contains(event.getFrom().toVector()) &&
                (filter == null || filter.evaluate(event.getPlayer()).equals(FilterState.ALLOW))) {
            addScore(event.getPlayer(), redeem(event.getPlayer()) + this.points);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!ObserverModule.testObserverOrDead(event.getPlayer()) && region.contains(event.getTo().toVector()) && !region.contains(event.getFrom().toVector()) &&
                (filter == null || filter.evaluate(event.getPlayer()).equals(FilterState.ALLOW))) {
            addScore(event.getPlayer(), redeem(event.getPlayer()) + this.points);
        }
    }

    public int redeem(Player player) {
        int points = 0;
        if (redeemables.size() > 0) {
            for (ItemStack item : redeemables.keySet()) {
                if (player.getInventory().contains(item.getType())) {
                    double itemCount = 0;
                    for (ItemStack itemstack : player.getInventory().getContents()) {
                        if (itemstack != null && itemstack.getType() == item.getType()) {
                            itemCount += itemstack.getAmount();
                        }
                    }
                    player.getInventory().remove(item.getType());
                    points += (redeemables.get(item) * itemCount);
                }
            }
        }
        return points;
    }

    public void addScore(Player player, int points) {
        Optional<TeamModule> playerTeam = Teams.getTeamByPlayer(player);
        if (points != 0 && playerTeam.isPresent()) {
            for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                if (score.getTeam() == playerTeam.get()) {
                    if (!this.silent) {
                        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}",
                                new LocalizedChatMessage(ChatConstant.UI_SCORED_FOR,
                                        new UnlocalizedChatMessage(playerTeam.get().getColor() + player.getName() + ChatColor.GRAY),
                                        new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}" + ChatColor.GRAY, points == 1 ? new LocalizedChatMessage(ChatConstant.UI_ONE_POINT) : new LocalizedChatMessage(ChatConstant.UI_POINTS, points + "" + ChatColor.GRAY)),
                                        new UnlocalizedChatMessage(playerTeam.get().getCompleteName()))));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    }
                    score.addScore(points);
                }
            }
        }
    }

}


