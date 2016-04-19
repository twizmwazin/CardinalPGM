package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.GameHandler;
import net.minecraft.server.EntityFireworks;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftFirework;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class Fireworks {

    public static FireworkEffect getFireworkEffect(Color color) {
        return FireworkEffect.builder().with(FireworkEffect.Type.BURST).flicker(true).withColor(color).withFade(Color.BLACK).build();
    }

    public static void spawnFireworks(Vector vec, double radius, int count, Color color, int power) {
        Location loc = vec.toLocation(GameHandler.getGameHandler().getMatchWorld());
        FireworkEffect effect = getFireworkEffect(color);
        for(int i = 0; i < count; i++) {
            double angle = (2 * Math.PI / count) * i;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            spawnFirework(firstEmptyBlock(loc.clone().add(x, 0, z)), effect, power);
        }
    }

    public static Firework spawnFirework(Location loc, FireworkEffect effect, int power) {
        Firework firework = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(power);
        firework.setFireworkMeta(fireworkMeta);
        return firework;
    }

    public static void spawnFlagFirework(Location location, Color color) {
        FireworkEffect effect = FireworkEffect.builder().withColor(color).with(FireworkEffect.Type.BURST).trail(true).build();
        Firework firework = spawnFirework(location, effect, 1);
        explodeFirework(firework);
    }

    public static void explodeFirework(Firework firework) {
        EntityFireworks nmsFirework = ((CraftFirework)firework).getHandle();
        nmsFirework.ticksFlown = 1;
        nmsFirework.expectedLifespan = 2;
    }

    private static Location firstEmptyBlock(Location loc) {
        loc = loc.clone();
        while (true) {
            if (loc.getBlock() == null || loc.getY() == 256 || !loc.getBlock().getType().isOccluding()) return loc;
            loc.add(0, 1, 0);
        }
    }

}
