package in.twizmwaz.cardinal.module.modules.worldBorder;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.io.File;

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
        if (!alreadyCreated) {
            boolean canCreatedBasedOnTime = false;
            if (getAfter() != 0) {
                if (MatchTimer.getTimeInSeconds() >= getAfter()) {
                    canCreatedBasedOnTime = true;
                }
            } else {
                if (MatchTimer.getTimeInSeconds() == 0) {
                    canCreatedBasedOnTime = true;
                }
            }

            boolean canCreatedBasedOnFilter = false;
            if (getWhen() != null) {
                if (getWhen().evaluate().equals(FilterState.ALLOW)) {
                    canCreatedBasedOnFilter = true;
                }
            } else canCreatedBasedOnFilter = true;

            if (canCreatedBasedOnTime && canCreatedBasedOnFilter) {
                alreadyCreated = true;
                border = GameHandler.getGameHandler().getMatchWorld().getWorldBorder();
                border.setCenter(getX(), getZ());

                if (getDuration() != 0) {
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

}
