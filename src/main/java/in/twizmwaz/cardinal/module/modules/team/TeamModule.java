package in.twizmwaz.cardinal.module.modules.team;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.tabList.TabList;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class TeamModule extends ArrayList<Player> implements Module {

    private final Match match;
    private final String id;
    private final boolean observer;
    private String name;
    private int min;
    private int max;
    private int maxOverfill;
    private ChatColor color;
    private boolean plural;
    private boolean ready;

    protected TeamModule(Match match, String name, String id, int min, int max, int maxOverfill, ChatColor color, boolean plural, boolean observer) {
        this.match = match;
        this.name = name;
        this.id = id;
        this.min = min;
        this.max = max;
        this.maxOverfill = maxOverfill;
        this.color = color;
        this.plural = plural;
        this.observer = observer;
        this.ready = false;
    }

    public boolean add(Player player, boolean force, boolean message) {
        if (Blitz.matchIsBlitz() && GameHandler.getGameHandler().getMatch().isRunning() && !this.isObserver() && !force) {
            player.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.ERROR_MAY_NOT_JOIN, ChatColor.ITALIC + "" + ChatColor.AQUA + "Blitz" + ChatColor.RESET + ChatColor.RED)).getMessage(player.getLocale()));
            return false;
        }
        if (!force && size() >= max) {
            player.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.ERROR_TEAM_FULL, getCompleteName() + ChatColor.RED)).getMessage(player.getLocale()));
            return false;
        }
        PlayerChangeTeamEvent event = new PlayerChangeTeamEvent(player, force, Optional.<TeamModule>of(this), Teams.getTeamOrPlayerManagerByPlayer(player));
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (message) {
            event.getPlayer().sendMessage(ChatColor.WHITE + new LocalizedChatMessage(ChatConstant.GENERIC_JOINED, event.getNewTeam().get().getCompleteName()).getMessage(event.getPlayer().getLocale()));
        }
        return !event.isCancelled() || force;
    }

    public boolean add(Player player, boolean force) {
        return this.add(player, force, true);
    }

    public boolean add(Player player) {
        return this.add(player, false);
    }

    public void join(Player player) {
        super.add(player);
    }

    public void leave(Player player) {
        super.remove(player);
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public Match getMatch() {
        return match;
    }

    public String getName() {
        return name;
    }

    public String getCompleteName() {
        return this.color + this.name;
    }

    public String getName(String locale) {
        return getName();
    }

    public String getCompleteName(String locale) {
        return getCompleteName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getMaxOverfill() {
        return maxOverfill;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
        TabList.renderTeamTitle(this);
    }

    public void setMaxOverfill(int maxOverfill) {
        this.maxOverfill = maxOverfill;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public boolean isPlural() {
        return plural;
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

    @Override
    public boolean equals(Object obj){
        return super.equals(obj) && obj instanceof TeamModule && ((TeamModule) obj).getId().equals(this.id);
    }

}
