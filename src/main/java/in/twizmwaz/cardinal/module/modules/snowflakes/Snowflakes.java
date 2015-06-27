package in.twizmwaz.cardinal.module.modules.snowflakes;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.SnowflakeChangeEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.MiscUtils;
import in.twizmwaz.cardinal.util.NumUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDespawnInVoidEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Snowflakes implements Module {

    private HashMap<Player, List<Item>> items;
    private HashMap<Player, List<DyeColor>> destroyed;

    public Snowflakes() {
        this.items = new HashMap<>();
        this.destroyed = new HashMap<>();
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!event.isCancelled() && TeamUtils.getTeamByPlayer(event.getPlayer()) != null && event.getItemDrop().getItemStack().getType().equals(Material.WOOL)) {
            for (TeamModule team : TeamUtils.getTeams()) {
                if (!team.isObserver() && TeamUtils.getTeamByPlayer(event.getPlayer()) != team) {
                    for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                        if (obj instanceof WoolObjective && event.getItemDrop().getItemStack().getData().getData() == ((WoolObjective) obj).getColor().getData()) {
                            if (!items.containsKey(event.getPlayer())) {
                                items.put(event.getPlayer(), new ArrayList<Item>());
                            }
                            List<Item> list = items.get(event.getPlayer());
                            list.add(event.getItemDrop());
                            items.put(event.getPlayer(), list);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemDespawnInVoid(EntityDespawnInVoidEvent event) {
        if (event.getEntity() instanceof Item) {
            for (Player player : items.keySet()) {
                if (player != null && TeamUtils.getTeamByPlayer(player) != null) {
                    for (Item item : items.get(player)) {
                        if (item.equals(event.getEntity())) {
                            for (TeamModule team : TeamUtils.getTeams()) {
                                if (!team.isObserver() && TeamUtils.getTeamByPlayer(player) != team) {
                                    for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                                        if (obj instanceof WoolObjective && item.getItemStack().getData().getData() == ((WoolObjective) obj).getColor().getData() && (!destroyed.containsKey(player) || !destroyed.get(player).contains(((WoolObjective) obj).getColor()))) {
                                            if (!destroyed.containsKey(player)) {
                                                destroyed.put(player, new ArrayList<DyeColor>());
                                            }
                                            List<DyeColor> list = destroyed.get(player);
                                            list.add(((WoolObjective) obj).getColor());
                                            destroyed.put(player, list);

                                            Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(player, ChangeReason.DESTROY_WOOL, 8, MiscUtils.convertDyeColorToChatColor(((WoolObjective) obj).getColor()) + ((WoolObjective) obj).getColor().name().toUpperCase().replaceAll("_", " ") + " WOOL" + ChatColor.GRAY));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (event.getKiller() != null && TeamUtils.getTeamByPlayer(event.getPlayer()) != TeamUtils.getTeamByPlayer(event.getKiller())) {
            Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(event.getKiller(), ChangeReason.PLAYER_KILL, 1, event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (TeamUtils.getTeamByPlayer(player) != null && !TeamUtils.getTeamByPlayer(player).isObserver() && event.getTeam() == TeamUtils.getTeamByPlayer(player)) {
                Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(player, ChangeReason.TEAM_WIN, 15, TeamUtils.getTeamByPlayer(player).getCompleteName()));
            } else if (TeamUtils.getTeamByPlayer(player) != null && !TeamUtils.getTeamByPlayer(player).isObserver() && event.getTeam() != TeamUtils.getTeamByPlayer(player)) {
                Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(player, ChangeReason.TEAM_LOYAL, 5, TeamUtils.getTeamByPlayer(player).getCompleteName()));
            }
        }
    }

    @EventHandler
    public void onSnowflakeChange(SnowflakeChangeEvent event) {
        if (event.getFinalAmount() != 0) {
            String reason;
            if (event.getChangeReason().equals(ChangeReason.PLAYER_KILL)) {
                reason = "killed " + TeamUtils.getTeamColorByPlayer(Bukkit.getOfflinePlayer(event.get(0))) + event.get(0);
            } else if (event.getChangeReason().equals(ChangeReason.WOOL_TOUCH)) {
                reason = "picked up " + event.get(0);
            } else if (event.getChangeReason().equals(ChangeReason.WOOL_PLACE)) {
                reason = "placed " + event.get(0);
            } else if (event.getChangeReason().equals(ChangeReason.CORE_LEAK)) {
                reason = "you broke a piece of " + event.get(0);
            } else if (event.getChangeReason().equals(ChangeReason.MONUMENT_DESTROY)) {
                reason = "you destroyed " + event.get(0) + "% of " + event.get(1);
            } else if (event.getChangeReason().equals(ChangeReason.TEAM_WIN)) {
                reason = "your team (" + event.get(0) + ChatColor.GRAY + ") won";
            } else if (event.getChangeReason().equals(ChangeReason.TEAM_LOYAL)) {
                reason = "you were loyal to your team (" + event.get(0) + ChatColor.GRAY + ")";
            } else if (event.getChangeReason().equals(ChangeReason.DESTROY_WOOL)) {
                reason = "you destroyed " + event.get(0);
            } else {
                reason = "unknown reason";
            }
            event.getPlayer().sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "+" + event.getFinalAmount() + ChatColor.AQUA + " Snowflakes" + ChatColor.DARK_PURPLE + " | " + ChatColor.GOLD + "" + ChatColor.ITALIC + event.getMultiplier() + "x" + ChatColor.DARK_PURPLE + " | " + ChatColor.GRAY + reason).getMessage(event.getPlayer().getLocale()));
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1.5F);
            if (Cardinal.getCardinalDatabase().get(event.getPlayer(), "snowflakes").equals("")) {
                Cardinal.getCardinalDatabase().put(event.getPlayer(), "snowflakes", event.getFinalAmount() + "");
            } else {
                Cardinal.getCardinalDatabase().put(event.getPlayer(), "snowflakes", (NumUtils.parseInt(Cardinal.getCardinalDatabase().get(event.getPlayer(), "snowflakes")) + event.getFinalAmount()) + "");
            }
        }
    }

    public enum ChangeReason {
        PLAYER_KILL(), WOOL_TOUCH(), WOOL_PLACE(), CORE_LEAK(), MONUMENT_DESTROY(), TEAM_WIN(), TEAM_LOYAL(), DESTROY_WOOL()
    }

}
