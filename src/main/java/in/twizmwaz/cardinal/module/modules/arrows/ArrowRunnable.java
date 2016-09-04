package in.twizmwaz.cardinal.module.modules.arrows;

import in.twizmwaz.cardinal.util.MiscUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Arrow;

public class ArrowRunnable implements Runnable {

    private final Arrow arrow;
    private final float x;
    private final float y;
    private final float z;

    private Integer taskId = null;

    protected ArrowRunnable(Arrow arrow, ChatColor chatColor) {
        this.arrow = arrow;
        Color rgb = MiscUtil.convertChatColorToColor(chatColor);
        x = (float) rgb.getRed() / 255;
        y = (float) rgb.getGreen() / 255;
        z = (float) rgb.getBlue() / 255;
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
