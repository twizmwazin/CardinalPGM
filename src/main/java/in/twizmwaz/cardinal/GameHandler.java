package in.twizmwaz.cardinal;


import in.twizmwaz.cardinal.cycle.Cycle;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.rotation.Rotation;
import in.twizmwaz.cardinal.rotation.exception.RotationLoadException;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by kevin on 10/31/14.
 */
public class GameHandler {

    private static GameHandler handler;

    private Rotation rotation;
    private UUID matchUUID;
    private World matchWorld;
    private Match match;
    private Cycle cycle;

    public GameHandler() {
        handler = this;
        rotation = new Rotation(new File("rotation.txt"));
        initialCycle();
    }

    public static GameHandler getGameHandler() {
        return handler;
    }

    private void initialCycle() {
        try {
            this.cycle = new Cycle(rotation.getEntry(0), UUID.randomUUID(), this);
            this.cycle.run();
            rotation.move();
            this.match = new Match();
        } catch (RotationLoadException ex) {
            Bukkit.getLogger().log(Level.WARNING, ex.getMessage());
        }
        cycle = new Cycle(rotation.getNext(), UUID.randomUUID(), this);

    }

    public void cycleAndMakeMatch() {
        matchUUID = UUID.randomUUID();
        rotation.move();
        World oldMatchWorld = matchWorld;
        cycle.run();
        this.match = new Match();
        Bukkit.unloadWorld(oldMatchWorld, true);
        cycle = new Cycle(rotation.getNext(), UUID.randomUUID(), this);
    }

    public Rotation getRotation() {
        return rotation;
    }

    public UUID getMatchUUID() {
        return matchUUID;
    }

    public void setMatchUUID(UUID uuid) {
        this.matchUUID = uuid;
    }

    public World getMatchWorld() {
        return matchWorld;
    }

    public void setMatchWorld(World world) {
        this.matchWorld = world;
    }

    public Match getMatch() {
        return match;
    }

    public File getXML() {
        return new File("matches/" + getMatchUUID().toString() + "/map.xml");
    }

    public Cycle getCycle() {
        return cycle;
    }
}
