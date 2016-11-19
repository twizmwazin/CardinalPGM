package in.twizmwaz.cardinal.module.modules.worldFreeze;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEntityEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldFreeze implements Module {

    private final Match match;
    private String daylight;

    protected WorldFreeze(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onLiquidFlow(BlockFromToEvent event) {
        if (!match.isRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (!match.isRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        if (!match.isRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEntityEvent event) {
        if (!match.isRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (!match.isRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        if (!match.isRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        if (!match.isRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (!match.isRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPysics(BlockPhysicsEvent event) {
        if (!match.isRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!match.isRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event) {
        if (!match.isRunning()) {
            event.setNewCurrent(event.getOldCurrent());
        }
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if (!match.isRunning()) {
            event.setCancelled(true);
            event.setBurning(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onCycle(CycleCompleteEvent event) {
        daylight = GameHandler.getGameHandler().getMatchWorld().getGameRuleValue("doDaylightCycle");
        GameHandler.getGameHandler().getMatchWorld().setGameRuleValue("doDaylightCycle", "false");
    }

    @EventHandler
    public void onStart(MatchStartEvent event) {
        GameHandler.getGameHandler().getMatchWorld().setGameRuleValue("doDaylightCycle", daylight);
    }

    @EventHandler
    public void onEnd(MatchEndEvent event) {
        GameHandler.getGameHandler().getMatchWorld().setGameRuleValue("doDaylightCycle", "false");
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        if (!match.isRunning()) {
            event.setCancelled(true);
        }
    }

}
