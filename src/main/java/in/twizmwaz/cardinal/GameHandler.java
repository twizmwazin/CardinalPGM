package in.twizmwaz.cardinal;


import in.twizmwaz.cardinal.cycle.Cycle;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.rotation.Rotation;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.util.UUID;

/**
 * Created by kevin on 10/31/14.
 */
public class GameHandler {

    private static GameHandler handler;

    private Rotation rotation;
    private UUID matchUUID;
    private World matchWorld;
    private Match match;

    public GameHandler() {
        handler = this;
        rotation = new Rotation(new File("rotation.txt"));
        initialCycle();
    }

    private void initialCycle() {
        matchUUID = UUID.randomUUID();
        matchWorld = Cycle.cycleWorld(rotation.getEntry(0), matchUUID);
        this.match = new Match();
    }

    public void cycleAndMakeMatch() {
        matchUUID = UUID.randomUUID();
        rotation.move();
        World oldMatchWorld = matchWorld;
        matchWorld = Cycle.cycleWorld(rotation.getCurrent(), matchUUID);
        Bukkit.unloadWorld(oldMatchWorld, true);
        this.match = new Match();
    }

    public static GameHandler getGameHandler() {
        return handler;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public UUID getMatchUUID() {
        return matchUUID;
    }

    public World getMatchWorld() {
        return matchWorld;
    }

    public Match getMatch() {
        return match;
    }

    public File getXML() {
        return new File("matches/" + getMatchUUID().toString() + "/map.xml");
    }
}
