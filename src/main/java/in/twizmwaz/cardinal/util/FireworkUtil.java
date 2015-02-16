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
        Firework firework = (Firework) world.spawn(location, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        Random random = new Random();
        int randomColor = random.nextInt(17);
        FireworkEffect effect = FireworkEffect.builder().withColor(randomColor(randomColor)).with(FireworkEffect.Type.BALL).trail(true).build();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(0);
        firework.setFireworkMeta(fireworkMeta);
    }

    public static void spawnFirework(Location location, World world, Color color) {
        Firework firework = (Firework) world.spawn(location, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().withColor(color).with(FireworkEffect.Type.BALL).trail(true).build();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(0);
        firework.setFireworkMeta(fireworkMeta);
    }

    private static Color randomColor(int c) {
        switch (c) {
            default:
            case 1: return Color.AQUA;
            case 2: return Color.BLACK;
            case 3: return Color.BLUE;
            case 4: return Color.FUCHSIA;
            case 5: return Color.GRAY;
            case 6: return Color.GREEN;
            case 7: return Color.LIME;
            case 8: return Color.MAROON;
            case 9: return Color.NAVY;
            case 10: return Color.OLIVE;
            case 11: return Color.ORANGE;
            case 12: return Color.PURPLE;
            case 13: return Color.RED;
            case 14: return Color.SILVER;
            case 15: return Color.TEAL;
            case 16: return Color.WHITE;
            case 17: return Color.YELLOW;
        }
    }


}
