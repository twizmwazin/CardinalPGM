package in.twizmwaz.cardinal.module.modules.snowflakes;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.SnowflakeChangeEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.PacketUtils;
import in.twizmwaz.cardinal.util.Teams;
import in.twizmwaz.cardinal.util.Watchers;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutEntityDestroy;
import net.minecraft.server.PacketPlayOutSpawnEntity;
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
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.Wool;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Snowflakes implements Module {

    private static String ITEM_THROWER_META = "item-thrower";

    private Map<Player, List<DyeColor>> destroyed = new HashMap<>();

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (testDestroy(event.getPlayer(), event.getItemDrop().getItemStack())) {
            event.getItemDrop().setMetadata(ITEM_THROWER_META,
                    new FixedMetadataValue(Cardinal.getInstance(), event.getPlayer().getUniqueId()));
        }
    }

    @EventHandler
    public void onItemDespawnInVoid(EntityDespawnInVoidEvent event) {
        if (!(event.getEntity() instanceof Item) || !event.getEntity().hasMetadata(ITEM_THROWER_META)) return;
        Player player = Bukkit.getPlayer((UUID) event.getEntity().getMetadata(ITEM_THROWER_META).get(0).value());
        Item item = (Item) event.getEntity();
        if (testDestroy(player, item.getItemStack())) {
            addDestroyed(player, ((Wool) item.getItemStack().getData()).getColor());
        }
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        for (DyeColor color : getColors(event.getRecipe())) {
            if (testDestroy(player, color)) {
                addDestroyed(player, color);
            }
        }
    }

    private boolean testDestroy(Player player, ItemStack item) {
        return item.getType().equals(Material.WOOL) && testDestroy(player, ((Wool) item.getData()).getColor());
    }

    private boolean testDestroy(Player player, DyeColor dye) {
        TeamModule team = Teams.getTeamByPlayer(player).orNull();
        return team != null && GameHandler.getGameHandler().getMatch().getModules().getModules(WoolObjective.class)
                .stream().anyMatch(wool -> wool.getTeam() != team && !wool.isComplete() && wool.getColor().equals(dye)
                        && !(destroyed.containsKey(player) && destroyed.get(player).contains(dye)));
    }

    private void addDestroyed(Player player, DyeColor dye) {
        if (!destroyed.containsKey(player)) destroyed.put(player, Lists.newArrayList());
        destroyed.get(player).add(dye);
        Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(player, ChangeReason.DESTROY_WOOL,
                8, MiscUtil.convertDyeColorToChatColor(dye) + dye.name().toUpperCase().replaceAll("_", " ") + " WOOL" + ChatColor.GRAY));
    }

    private List<DyeColor> getColors(Recipe recipe) {
        return recipe instanceof ShapedRecipe ? getColors(((ShapedRecipe) recipe).getIngredientMap().values())
                : recipe instanceof ShapelessRecipe ? getColors(((ShapelessRecipe) recipe).getIngredientList())
                : Lists.newArrayList();
    }

    private List<DyeColor> getColors(Collection<ItemStack> items) {
        return items.stream().filter(item -> item != null && item.getType().equals(Material.WOOL))
                .map(item -> ((Wool) item.getData()).getColor()).distinct().collect(Collectors.toList());
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (event.getKiller() != null && Teams.getTeamOrPlayerByPlayer(event.getPlayer()).orNull() != Teams.getTeamOrPlayerByPlayer(event.getKiller()).orNull()) {
            Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(event.getKiller(), ChangeReason.PLAYER_KILL, 1, event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Optional<TeamModule> team = Teams.getTeamOrPlayerByPlayer(player);
            if (!team.get().isObserver()) {
                if (event.getTeam().equals(team)) {
                    Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(player, ChangeReason.TEAM_WIN, 15, team.get().getCompleteName()));
                } else {
                    Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(player, ChangeReason.TEAM_LOYAL, 5, team.get().getCompleteName()));
                }
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
            if (Settings.getSettingByName("Snowflakes").getValueByPlayer(event.getPlayer()).getValue().equalsIgnoreCase("on"))
                spawnSnowflakes(event.getPlayer(), event.getFinalAmount());
        }
    }

    private void spawnSnowflakes(Player player, int count) {
        count = Math.min(12, Math.max(2, count));
        int[] entities = new int[count];
        List<Packet> packets = Lists.newArrayList();
        Vector loc = player.getLocation().position().plus(0, 2, 0);
        for (int i = 0; i < count; i++) {
            int id = Bukkit.allocateEntityId();
            entities[i] = id;
            int motX = (int) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 8000),
                    motY = (int) (0.2D * 8000),
                    motZ = (int) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 8000);
            packets.add(new PacketPlayOutSpawnEntity(
                    id, UUID.randomUUID(),             // Entity id and Entity UUID
                    loc.getX(), loc.getY(), loc.getZ(),// X, Y and Z Position
                    motX, motY, motZ,                  // X, Y and Z Motion
                    (byte) 2, (byte) 0,                // Pitch, Yaw
                    2, 0                               // Type and data
            ));
            packets.add(PacketUtils.createMetadataPacket(id, Watchers.toList(Watchers.SNOWFLAKE)));
        }
        for (Packet packet : packets) PacketUtils.sendPacket(player, packet);
        scheduleSnowflakeRemove(player, entities);
    }

    private void scheduleSnowflakeRemove(final Player player, final int... id) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(),
                () -> PacketUtils.sendPacket(player, new PacketPlayOutEntityDestroy(id)), 100L);
    }

    public enum ChangeReason {
        PLAYER_KILL(), WOOL_TOUCH(), WOOL_PLACE(), CORE_LEAK(), MONUMENT_DESTROY(), TEAM_WIN(), TEAM_LOYAL(), DESTROY_WOOL()
    }

}
