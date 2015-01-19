package in.twizmwaz.cardinal.module.modules.destroyable;

import in.twizmwaz.cardinal.chat.TeamChat;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.gameScoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;

public class DestroyableObjective implements GameObjective {

    private final TeamModule team;
    private final String name;
    private final String id;
    private final Region region;
    private final List<Material> types;
    private final List<Integer> damageValues;
    private final double required;
    private final boolean showPercent;
    private final boolean repairable;
    private final boolean show;

    private Set<UUID> playersTouched;
    private double size;
    private HashMap<UUID, Integer> playerDestroyed;

    private double complete;
    private boolean completed;

    private GameObjectiveScoreboardHandler scoreboardHandler;

    protected DestroyableObjective(final TeamModule team, final String name, final String id, final Region region, final List<Material> types, final List<Integer> damageValues, final double required, final boolean show, boolean showPercent, boolean repairable) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.region = region;
        this.types = types;
        this.damageValues = damageValues;
        this.showPercent = showPercent;
        this.repairable = repairable;
        this.complete = 0;
        this.required = required;
        this.show = show;
        this.completed = false;

        this.playersTouched = new HashSet<>();
        this.playerDestroyed = new HashMap<>();

        size = 0.0;
        for (Block block : region.getBlocks()) {
            for (int i = 0; i < types.size(); i++) {
                if (types.get(i).equals(block.getType()) && damageValues.get(i) == (int) block.getState().getData().getData()) {
                    size ++;
                    break;
                }
            }
        }

