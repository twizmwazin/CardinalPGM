package in.twizmwaz.cardinal.teams;

import in.twizmwaz.cardinal.player.PgmPlayer;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

import java.util.List;

/**
 * Created by kevin on 11/17/14.
 */
public class PgmTeam {

    private Team scoreboardTeam;
    private String name;
    private String id;
    private int max;
    private int maxOverfill;
    private int respawnLimit;
    private ChatColor color;
    private List<PgmPlayer> pgmPlayers;

    PgmTeam(String name, String id, int max, int maxOverfill, int respawnLimit, ChatColor color) {
        //scoreboard team
        this.name = name;
        this.id = id;
        this.max = max;
        this.maxOverfill = maxOverfill;
        this.respawnLimit = respawnLimit;
        this.color = color;

    }

    public void add() {

    }


}
