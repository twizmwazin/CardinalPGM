package in.twizmwaz.cardinal.cycle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;
import java.util.UUID;

/**
 * Created by kevin on 10/31/14.
 */
public class Cycle {

    public static World cycleWorld(String map, UUID uuid) {
        GenerateMap.copyWorldFromRepository(map, uuid);
        WorldCreator wc = new WorldCreator("matches/" + uuid.toString()).generator(new ChunkGenerator() {
            public byte[] generate(World world, Random random, int x, int z) {
                return new byte[65536];
            }
        });
        World world = Bukkit.createWorld(wc);
        world.setSpawnFlags(false, false);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(new Location(world, 0, 64, 0));
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }


        return world;
    }

}
