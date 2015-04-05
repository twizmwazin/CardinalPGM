package in.twizmwaz.cardinal.module.modules.scorebox;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Scorebox implements Module {

    private final RegionModule region;
    private final int points;
    private final FilterModule filter;
    private final HashMap<ItemStack, Integer> redeemables;

    protected Scorebox(final RegionModule region, final int points, final FilterModule filter, final HashMap<ItemStack, Integer> redeemables) {
        this.region = region;
        this.points = points;
        this.filter = filter;
        this.redeemables = redeemables;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if (region.contains(event.getTo().toVector()) && !region.contains(event.getFrom().toVector())) {
                if (filter == null || filter.evaluate(event.getPlayer()).equals(FilterState.ALLOW)) {
                    if (event.getPlayer().getHealth() > 0) {
                        int points = 0;
                        if (redeemables.size() > 0) {
                            for (ItemStack item : redeemables.keySet()) {
                                if (event.getPlayer().getInventory().contains(item)) {
                                    for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                                        TeamModule playerTeam = TeamUtils.getTeamByPlayer(event.getPlayer());
                                        if (playerTeam != null && score.getTeam() == playerTeam) {
                                            event.getPlayer().getInventory().remove(item);
                                            points += redeemables.get(item);
                                        }
                                    }
                                }
                            }
                        }
                        points += this.points;
                        if (points != 0) {
                            TeamModule playerTeam = TeamUtils.getTeamByPlayer(event.getPlayer());
                            if (playerTeam != null) {
                                for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                                    if (score.getTeam() == playerTeam) {
                                        score.setScore(score.getScore() + points);
                                        Bukkit.getServer().getPluginManager().callEvent(new ScoreUpdateEvent(score));
                                        ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}", new LocalizedChatMessage(ChatConstant.UI_SCORED_FOR, new UnlocalizedChatMessage(playerTeam.getColor() + event.getPlayer().getName() + ChatColor.GRAY), new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}" + ChatColor.GRAY, points == 1 ? new LocalizedChatMessage(ChatConstant.UI_ONE_POINT) : new LocalizedChatMessage(ChatConstant.UI_POINTS, points + "" + ChatColor.GRAY)), new UnlocalizedChatMessage(playerTeam.getCompleteName()))));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
