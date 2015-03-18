package in.twizmwaz.cardinal.module.modules.worldFreeze;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
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
        if (!match.getState().equals(MatchState.PLAYING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (!match.getState().equals(MatchState.PLAYING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        if (!match.getState().equals(MatchState.PLAYING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEntityEvent event) {
        if (!match.getState().equals(MatchState.PLAYING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (!match.getState().equals(MatchState.PLAYING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        if (!match.getState().equals(MatchState.PLAYING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        if (!match.getState().equals(MatchState.PLAYING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (!match.getState().equals(MatchState.PLAYING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPysics(BlockPhysicsEvent event) {
        if (!match.getState().equals(MatchState.PLAYING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!match.getState().equals(MatchState.PLAYING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event) {
        if (!match.getState().equals(MatchState.PLAYING)) {
            event.setNewCurrent(event.getOldCurrent());
        }
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if (!match.getState().equals(MatchState.PLAYING)) {
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

}