        this.scoreboardHandler = new GameObjectiveScoreboardHandler(this);
    }

    @Override
    public TeamModule getTeam() {
        return this.team;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isTouched() {
        return this.complete > 0;
    }

    @Override
    public boolean isComplete() {
        return this.completed;
    }

    @Override
    public boolean showOnScoreboard() {
        return show;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public GameObjectiveScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (getBlocks().contains(event.getBlock())) {
            if (TeamUtils.getTeamByPlayer(event.getPlayer()) != team) {
                if (!playersTouched.contains(event.getPlayer().getUniqueId())) {
                    playersTouched.add(event.getPlayer().getUniqueId());
                    TeamChat.sendToTeam(team.getColor() + "[Team] " + event.getPlayer().getDisplayName() + ChatColor.GRAY + " destroyed some of " + ChatColor.AQUA + name, TeamUtils.getTeamByPlayer(event.getPlayer()));
                }
                boolean oldState = this.isTouched();
                this.complete ++;
                this.playerDestroyed.put(event.getPlayer().getUniqueId(), (playerDestroyed.containsKey(event.getPlayer().getUniqueId()) ? playerDestroyed.get(event.getPlayer().getUniqueId()) + 1 : 1));
                if ((this.complete / size) >= this.required && !this.completed) {
                    this.completed = true;
                    event.setCancelled(false);
                    Bukkit.broadcastMessage(team.getCompleteName() + "'s " + ChatColor.AQUA + name + ChatColor.GRAY + " destroyed by " + getWhoDestroyed());
                    ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, event.getPlayer());
                    Bukkit.getServer().getPluginManager().callEvent(compEvent);
                } else if (!this.completed) {
                    ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, event.getPlayer(), !oldState || showPercent);
                    Bukkit.getServer().getPluginManager().callEvent(touchEvent);
                }
            } else {
                event.setCancelled(true);
                ChatUtils.sendWarningMessage(event.getPlayer(), "You may not damage your own objective.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        List<Block> objectiveBlownUp = new ArrayList<>();
        for (Block block : event.blockList()) {
            if (getBlocks().contains(block)) {
                objectiveBlownUp.add(block);
            }
        }
        boolean oldState = this.isTouched();
        boolean blownUp = false;
        Player eventPlayer = null;
        int originalPercent = getPercent();
        for (Block block : objectiveBlownUp) {
            boolean blockDestroyed = false;
            if (TntTracker.getWhoPlaced(event.getEntity()) != null) {
                UUID player = TntTracker.getWhoPlaced(event.getEntity());
                if (Bukkit.getOfflinePlayer(player).isOnline()) {
                    if (TeamUtils.getTeamByPlayer(Bukkit.getPlayer(player)) == team) {
                        event.blockList().remove(block);
                    } else {
                        if (!playersTouched.contains(player)) {
                            playersTouched.add(player);
                            TeamChat.sendToTeam(team.getColor() + "[Team] " + Bukkit.getPlayer(player).getDisplayName() + ChatColor.GRAY + " destroyed some of " + ChatColor.AQUA + name, TeamUtils.getTeamByPlayer(Bukkit.getPlayer(player)));
                        }
                        blockDestroyed = true;
                        blownUp = true;
                        eventPlayer = Bukkit.getPlayer(player);
                    }
                } else {
                    if (!playersTouched.contains(player)) {
                        playersTouched.add(player);
                    }
                    blockDestroyed = true;
                    blownUp = true;
                }
            } else {
                blockDestroyed = true;
                blownUp = true;
            }
            if (blockDestroyed) {
                this.complete ++;
                if (eventPlayer != null) this.playerDestroyed.put(eventPlayer.getUniqueId(), (playerDestroyed.containsKey(eventPlayer.getUniqueId()) ? playerDestroyed.get(eventPlayer.getUniqueId()) + 1 : 1));
                if ((this.complete / size) >= this.required && !this.completed) {
                    this.completed = true;
                    Bukkit.broadcastMessage(team.getCompleteName() + ChatColor.GRAY + "'s " + ChatColor.AQUA + name + ChatColor.GRAY + " destroyed by " + getWhoDestroyed());
                    ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, eventPlayer);
                    Bukkit.getServer().getPluginManager().callEvent(compEvent);
                }
            }
        }
        if (!this.completed && blownUp) {
            ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, eventPlayer, !oldState || (getPercent() != originalPercent));
            Bukkit.getServer().getPluginManager().callEvent(touchEvent);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        while (playersTouched.contains(event.getEntity().getUniqueId())) {
            playersTouched.remove(event.getEntity().getUniqueId());
        }
    }

    public double getMonumentSize() {
        return size;
    }

    public boolean showPercent() {
        return showPercent;
    }

    public boolean isRepairable() {
        return repairable;
    }

    public int getPercent() {
        double blocksRequired = required * getMonumentSize();
        if (Math.floor((complete / blocksRequired) * 100) > 100) {
            return 100;
        }
        if (Math.floor((complete / blocksRequired) * 100) < 0) {
            return 0;
        }
        return (int) Math.floor((complete / blocksRequired) * 100);
    }

    public boolean partOfObjective(Block block) {
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).equals(block.getType()) && damageValues.get(i) == (int) block.getState().getData().getData()) {
                return true;
            }
        }
        return false;
    }

    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();
        for (Block block : region.getBlocks()) {
            if (partOfObjective(block)) {
                blocks.add(block);
            }
        }
        return blocks;
    }

    public String getWhoDestroyed() {
        String whoDestroyed = "";
        List<String> toCombine = new ArrayList<>();
        for (UUID player : getSortedHashMapKeyset(playerDestroyed)) {
            if (Bukkit.getOfflinePlayer(player).isOnline() && playerDestroyed.get(player) > (1 / 3)) {
                toCombine.add(TeamUtils.getTeamByPlayer(Bukkit.getPlayer(player)).getColor() + Bukkit.getPlayer(player).getDisplayName() + ChatColor.GRAY + " (" + (int) Math.floor((playerDestroyed.get(player) / size) * 100) + "%)");
            }
        }
        if (playerDestroyed.size() > 2 || toCombine.size() == 0) {
            toCombine.add(ChatColor.DARK_AQUA + "the enemy");
        }
        whoDestroyed = toCombine.get(0);
        for (int i = 1; i < toCombine.size(); i ++) {
            whoDestroyed += ChatColor.GRAY + (i == toCombine.size() - 1 ? " and " : ", ") + toCombine.get(i);
        }
        return whoDestroyed;
    }

    public <T> List<T> getSortedHashMapKeyset(HashMap<T, Integer> sorting) {
        List<T> types = new ArrayList<>();
        HashMap<T, Integer> clone = new HashMap<>();
        for (T player : sorting.keySet()) {
            clone.put(player, sorting.get(player));
        }
        for (int i = 0; i < sorting.size(); i ++) {
            int highestNumber = -1;
            T highestType = null;
            for (T player : clone.keySet()) {
                if (clone.get(player) > highestNumber) {
                    highestNumber = clone.get(player);
                    highestType = player;
                }
            }
            clone.remove(highestType);
            types.add(highestType);
        }
        return types;
    }
}
