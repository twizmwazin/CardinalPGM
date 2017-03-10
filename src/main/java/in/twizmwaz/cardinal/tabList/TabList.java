package in.twizmwaz.cardinal.tabList;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.PlayerNameUpdateEvent;
import in.twizmwaz.cardinal.event.RankChangeEvent;
import in.twizmwaz.cardinal.event.TeamNameChangeEvent;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.tabList.entries.EmptyTabEntry;
import in.twizmwaz.cardinal.tabList.entries.PlayerTabEntry;
import in.twizmwaz.cardinal.tabList.entries.SkinTabEntry;
import in.twizmwaz.cardinal.tabList.entries.TabEntry;
import in.twizmwaz.cardinal.tabList.entries.TeamTabEntry;
import in.twizmwaz.cardinal.module.modules.teamRegister.TeamRegisterModule;
import in.twizmwaz.cardinal.rank.Rank;
import in.twizmwaz.cardinal.util.PacketUtils;
import in.twizmwaz.cardinal.util.Teams;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutPlayerInfo;
import net.minecraft.server.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.Skin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSkinPartsChangeEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TabList implements Listener {

    private static long TAB_UPDATE_TIME = 5L;

    public static int columnsPerTeam = 0;

    // Entries
    private static Map<UUID, PlayerTabEntry> fakePlayer = new HashMap<>();
    private static Map<String, TeamTabEntry> teamTitles = new HashMap<>();
    private static List<EmptyTabEntry> emptyPlayers = new ArrayList<>();

    // Views
    private static Map<Player, TabView> playerView = new HashMap<>();

    // Update handling
    private static boolean scheduledUpdate = false;
    private static Set<TabEntry> updateEntries = new HashSet<>();
    private static Set<TeamModule> teamNeedUpdate = new HashSet<>();

    public TabList() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                PacketPlayOutPlayerInfo listPacket =
                        new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY);
                for (PlayerTabEntry entry : fakePlayer.values()) {
                    listPacket.add(entry.getPlayerInfo(null, listPacket));
                }
                PacketUtils.broadcastPacket(listPacket);
            }
        }, 0L, 600L);
        scheduler.scheduleSyncRepeatingTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (SkinTabEntry entry : teamTitles.values()) {
                    entry.setHat(true);
                }
            }
        }, 0L, 20L);
        scheduler.scheduleSyncRepeatingTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (SkinTabEntry entry : teamTitles.values()) {
                    entry.setHat(false);
                }
            }
        }, 5L, 20L);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void beforeCycleComplete(CycleCompleteEvent event) {
        List<String> names = Lists.newArrayList();
        for (Player player : Bukkit.getOnlinePlayers()) {
            names.add(player.getName());
        }
        PacketUtils.broadcastPacket(getTeamPacket(names, 80, 3));
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        columnsPerTeam = Math.max(4 / (Teams.getTeams().size() - 1), 1);
        resetTeams();
        updateAll();
        for (PlayerTabEntry entry : fakePlayer.values()) {
            entry.broadcastCreateSkinParts();
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerView.put(event.getPlayer(), new TabView(event.getPlayer()));
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        playerView.remove(event.getPlayer());
        removePlayer(event.getPlayer());
        updateAll();
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onTeamChange(PlayerChangeTeamEvent event) {
        addUpdateTeam(event.getNewTeam());
        renderAllTeamTitles();
        updateAll();
    }

    @EventHandler
    public void onTeamChangeName(TeamNameChangeEvent event) {
        renderTeamTitle(event.getTeam());
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDisplayNameChange(PlayerNameUpdateEvent event) {
        if (!playerView.containsKey(event.getPlayer())) return;
        addUpdateName(getPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onRankChange(RankChangeEvent event) {
        if (!event.isOnline()) return;
        addUpdateTeam(Teams.getTeamByPlayer(event.getPlayer()));
        updateAll();
    }

    @EventHandler
    public void onPlayerChangeSkinParts(PlayerSkinPartsChangeEvent event) {
        getPlayer(event.getPlayer()).setHat(event.getPlayer().getSkinParts().contains(Skin.Part.HAT));
    }

    private static void updateAll() {
        if (!scheduledUpdate) {
            scheduledUpdate = true;
            Bukkit.getScheduler().runTaskLaterAsynchronously(Cardinal.getInstance(), new Runnable() {
                @Override
                public void run() {
                    scheduledUpdate = false;
                    try {
                        for (TabView view : playerView.values()) view.update();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, TAB_UPDATE_TIME);
        }
    }

    private static void addUpdateName(TabEntry entry) {
        if (updateEntries.size() == 0) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable() {
                @Override
                public void run() {
                    try {
                        Set<TabEntry> entries = new HashSet<>(updateEntries);
                        updateEntries.clear();
                        PacketPlayOutPlayerInfo listPacket = new PacketPlayOutPlayerInfo(
                                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
                        boolean compact = false;
                        for (TabEntry entry : entries) {
                            if (entry instanceof PlayerTabEntry) {
                                entry.broadcastTabListPacket(
                                        PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
                            } else {
                                compact = true;
                                listPacket.add(entry.getPlayerInfo(null, listPacket));
                            }
                        }
                        if (compact) PacketUtils.broadcastPacket(listPacket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, TAB_UPDATE_TIME);
        }
        updateEntries.add(entry);
    }

    private static void addUpdateTeam(Optional<TeamModule> team) {
        if (team != null && team.isPresent()) {
            teamNeedUpdate.add(team.get());
        }
    }

    public static boolean updateTeam(TeamModule team) {
        if (teamNeedUpdate.contains(team)) {
            teamNeedUpdate.remove(team);
            return true;
        }
        return false;
    }

    public static void renderAllTeamTitles() {
        for (TabEntry entry : teamTitles.values()) {
            addUpdateName(entry);
        }
    }

    public static void renderTeamTitle(TeamModule team) {
        if (team.isObserver()) return;
        addUpdateName(getTeam(team));
    }

    public static List<TabEntry> getTabEntries() {
        List<TabEntry> result = Lists.newArrayList();
        result.addAll(fakePlayer.values());
        result.addAll(teamTitles.values());
        result.addAll(emptyPlayers);
        return result;
    }

    public static Collection<TabView> getTabViews() {
        return playerView.values();
    }

    public static PlayerTabEntry getPlayer(Player player) {
        if (!fakePlayer.containsKey(player.getUniqueId())) {
            fakePlayer.put(player.getUniqueId(), new PlayerTabEntry(player));
        }
        return fakePlayer.get(player.getUniqueId());
    }

    private static void removePlayer(Player player) {
        if (!fakePlayer.containsKey(player.getUniqueId())) return;
        TabEntry entry = getPlayer(player);
        entry.destroy();
        fakePlayer.remove(player.getUniqueId());
    }

    public static TeamTabEntry getTeam(TeamModule team) {
        if (!teamTitles.containsKey(team.getId()))
            teamTitles.put(team.getId(), new TeamTabEntry(team));
        return teamTitles.get(team.getId());
    }

    private static void resetTeams() {
        if (teamTitles.isEmpty()) return;
        for (TabEntry entry : teamTitles.values()) {
            entry.destroy();
        }
        teamTitles.clear();
    }

    public static EmptyTabEntry getFakePlayer(TabView view) {
        for (EmptyTabEntry emptyPlayer : emptyPlayers) {
            if (view.getEmptyPlayers().contains(emptyPlayer)) continue;
            return emptyPlayer;
        }
        EmptyTabEntry fakePlayer = new EmptyTabEntry();
        emptyPlayers.add(fakePlayer);
        return fakePlayer;
    }

    public static Packet getTeamPacket(String player, int slot, int action) {
        return getTeamPacket(player == null ? Collections.<String>emptyList() : Collections.singletonList(player),
                slot, action);
    }

    public static Packet getTeamPacket(Collection<String> players, int slot, int action) {
        String team = "\000TabView" + (slot < 10 ? "0" + slot : slot);
        return new PacketPlayOutScoreboardTeam(action, team, team, "", "", -1, "never", "never", 0, players);
    }

}
