package in.twizmwaz.cardinal.module.modules.respawn;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.classModule.ClassModule;
import in.twizmwaz.cardinal.module.modules.spawn.SpawnModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.tutorial.Tutorial;
import in.twizmwaz.cardinal.util.Items;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInitialSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.Collections;

public class RespawnModule implements Module {

    private final Match match;

    protected RespawnModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onCardinalSpawn(CardinalSpawnEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (((team.isPresent() && !team.get().isObserver()) || !team.isPresent()) && match.isRunning()) {
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
        event.getPlayer().updateInventory();

    }

    @EventHandler(priority = EventPriority.LOW)
    public void clearIgnorantEffects(CardinalSpawnEvent event) {
        event.getPlayer().setPotionParticles(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInitLogin(PlayerInitialSpawnEvent event) {
        Optional<TeamModule> teamModule = Teams.getTeamById("observers");
        ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
        for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
            if (spawnModule.getTeam() == teamModule.orNull()) modules.add(spawnModule);
        }
        SpawnModule chosen = modules.getRandom();
        event.setSpawnLocation(chosen.getLocation());
        event.getPlayer().setMetadata("initSpawn", new FixedMetadataValue(Cardinal.getInstance(), chosen));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Players.resetPlayer(event.getPlayer());
        CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), (SpawnModule) event.getPlayer().getMetadata("initSpawn").get(0).value(), Teams.getTeamById("observers").orNull());
        Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
    }

    @EventHandler
    public void onMinecraftRespawn(PlayerRespawnEvent event) {
        if (match.getState().equals(MatchState.PLAYING)) {
            Optional<TeamModule> teamModule = Teams.getTeamByPlayer(event.getPlayer());
            ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
            for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                if (spawnModule.getTeam() == teamModule.orNull()) modules.add(spawnModule);
            }
            SpawnModule chosen = modules.getRandom();
            CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), chosen, Teams.getTeamByPlayer(event.getPlayer()).orNull());
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                event.setRespawnLocation(chosen.getLocation());
            }
        } else {
            Optional<TeamModule> teamModule = Teams.getTeamById("observers");
            ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
            for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                if (spawnModule.getTeam() == teamModule.orNull()) modules.add(spawnModule);
            }
            SpawnModule chosen = modules.getRandom();
            CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), chosen, Teams.getTeamById("observers").get());
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                Player player = event.getPlayer();
                event.setRespawnLocation(chosen.getLocation());
                Players.resetPlayer(player);
                giveObserversKit(player);
                player.teleport(chosen.getLocation());
            }
        }
    }


    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Optional<TeamModule> team = Teams.getTeamByPlayer(player);
            if (!team.isPresent() || !team.get().isObserver()) {
                Players.resetPlayer(player);
                ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
                for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                    if (spawnModule.getTeam() == team.orNull()) modules.add(spawnModule);
                }
                SpawnModule chosen = modules.getRandom();
                CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(player, chosen, Teams.getTeamByPlayer(player).orNull());
                Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                if (!spawnEvent.isCancelled()) {
                    player.teleport(chosen.getLocation());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Optional<TeamModule> teamModule = Teams.getTeamByPlayer(player);
            ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
            for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                if (spawnModule.getTeam() == teamModule.orNull()) modules.add(spawnModule);
            }
            SpawnModule chosen = modules.getRandom();
            CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(player, chosen, Teams.getTeamById("observers").get());
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                Players.resetPlayer(player);
                giveObserversKit(player);
                player.teleport(chosen.getLocation());
            }
        }
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        if (event.getOldTeam() == null) {
            event.getPlayer().setMaxHealth(20);
            Players.resetPlayer(event.getPlayer());
            Optional<TeamModule> teamModule = event.getNewTeam();
            ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
            for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                if (spawnModule.getTeam() == teamModule.orNull()) modules.add(spawnModule);
            }
            SpawnModule chosen = modules.getRandom();
            CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), chosen, event.getNewTeam().orNull());
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                event.getPlayer().teleport(chosen.getLocation());
            }
        } else if (match.getState().equals(MatchState.PLAYING)) {
            if (!event.getNewTeam().isPresent() || !event.getNewTeam().get().isObserver()) {
                if (event.getOldTeam().isPresent() && event.getOldTeam().get().isObserver()) {
                    event.getPlayer().setMaxHealth(20);
                    Players.resetPlayer(event.getPlayer());
                    Optional<TeamModule> teamModule = event.getNewTeam();
                    ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
                    for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                        if (spawnModule.getTeam() == teamModule.orNull()) modules.add(spawnModule);
                    }
                    SpawnModule chosen = modules.getRandom();
                    CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), chosen, event.getNewTeam().orNull());
                    Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                    if (!spawnEvent.isCancelled()) {
                        event.getPlayer().teleport(chosen.getLocation());
                    }
                } else {
                    event.getPlayer().setMetadata("teamChange", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), "teamChange"));
                    event.getPlayer().setHealth(0);
                }
            } else {
                Optional<TeamModule> teamModule = event.getNewTeam();
                SpawnModule spawn = null;
                for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                    if (spawnModule.getTeam() == teamModule.orNull()) spawn = spawnModule;
                }
                CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), spawn, event.getNewTeam().orNull());
                Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                if (!spawnEvent.isCancelled()) {
                    event.getPlayer().setMetadata("teamChange", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), "teamChange"));
                    event.getPlayer().setHealth(0);
                }
            }

        }

    }

    public void giveObserversKit(Player player) {
        player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
        player.getInventory().setItem(1, Items.createBook(Material.WRITTEN_BOOK, 1, ChatColor.AQUA.toString() + ChatColor.BOLD + "Coming Soon", ChatColor.GOLD + "CardinalPGM"));
        if (!GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
            player.getInventory().setItem(2, Items.createItem(Material.LEATHER_HELMET, 1, (short) 0,
                    ChatColor.GREEN + "" + ChatColor.BOLD + (GameHandler.getGameHandler().getMatch().getModules().getModule(ClassModule.class) != null ? ChatConstant.UI_TEAM_CLASS_SELECTION.getMessage(player.getLocale()) : ChatConstant.UI_TEAM_SELECTION.getMessage(player.getLocale())),
                    Collections.singletonList(ChatColor.DARK_PURPLE + ChatConstant.UI_TEAM_JOIN_TIP.getMessage(player.getLocale()))));
        }
        player.getInventory().setItem(3, Tutorial.getEmerald(player));
        if (player.hasPermission("tnt.defuse")) {
            player.getInventory().setItem(5, Items.createItem(Material.SHEARS, 1, (short) 0, ChatColor.RED + ChatConstant.UI_TNT_DEFUSER.getMessage(player.getLocale())));
        }
        player.getInventory().setItem(8, Items.createItem(Material.DIAMOND, 1, (short) 0, ChatColor.AQUA + "" + ChatColor.BOLD + ChatConstant.UI_TOGGLE_SPECTATOR_MODE.getMessage(player.getLocale()), Arrays.asList(ChatColor.BLUE + ChatConstant.UI_LEFT_CLICK.getMessage(player.getLocale()))));
    }

}
