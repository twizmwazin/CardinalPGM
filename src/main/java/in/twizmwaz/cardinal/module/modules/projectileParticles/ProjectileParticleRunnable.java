package in.twizmwaz.cardinal.module.modules.projectileParticles;

import in.twizmwaz.cardinal.util.MiscUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Projectile;

public class ProjectileParticleRunnable implements Runnable {

    private static final float ZERO = 0.00001f;

    private final Projectile projectile;
    private final float x;
    private final float y;
    private final float z;

    private int taskId = -1;

    protected ProjectileParticleRunnable(Projectile projectile, ChatColor chatColor) {
        this.projectile = projectile;
        Color rgb = MiscUtil.convertChatColorToColor(chatColor);
        x = rgbToFloat(rgb.getRed());
        y = rgbToFloat(rgb.getGreen());
        z = rgbToFloat(rgb.getBlue());
    }

    private static float rgbToFloat(int i) {
        return Math.max((float) i / 255, ZERO);
    }

    public void setTask(int id) {
        this.taskId = id;
    }

    @Override
    public void run() {
        if (taskId == -1) return;
        if (projectile.isOnGround() || projectile.isDead()) {
            Bukkit.getScheduler().cancelTask(taskId);
        } else {
            ProjectileParticlesModule.sendArrowParticle(projectile, x, y, z);
        }
    }

}
