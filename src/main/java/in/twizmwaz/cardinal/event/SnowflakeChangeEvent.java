package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.modules.snowflakes.Snowflakes;
import in.twizmwaz.cardinal.util.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SnowflakeChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Snowflakes.ChangeReason changeReason;
    private final int rawAmount;
    private final double multiplier;
    private final int finalAmount;
    private final String[] args;

    public SnowflakeChangeEvent(Player player, Snowflakes.ChangeReason changeReason, int rawAmount, String... args) {
        this.player = player;
        this.changeReason = changeReason;
        this.rawAmount = rawAmount;
        this.multiplier = Players.getSnowflakeMultiplier(player);
        this.finalAmount = (int) (rawAmount * multiplier);
        this.args = args;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public int getRawAmount() {
        return rawAmount;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public String get(int i) {
        return args[i];
    }

    public Snowflakes.ChangeReason getChangeReason() {
        return changeReason;
    }
}
