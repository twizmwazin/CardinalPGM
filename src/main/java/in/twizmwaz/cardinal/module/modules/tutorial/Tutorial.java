package in.twizmwaz.cardinal.module.modules.tutorial;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.PlayerUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tutorial implements Module {

    private Stage prefix;
    private List<Stage> stages;
    private Stage suffix;

    private List<Stage> allStages = new ArrayList<>();

    private Map<Player, DisplayHandler> displayHandlerMap = new HashMap<>();

    public Stage getPrefix() {
        return prefix;
    }

    public List<Stage> getStages() {
        return this.stages;
    }

    public List<Stage> getAllStages() {
        return this.allStages;
    }

    public Stage getSuffix() {
        return suffix;
    }

    public Tutorial(Stage prefix, List<Stage> stages, Stage suffix) {
        this.prefix = prefix;
        this.stages = stages;
        this.suffix = suffix;

        this.allStages.add(prefix);
        this.allStages.addAll(stages);
        this.allStages.add(suffix);
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        boolean condition = TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver() && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().equals(Material.EMERALD);

        if (!this.displayHandlerMap.containsKey(event.getPlayer())) {
            this.displayHandlerMap.put(event.getPlayer(), new DisplayHandler(event.getPlayer(), this));
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR && condition) {
            this.displayHandlerMap.get(event.getPlayer()).displayNext();
        }
        if (event.getAction() == Action.LEFT_CLICK_AIR && condition) {
            this.displayHandlerMap.get(event.getPlayer()).displayPrev();
        }
    }

    @EventHandler
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (event.getNewTeam().isObserver()) {
            this.displayHandlerMap.remove(event.getPlayer());
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public static ItemStack getEmerald(Player player) {
        if (GameHandler.getGameHandler().getMatch().getModules().getModules(Tutorial.class).size() > 0) {
            ItemStack emerald = new ItemStack(Material.EMERALD);
            ItemMeta meta = emerald.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + new LocalizedChatMessage(ChatConstant.UI_TUTORIAL_VIEW).getMessage(ChatUtils.getLocale(player)));
            emerald.setItemMeta(meta);
            return emerald;
        }
        return new ItemStack(Material.AIR);
    }
}
