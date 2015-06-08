package in.twizmwaz.cardinal.module.modules.respawn;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
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
import in.twizmwaz.cardinal.util.ItemUtils;
import in.twizmwaz.cardinal.util.PlayerUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInitialSpawnEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

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
    public void onPgmSpawn(CardinalSpawnEvent event) {
        try {
            if (!TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver() && match.isRunning()) {
                event.getPlayer().setGameMode(GameMode.SURVIVAL);
            }
            event.getPlayer().updateInventory();
        } catch (NullPointerException e) {
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void clearIgnorantEffects(CardinalSpawnEvent event) {
        event.getPlayer().setPotionParticles(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInitLogin(PlayerInitialSpawnEvent event) {
        TeamModule teamModule = TeamUtils.getTeamById("observers");
        ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
        for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
            if (spawnModule.getTeam() == teamModule) modules.add(spawnModule);
        }
        SpawnModule chosen = modules.getRandom();
        CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), chosen, TeamUtils.getTeamById("observers"));
        Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
        if (!spawnEvent.isCancelled()) {
            event.setSpawnLocation(chosen.getLocation());
            PlayerUtils.resetPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onMinecraftRespawn(PlayerRespawnEvent event) {
        if (match.getState().equals(MatchState.PLAYING)) {
            TeamModule teamModule = TeamUtils.getTeamByPlayer(event.getPlayer());
            ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
            for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                if (spawnModule.getTeam() == teamModule) modules.add(spawnModule);
            }
            SpawnModule chosen = modules.getRandom();
            CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), chosen, TeamUtils.getTeamByPlayer(event.getPlayer()));
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                event.setRespawnLocation(chosen.getLocation());
            }
        } else {
            TeamModule teamModule = TeamUtils.getTeamById("observers");
            ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
            for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                if (spawnModule.getTeam() == teamModule) modules.add(spawnModule);
            }
            SpawnModule chosen = modules.getRandom();
            CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), chosen, TeamUtils.getTeamById("observers"));
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                Player player = event.getPlayer();
                event.setRespawnLocation(chosen.getLocation());
                PlayerUtils.resetPlayer(player);
                player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
                ItemStack howTo = ItemUtils.createBook(Material.WRITTEN_BOOK, 1, ChatColor.AQUA.toString() + ChatColor.BOLD + "Coming Soon", ChatColor.GOLD + "CardinalPGM");
                player.getInventory().setItem(1, howTo);
                if (!GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
                    ItemStack picker = ItemUtils.createItem(Material.LEATHER_HELMET, 1, (short) 0,
                            ChatColor.GREEN + "" + ChatColor.BOLD + (GameHandler.getGameHandler().getMatch().getModules().getModule(ClassModule.class) != null ? new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(player.getLocale()) : new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(player.getLocale())),
                            Arrays.asList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_TIP).getMessage(player.getLocale())));
                    player.getInventory().setItem(2, picker);
                }
                if (player.hasPermission("tnt.defuse")) {
                    ItemStack shears = ItemUtils.createItem(Material.SHEARS, 1, (short) 0, ChatColor.RED + new LocalizedChatMessage(ChatConstant.UI_TNT_DEFUSER).getMessage(player.getLocale()));
                    player.getInventory().setItem(5, shears);
                }
                player.teleport(chosen.getLocation());
            }
        }
    }


    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!TeamUtils.getTeamByPlayer(player).isObserver()) {
                PlayerUtils.resetPlayer(player);
                TeamModule teamModule = TeamUtils.getTeamByPlayer(player);
                ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
                for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                    if (spawnModule.getTeam() == teamModule) modules.add(spawnModule);
                }
                SpawnModule chosen = modules.getRandom();
                CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(player, chosen, TeamUtils.getTeamByPlayer(player));
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
            TeamModule teamModule = TeamUtils.getTeamByPlayer(player);
            ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
            for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                if (spawnModule.getTeam() == teamModule) modules.add(spawnModule);
            }
            SpawnModule chosen = modules.getRandom();
            CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(player, chosen, TeamUtils.getTeamById("observers"));
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                PlayerUtils.resetPlayer(player);
                player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
                ItemStack howTo = ItemUtils.createBook(Material.WRITTEN_BOOK, 1, ChatColor.AQUA.toString() + ChatColor.BOLD + "Coming Soon", ChatColor.GOLD + "CardinalPGM");
                player.getInventory().setItem(1, howTo);
                if (!GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
                    ItemStack picker = ItemUtils.createItem(Material.LEATHER_HELMET, 1, (short) 0,
                            ChatColor.GREEN + "" + ChatColor.BOLD + (GameHandler.getGameHandler().getMatch().getModules().getModule(ClassModule.class) != null ? new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(player.getLocale()) : new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(player.getLocale())),
                            Arrays.asList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_TIP).getMessage(player.getLocale())));
                    player.getInventory().setItem(2, picker);
                }
                player.getInventory().setItem(3, Tutorial.getEmerald(player));
                if (player.hasPermission("tnt.defuse")) {
                    ItemStack shears = ItemUtils.createItem(Material.SHEARS, 1, (short) 0, ChatColor.RED + new LocalizedChatMessage(ChatConstant.UI_TNT_DEFUSER).getMessage(player.getLocale()));
                    player.getInventory().setItem(5, shears);
                }
                player.teleport(chosen.getLocation());
            }
        }
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        if (event.getOldTeam() == null) {
            event.getPlayer().setMaxHealth(20);
            PlayerUtils.resetPlayer(event.getPlayer());
            TeamModule teamModule = event.getNewTeam();
            ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
            for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                if (spawnModule.getTeam() == teamModule) modules.add(spawnModule);
            }
            SpawnModule chosen = modules.getRandom();
            CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), chosen, event.getNewTeam());
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                event.getPlayer().teleport(chosen.getLocation());
            }
        } else if (match.getState().equals(MatchState.PLAYING)) {
            if (!event.getNewTeam().isObserver()) {
                if (event.getOldTeam().isObserver()) {
                    event.getPlayer().setMaxHealth(20);
                    PlayerUtils.resetPlayer(event.getPlayer());
                    TeamModule teamModule = event.getNewTeam();
                    ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
                    for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                        if (spawnModule.getTeam() == teamModule) modules.add(spawnModule);
                    }
                    SpawnModule chosen = modules.getRandom();
                    CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), chosen, event.getNewTeam());
                    Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                    if (!spawnEvent.isCancelled()) {
                        event.getPlayer().teleport(chosen.getLocation());
                    }
                } else {
                    event.getPlayer().setMetadata("teamChange", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), "teamChange"));
                    event.getPlayer().setHealth(0);
                }
            } else {
                TeamModule teamModule = event.getNewTeam();
                SpawnModule spawn = null;
                for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                    if (spawnModule.getTeam() == teamModule) spawn = spawnModule;
                }
                CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), spawn, event.getNewTeam());
                Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                if (!spawnEvent.isCancelled()) {
                    event.getPlayer().setMetadata("teamChange", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), "teamChange"));
                    event.getPlayer().setHealth(0);
                }
            }

        }

    }

}
