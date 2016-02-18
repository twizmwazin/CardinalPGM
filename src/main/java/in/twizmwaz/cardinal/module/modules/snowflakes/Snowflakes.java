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
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Teams;
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
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

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
        if (!event.isCancelled() && Teams.getTeamByPlayer(event.getPlayer()) != null && event.getItemDrop().getItemStack().getType().equals(Material.WOOL)) {
            for (TeamModule team : Teams.getTeams()) {
                if (!team.isObserver() && Teams.getTeamByPlayer(event.getPlayer()).orNull() != team) {
                    for (GameObjective obj : Teams.getShownObjectives(team)) {
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
                if (player != null && Teams.getTeamByPlayer(player) != null) {
                    for (Item item : items.get(player)) {
                        if (item.equals(event.getEntity())) {
                            for (TeamModule team : Teams.getTeams()) {
                                if (!team.isObserver() && Teams.getTeamByPlayer(player).orNull() != team) {
                                    for (GameObjective obj : Teams.getShownObjectives(team)) {
                                        if (obj instanceof WoolObjective && item.getItemStack().getData().getData() == ((WoolObjective) obj).getColor().getData() && (!destroyed.containsKey(player) || !destroyed.get(player).contains(((WoolObjective) obj).getColor())) && !obj.isComplete()) {
                                            if (!destroyed.containsKey(player)) {
                                                destroyed.put(player, new ArrayList<DyeColor>());
                                            }
                                            List<DyeColor> list = destroyed.get(player);
                                            list.add(((WoolObjective) obj).getColor());
                                            destroyed.put(player, list);

                                            Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(player, ChangeReason.DESTROY_WOOL, 8, MiscUtil.convertDyeColorToChatColor(((WoolObjective) obj).getColor()) + ((WoolObjective) obj).getColor().name().toUpperCase().replaceAll("_", " ") + " WOOL" + ChatColor.GRAY));
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
    public void onPlayerCraft(CraftItemEvent event) {
        Player player = ((Player)event.getWhoClicked());
        List<ItemStack> destroyedWools = new ArrayList<>();
        if (event.getRecipe() instanceof ShapedRecipe) {
            for (ItemStack item : ((ShapedRecipe) event.getRecipe()).getIngredientMap().values()) {
                if (item != null && item.getType() != Material.AIR && item.getType() == Material.WOOL && !destroyedWools.contains(item)) {
                    destroyedWools.add(item);
                }
            }
        } else if (event.getRecipe() instanceof ShapelessRecipe) {
            for (ItemStack item : ((ShapelessRecipe) event.getRecipe()).getIngredientList()) {
                if (item.getType().equals(Material.WOOL) && !destroyedWools.contains(item)) {
                    destroyedWools.add(item);
                }
            }
        }
        if (!destroyedWools.isEmpty() && Teams.getTeamByPlayer(player) != null) {
            for (ItemStack item : destroyedWools) {
                for (TeamModule team : Teams.getTeams()) {
                    if (!team.isObserver() && Teams.getTeamByPlayer(player).orNull() != team) {
                        for (GameObjective obj : Teams.getShownObjectives(team)) {
                            if (obj instanceof WoolObjective && item.getData().getData() == ((WoolObjective) obj).getColor().getData() && (!destroyed.containsKey(player) || !destroyed.get(player).contains(((WoolObjective) obj).getColor())) && !obj.isComplete()) {
                                if (!destroyed.containsKey(player)) {
                                    destroyed.put(player, new ArrayList<DyeColor>());
                                }
                                List<DyeColor> list = destroyed.get(player);
                                list.add(((WoolObjective) obj).getColor());
                                destroyed.put(player, list);

                                Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(player, ChangeReason.DESTROY_WOOL, 8, MiscUtil.convertDyeColorToChatColor(((WoolObjective) obj).getColor()) + ((WoolObjective) obj).getColor().name().toUpperCase().replaceAll("_", " ") + " WOOL" + ChatColor.GRAY));
                            }
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (event.getKiller() != null && Teams.getTeamByPlayer(event.getPlayer()).orNull() != Teams.getTeamByPlayer(event.getKiller()).orNull()) {
            Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(event.getKiller(), ChangeReason.PLAYER_KILL, 1, event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Teams.getTeamByPlayer(player).isPresent() && !Teams.getTeamByPlayer(player).get().isObserver() && event.getTeam().equals(Teams.getTeamByPlayer(player))) {
                Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(player, ChangeReason.TEAM_WIN, 15, Teams.getTeamByPlayer(player).get().getCompleteName()));
            } else if (Teams.getTeamByPlayer(player).isPresent() && !Teams.getTeamByPlayer(player).get().isObserver() && !event.getTeam().equals(Teams.getTeamByPlayer(player))) {
                Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(player, ChangeReason.TEAM_LOYAL, 5, Teams.getTeamByPlayer(player).get().getCompleteName()));
            }
        }
    }

    @EventHandler
    public void onSnowflakeChange(SnowflakeChangeEvent event) {
        if (event.getFinalAmount() != 0) {
            String reason;
            if (event.getChangeReason().equals(ChangeReason.PLAYER_KILL)) {
                reason = "killed " + Teams.getTeamColorByPlayer(Bukkit.getOfflinePlayer(event.get(0))) + event.get(0);
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
            event.getPlayer().sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "+" + event.getFinalAmount() + ChatColor.WHITE + " Snowflakes" + ChatColor.DARK_PURPLE + " | " + ChatColor.GOLD + "" + ChatColor.ITALIC + event.getMultiplier() + "x" + ChatColor.DARK_PURPLE + " | " + ChatColor.GRAY + reason).getMessage(event.getPlayer().getLocale()));
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.5F);
            if (Settings.getSettingByName("Sounds") != null && Settings.getSettingByName("Sounds").getValueByPlayer(event.getPlayer()).getValue().equalsIgnoreCase("on")) {
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.5F);
            }
            if (Cardinal.getCardinalDatabase().get(event.getPlayer(), "snowflakes").equals("")) {
                Cardinal.getCardinalDatabase().put(event.getPlayer(), "snowflakes", event.getFinalAmount() + "");
            } else {
                Cardinal.getCardinalDatabase().put(event.getPlayer(), "snowflakes", (Numbers.parseInt(Cardinal.getCardinalDatabase().get(event.getPlayer(), "snowflakes")) + event.getFinalAmount()) + "");
            }
        }
    }

    public enum ChangeReason {
        PLAYER_KILL(), WOOL_TOUCH(), WOOL_PLACE(), CORE_LEAK(), MONUMENT_DESTROY(), TEAM_WIN(), TEAM_LOYAL(), DESTROY_WOOL()
    }

}
