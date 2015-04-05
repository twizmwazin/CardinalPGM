package in.twizmwaz.cardinal.module.modules.blitz;


import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.LazyMetadataValue;

public class Blitz implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }


    private String title = null;
    private boolean broadcastLives;
    private int lives;

    protected Blitz(final String title, final boolean broadcastLives, final int lives) {
        this.title = title;
        this.broadcastLives = broadcastLives;
        this.lives = lives;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        TeamModule team = TeamUtils.getTeamByPlayer(player);
        if (team != null && !team.isObserver()) {
            int oldMeta = this.getLives(player);
            player.removeMetadata("lives", Cardinal.getInstance());
            player.setMetadata("lives", new LazyMetadataValue(Cardinal.getInstance(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new BlitzLives(oldMeta - 1)));
            if (this.getLives(player) == 0) {
                TeamUtils.getTeamById("observers").add(player, true);
                player.removeMetadata("lives", Cardinal.getInstance());
            }
        }
    }

    @EventHandler
    public void onPgmSpawn(CardinalSpawnEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            Player player = event.getPlayer();
            if (TeamUtils.getTeamByPlayer(player) != null) {
                if (!TeamUtils.getTeamByPlayer(player).isObserver()) {
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
    }

    private int getLives(Player player){
        return player.getMetadata("lives").get(0).asInt();
    }

    public String getTitle(){
        return this.title;
    }

    public static boolean matchIsBlitz() {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(Blitz.class) != null;
    }


}
