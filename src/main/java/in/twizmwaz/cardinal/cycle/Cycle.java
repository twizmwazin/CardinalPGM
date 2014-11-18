package in.twizmwaz.cardinal.cycle;

import in.twizmwaz.cardinal.GameHandler;
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
public class Cycle implements Runnable {

    private String map;
    private final UUID uuid;
    private final GameHandler handler;

    public Cycle(String map, UUID uuid, GameHandler handler) {
        this.map = map;
        this.uuid = uuid;
        this.handler = handler;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getMap() {
        return map;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void run() {
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

        handler.setMatchWorld(world);
        handler.setMatchUUID(uuid);

    }

    @Deprecated
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
