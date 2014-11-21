package in.twizmwaz.cardinal.player;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.GameHandler;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 11/18/14.
 */
public class PgmPlayer {

    private static List<PgmPlayer> players = new ArrayList<PgmPlayer>();

    private Player player;

    public PgmPlayer(Player player) {
        this.player = player;
        players.add(this);
    }

    public static PgmPlayer getPgmPlayer(Player player) {
        for (PgmPlayer play : players) {
            if (play.getPlayer().equals(player)) return play;
        }
        return null;
    }

    public void remove() {
        players.remove(this);
    }

    public Player getPlayer() {
        return player;
    }

    public String getCompleteName() {
        try {
            return (GameHandler.getGameHandler().getMatch().getTeam(this.getPlayer()).getColor() + player.getName());
        } catch (NullPointerException ex) {
            return ChatColor.DARK_AQUA + player.getName();
        }
    }


}
