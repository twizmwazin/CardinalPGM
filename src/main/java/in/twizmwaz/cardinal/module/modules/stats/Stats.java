package in.twizmwaz.cardinal.module.modules.stats;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.PlayerSettingChangeEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.PacketUtils;
import in.twizmwaz.cardinal.util.bossBar.LocalizedBossBar;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutScoreboardScore;
import net.minecraft.server.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class Stats implements Module {

    private static DecimalFormat format = new DecimalFormat("0.00");

    private Map<UUID, Integer> kills = new HashMap<>();
    private Map<UUID, Integer> deaths = new HashMap<>();

    private List<UUID> sidebarView = new ArrayList<>();
    private Map<UUID, LocalizedBossBar> bossBars = new HashMap<>();
    private Map<UUID, Integer> actionBarTasks = new HashMap<>();

    private static String scoreboardEntry = ChatColor.WHITE + " D:" + ChatColor.RED;

    private static PacketPlayOutScoreboardScore getScoreboardPacket(String entry, int score, PacketPlayOutScoreboardScore.EnumScoreboardAction action) {
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore();
        PacketUtils.setField("a", packet, entry);
        PacketUtils.setField("b", packet, "scoreboard");
        PacketUtils.setField("c", packet, score);
        PacketUtils.setField("d", packet, action);
        return packet;
    }

    @Override
    public void unload() {
        kills.clear();
        deaths.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            clearDisplay(player, Settings.getSettingByName("Stats").getValueByPlayer(player).getValue());
        }
        HandlerList.unregisterAll(this);
    }

    public int getKills(UUID player) {
        if (!kills.containsKey(player)) kills.put(player, 0);
        return kills.get(player);
    }

    public void addKill(Player player) {
        kills.put(player.getUniqueId(), getKills(player.getUniqueId()) + 1);
        updateDisplay(player);
    }

    public int getDeaths(UUID player) {
        if (!deaths.containsKey(player)) deaths.put(player, 0);
        return deaths.get(player);
    }

    public void addDeath(Player player) {
        deaths.put(player.getUniqueId(), getDeaths(player.getUniqueId()) + 1);
        updateDisplay(player);
    }

    private boolean shouldShow(Player player) {
        return getKills(player.getUniqueId()) != 0 || getDeaths(player.getUniqueId()) != 0;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(CardinalDeathEvent event) {
        if (event.getKiller() != null) addKill(event.getKiller());
        addDeath(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeamChange(PlayerChangeTeamEvent event) {
        updateDisplay(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateDisplay(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event) {
        clearDisplay(event.getPlayer(), Settings.getSettingByName("Stats").getValueByPlayer(event.getPlayer()).getValue());
    }

    @EventHandler
    public void onPlayerChangeSetting(PlayerSettingChangeEvent event) {
        if (!event.getSetting().equals(Settings.getSettingByName("Stats"))) return;
        clearDisplay(event.getPlayer(), event.getOldValue().getValue());
        updateDisplay(event.getPlayer());
    }

    private void sendSlotPackets(Player player, boolean set) {
        PacketPlayOutScoreboardScore.EnumScoreboardAction action = set ? PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE : PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE;
        Packet blankSlot = getScoreboardPacket("", -1, action);
        Packet scoreSlot = getScoreboardPacket(ChatColor.WHITE + " D:" + ChatColor.RED, -2, action);
        PacketUtils.sendPacket(player, blankSlot);
        PacketUtils.sendPacket(player, scoreSlot);
    }

    private void sendTeamPackets(Player player, boolean set) {
        String prefix = "K:" + ChatColor.GREEN + "0";
        String suffix = "0" + ChatColor.WHITE + " K/D:" + ChatColor.AQUA + "0.00";
        PacketUtils.sendPacket(player, new PacketPlayOutScoreboardTeam(set ? 0 : 1, "scoreboard-stats", "scoreboard-stats", prefix, suffix, -1, "never", "never", 0, Collections.singletonList(scoreboardEntry)));
    }

    private void sendActionBarPacket(Player player, int kills, int deaths, String kd) {
        player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(getLocalizedMessage(kills, deaths, kd).getMessage(player.getLocale())));
    }

    public void updateDisplay(final Player player) {
        if (!shouldShow(player)) return;

        final int kills = getKills(player.getUniqueId());
        final int deaths = getDeaths(player.getUniqueId());
        final String kd = format.format((double)kills / Math.max(deaths, 1)).replace(",", ".");

        switch (Settings.getSettingByName("Stats").getValueByPlayer(player).getValue()) {
            case "sidebar":
                if (!sidebarView.contains(player.getUniqueId())) {
                    sidebarView.add(player.getUniqueId());
                    sendTeamPackets(player, true);
                }
                sendSlotPackets(player, true);
                String prefix = "K:" + ChatColor.GREEN + Math.min(kills, 999);
                String suffix = "" + Math.min(deaths, 999) + ChatColor.WHITE + " K/D:" + ChatColor.AQUA + kd;
                PacketUtils.sendPacket(player, new PacketPlayOutScoreboardTeam(2, "scoreboard-stats", "scoreboard-stats", prefix, suffix, -1, "never", "never", 0, Collections.singletonList(scoreboardEntry)));
                break;
            case "boss bar":
                if (!bossBars.containsKey(player.getUniqueId())) {
                    LocalizedBossBar bossBar = new LocalizedBossBar(new UnlocalizedChatMessage(""), BarColor.PURPLE, BarStyle.SOLID);
                    bossBar.addPlayer(player);
                    bossBars.put(player.getUniqueId(), bossBar);
                }
                bossBars.get(player.getUniqueId()).setTitle(getLocalizedMessage(kills, deaths, kd));
                break;
            case "action bar":
                if (actionBarTasks.containsKey(player.getUniqueId())) {
                    Bukkit.getScheduler().cancelTask(actionBarTasks.get(player.getUniqueId()));
                }
                actionBarTasks.put(player.getUniqueId(), Bukkit.getScheduler().scheduleSyncRepeatingTask(Cardinal.getInstance(), new Runnable() {

                    private int tick;

                    @Override
                    public void run() {
                        if (tick > 40) {
                            if (actionBarTasks.containsKey(player.getUniqueId())) {
                                Bukkit.getScheduler().cancelTask(actionBarTasks.get(player.getUniqueId()));
                                actionBarTasks.remove(player.getUniqueId());
                            }
                        } else {
                            sendActionBarPacket(player, kills, deaths, kd);
                            tick++;
                        }
                    }
                }, 1L, 1L));
                break;
        }
    }

    public void clearDisplay(Player player, String setting) {
        switch (setting) {
            case "sidebar":
                if (sidebarView.contains(player.getUniqueId())) {
                    sendTeamPackets(player, false);
                    sendSlotPackets(player, false);
                    sidebarView.remove(player.getUniqueId());
                }
            case "boss bar":
                if (bossBars.containsKey(player.getUniqueId())) {
                    bossBars.get(player.getUniqueId()).removePlayer(player);
                    bossBars.remove(player.getUniqueId());
                }
            case "action bar":
                if (actionBarTasks.containsKey(player.getUniqueId())) {
                    Bukkit.getScheduler().cancelTask(actionBarTasks.get(player.getUniqueId()));
                    actionBarTasks.remove(player.getUniqueId());
                }
        }
    }

    private ChatMessage getLocalizedMessage(int kills, int deaths, String kd) {
        return new LocalizedChatMessage(ChatConstant.UI_STATS_DISPLAY, ChatColor.GREEN + "" + kills + ChatColor.WHITE, ChatColor.RED + "" + deaths + ChatColor.WHITE, ChatColor.AQUA + kd + ChatColor.WHITE);
    }

}


