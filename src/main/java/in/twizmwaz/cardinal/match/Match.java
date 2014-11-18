package in.twizmwaz.cardinal.match;


import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.data.MapInfo;
import in.twizmwaz.cardinal.module.ModuleContainer;
import in.twizmwaz.cardinal.util.Timer;
import in.twizmwaz.cardinal.util.XMLHandler;
import org.w3c.dom.Document;

import java.io.File;
import java.util.UUID;

public class Match {

    private GameHandler handler = GameHandler.getGameHandler();
    private UUID uuid;
    private MatchState state;
    private ModuleContainer modules;
    private Document document;
    private Thread timer;
    private MapInfo mapInfo;

    public Match() {
        this.uuid = handler.getMatchUUID();
        this.state = MatchState.WAITING;
        this.document = new XMLHandler(new File("matches/" + this.uuid.toString() + "/map.xml")).getDocument();
        this.modules = new ModuleContainer(this);
        this.mapInfo = new MapInfo(document);
        timer = new Thread(new Timer());
        //set timer value



    }

    public Match getMatch() {
        return this;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isRunning() {
        return state == MatchState.PLAYING;
    }

    public MatchState getState() {
        return state;
    }

    public void setState(MatchState state) {
        this.state = state;
    }

    public void start() {
        state = MatchState.PLAYING;
    }

    public void end() {
        state = MatchState.ENDED;
    }

    public ModuleContainer getModules() {
        return modules;
    }

    public MapInfo getMapInfo() {
        return mapInfo;
    }

}
