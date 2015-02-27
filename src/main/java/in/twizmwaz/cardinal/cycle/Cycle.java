package in.twizmwaz.cardinal.cycle;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;
import java.util.UUID;

public class Cycle implements Runnable {

    private final UUID uuid;
    private final GameHandler handler;
    private LoadedMap map;

    public Cycle(LoadedMap map, UUID uuid, GameHandler handler) {
        this.map = map;
        this.uuid = uuid;
        this.handler = handler;
    }

    public LoadedMap getMap() {
        return map;
    }

    public void setMap(LoadedMap map) {
        this.map = map;
    }

    public UUID getUuid() {
        return uuid;
    }

    public GameHandler getGameHandler() {
        return handler;
    }

    @Override
    public void run() {
        GenerateMap.copyWorldFromRepository(map.getFolder(), uuid);
        World world = new WorldCreator("matches/" + uuid.toString()).generator(new NullChunkGenerator()).createWorld();
        handler.setMatchWorld(world);
    }
}
