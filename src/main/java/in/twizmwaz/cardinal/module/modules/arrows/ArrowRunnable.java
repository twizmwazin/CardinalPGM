package in.twizmwaz.cardinal.module.modules.arrows;

import in.twizmwaz.cardinal.util.MiscUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Arrow;

public class ArrowRunnable implements Runnable {

    private static final float ZERO = 0.00001f;

    private final Arrow arrow;
    private final float x;
    private final float y;
    private final float z;

    private Integer taskId = null;

    protected ArrowRunnable(Arrow arrow, ChatColor chatColor) {
        this.arrow = arrow;
        Color rgb = MiscUtil.convertChatColorToColor(chatColor);
        x = rgbToFloat(rgb.getRed());
        y = rgbToFloat(rgb.getGreen());
        z = rgbToFloat(rgb.getBlue());
    }

    private static float rgbToFloat(int i) {
        if (i == 0) {
            return ZERO;
        }
        return (float) i / 255;
    }

    public void setTask(int id) {
        this.taskId = id;
    }

    @Override
    public void run() {
        if (arrow.isOnGround() || arrow.isDead()) {
            ArrowModule.arrowOnGround(arrow);
            Bukkit.getScheduler().cancelTask(taskId);
        } else {
            ArrowModule.sendArrowParticle(arrow, x, y, z);
        }
    }

}
