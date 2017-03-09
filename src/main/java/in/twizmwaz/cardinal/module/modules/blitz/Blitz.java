package in.twizmwaz.cardinal.module.modules.blitz;


import com.google.common.base.Optional;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.LazyMetadataValue;

@SuppressWarnings({"unchecked"})
public class Blitz implements Module {

    private String title;
    private boolean broadcastLives;
    private int lives;

    protected Blitz(final String title, final boolean broadcastLives, final int lives) {
        this.title = title;
        this.broadcastLives = broadcastLives;
        this.lives = lives;
    }

    public static boolean matchIsBlitz() {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(Blitz.class) != null;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Optional<TeamModule> team = Teams.getTeamOrPlayerByPlayer(player);
        if (team.isPresent() && !team.get().isObserver()) {
            int oldMeta = this.getLives(player);
            player.removeMetadata("lives", Cardinal.getInstance());
            player.setMetadata("lives", new LazyMetadataValue(Cardinal.getInstance(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new BlitzLives(oldMeta - 1)));
            if (this.getLives(player) == 0) {
                Teams.getTeamById("observers").get().add(player, true, false);
                player.removeMetadata("lives", Cardinal.getInstance());
            }
        }
    }

    @EventHandler
    public void onPgmSpawn(CardinalSpawnEvent event) {
        if (event.isCancelled()) return;
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            Player player = event.getPlayer();
            if (!event.getTeam().isObserver()) {
                if (!player.hasMetadata("lives")) {
                    player.setMetadata("lives", new LazyMetadataValue(Cardinal.getInstance(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new BlitzLives(this.lives)));
                }
                if (this.broadcastLives) {
                    int lives = this.getLives(player);
                    if (lives == 1) {
                        player.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.UI_AMOUNT_REMAINING, ChatColor.AQUA + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_ONE_LIFE).getMessage(player.getLocale()) + ChatColor.RED).getMessage(player.getLocale()));
                    } else {
                        player.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.UI_AMOUNT_REMAINING, ChatColor.AQUA + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_LIVES, lives + "").getMessage(player.getLocale()) + ChatColor.RED).getMessage(player.getLocale()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.removeMetadata("lives", Cardinal.getInstance());
        }
    }

    private int getLives(Player player) {
        return player.getMetadata("lives").get(0).asInt();
    }

    public static String getTitle() {
        Blitz blitz = GameHandler.getGameHandler().getMatch().getModules().getModule(Blitz.class);
        return blitz != null ? blitz.title : null;
    }


}
