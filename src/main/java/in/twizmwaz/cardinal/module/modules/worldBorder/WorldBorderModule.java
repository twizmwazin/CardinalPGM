package in.twizmwaz.cardinal.module.modules.worldBorder;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import org.bukkit.WorldBorder;
import org.bukkit.event.HandlerList;

public class WorldBorderModule implements TaskedModule {

    public void unload() {
        HandlerList.unregisterAll(this);
    }

    private WorldBorder border;
    private boolean alreadyCreated;

    private double x, z;
    private double size;
    private FilterModule when;
    private int after;
    private long duration;
    private double damage;
    private double buffer;
    private int warningDistance;
    private int warningTime;

    public WorldBorderModule(double x, double z, double size, FilterModule when, int after, long duration, double damage, double buffer, int warningDistance, int warningTime) {
        this.x = x;
        this.z = z;
        this.size = size;
        this.when = when;
        this.after = after;
        this.duration = duration;
        this.damage = damage;
        this.buffer = buffer;
        this.warningDistance = warningDistance;
        this.warningTime = warningTime;

        this.alreadyCreated = false;
    }

    public WorldBorder getBorder() { return border; }

    public double getX() { return x; }

    public double getZ() { return z; }

    public double getSize() { return size; }

    public FilterModule getWhen() { return when; }

    public int getAfter() { return after; }

    public long getDuration() { return duration; }

    public double getDamage() { return damage; }

    public double getBuffer() { return buffer; }

    public int getWarningDistance() { return warningDistance; }

    public int getWarningTime() { return warningTime; }

    public void run() {
        if (!alreadyCreated && (when != null ? getWhen().evaluate().equals(FilterState.ALLOW) : MatchTimer.getTimeInSeconds() >= getAfter())) {
            alreadyCreated = true;
            border = GameHandler.getGameHandler().getMatchWorld().getWorldBorder();
            border.setCenter(getX(), getZ());

            if (getDuration() > 0) {
                border.setSize(getSize(), getDuration());
            } else {
                border.setSize(getSize());
            }

            border.setDamageAmount(getDamage());
            border.setDamageBuffer(getBuffer());
            border.setWarningDistance(getWarningDistance());
            border.setWarningTime(getWarningTime());
        }
    }

}
