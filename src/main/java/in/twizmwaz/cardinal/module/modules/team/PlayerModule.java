package in.twizmwaz.cardinal.module.modules.team;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerModule extends TeamModule {

    private Player player;

    PlayerModule(Match match, Player player, ChatColor color) {
        super(match, player.getPlayerListName(), UUID.randomUUID().toString().substring(0, 14), 1, 1, 1, color, false, false);
        this.player = player;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public String getCompleteName() {
        return player.getPlayerListName();
    }

}
