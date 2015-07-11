package in.twizmwaz.cardinal.module.modules.team;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class TeamModule<P extends Player> extends ArrayList<Player> implements Module {

    private final Match match;
    private final String id;
    private final boolean observer;
    private String name;
    private int max;
    private int maxOverfill;
    private int respawnLimit;
    private ChatColor color;
    private boolean ready;

    protected TeamModule(Match match, String name, String id, int max, int maxOverfill, int respawnLimit, ChatColor color, boolean observer) {
        this.match = match;
        this.name = name;
        this.id = id;
        this.max = max;
        this.maxOverfill = maxOverfill;
        this.respawnLimit = respawnLimit;
        this.color = color;
        this.observer = observer;
        this.ready = false;
    }

    public boolean add(Player player, boolean force, boolean message) {
        TeamModule old = null;
        for (TeamModule team : match.getModules().getModules(TeamModule.class)) {
            if (team.contains(player)) {
                old = team;
                break;
            }
        }
        if (Blitz.matchIsBlitz() && GameHandler.getGameHandler().getMatch().isRunning() && !this.isObserver() && !force) {
            String title = GameHandler.getGameHandler().getMatch().getModules().getModule(Blitz.class).getTitle();
            player.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.ERROR_MAY_NOT_JOIN, ChatColor.ITALIC + "" + ChatColor.AQUA + title + ChatColor.RESET + ChatColor.RED)).getMessage(player.getLocale()));
            return false;
        }
        if (!force && size() >= maxOverfill) {
            player.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.ERROR_TEAM_FULL, getCompleteName() + ChatColor.RED)).getMessage(player.getLocale()));
            return false;
        }
        PlayerChangeTeamEvent event = new PlayerChangeTeamEvent(player, force, this, old);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (message) {
            event.getPlayer().sendMessage(new LocalizedChatMessage(ChatConstant.GENERIC_JOINED, event.getNewTeam().getCompleteName()).getMessage(event.getPlayer().getLocale()));
        }
        return !event.isCancelled() || force;
    }

    public boolean add(Player player, boolean force) {
        return this.add(player, force, true);
    }

    public boolean add(Player player) {
        return this.add(player, false);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onTeamSwitch(PlayerChangeTeamEvent event) {
        if (!event.isCancelled()) {
            this.remove(event.getPlayer());
        }
        if (event.getNewTeam() == this) {
            super.add(event.getPlayer());
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public String getCompleteName() {
        return this.color + this.name;
    }

    public Match getMatch() {
        return match;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMaxOverfill() {
        return maxOverfill;
    }

    public void setMaxOverfill(int maxOverfill) {
        this.maxOverfill = maxOverfill;
    }

    public int getRespawnLimit() {
        return respawnLimit;
    }

    public void setRespawnLimit(int respawnLimit) {
        this.respawnLimit = respawnLimit;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public boolean isObserver() {
        return observer;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

}