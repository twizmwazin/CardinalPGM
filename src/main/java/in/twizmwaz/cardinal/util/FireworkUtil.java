package in.twizmwaz.cardinal.util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class FireworkUtil {

    public static void spawnFirework(Location location, World world) {
        Random random = new Random();
        int r = random.nextInt(256), g = random.nextInt(256), b = random.nextInt(256);
        spawnFirework(location, world, Color.fromRGB(r, g, b));
    }

    public static void spawnFirework(Location location, World world, Color color) {
        Firework firework = world.spawn(location, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().withColor(color).with(FireworkEffect.Type.BALL).trail(true).build();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
    }

}
