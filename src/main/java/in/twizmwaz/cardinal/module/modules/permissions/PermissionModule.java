package in.twizmwaz.cardinal.module.modules.permissions;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class PermissionModule implements Module {

    private final Plugin plugin;
    private final Map<Player, PermissionAttachment> attachmentMap;

    private List<UUID> developers = Arrays.asList(
            UUID.fromString("670223bb-7560-48c8-8f01-2f463549b917") /* twiz_mwazin */,
            UUID.fromString("33a703d0-3237-4337-9ddd-3dbf33b3d8a6") /* iEli2tyree011 */,
            UUID.fromString("208c84af-790a-41da-bf7e-eb184f17bdf8") /* Elly */,
            UUID.fromString("260004f0-996b-4539-ba21-df4ee6336b63") /* Elliott_ */,
            UUID.fromString("e82603df-417d-4a95-bd1f-1720648be0b4") /* Pablete1234 */);
    private List<OfflinePlayer> muted = new ArrayList<>();

    public PermissionModule(Plugin plugin) {
        this.plugin = plugin;
        this.attachmentMap = new HashMap<>();
    }

    public static boolean isDeveloper(UUID player) {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).getDevelopers().contains(player);
    }

    @Override
    public void unload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.removeAttachment(attachmentMap.get(player));
        }

        attachmentMap.clear();
    }

    public List<UUID> getDevelopers() {
        return developers;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        attachmentMap.put(event.getPlayer(), event.getPlayer().addAttachment(plugin));

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers())
            attachmentMap.put(player, player.addAttachment(plugin));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (attachmentMap.get(event.getPlayer()) != null) {
            event.getPlayer().removeAttachment(attachmentMap.get(event.getPlayer()));
            attachmentMap.remove(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent event) {
        event.getPlayer().removeAttachment(attachmentMap.get(event.getPlayer()));
        attachmentMap.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (Cardinal.getInstance().getConfig().getBoolean("worldEditPermissions")) {
            if ((event.getNewTeam().isPresent() && event.getNewTeam().get().isObserver()) || !GameHandler.getGameHandler().getMatch().isRunning()) {
                setWorldeditPermissions(event.getPlayer(), true);
            } else {
                setWorldeditPermissions(event.getPlayer(), false);
            }
        }
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        if (Cardinal.getInstance().getConfig().getBoolean("worldEditPermissions")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Optional<TeamModule> team = Teams.getTeamByPlayer(player);
                if ((team.isPresent() && team.get().isObserver())) {
                    setWorldeditPermissions(player, true);
                } else {
                    setWorldeditPermissions(player, false);
                }
            }
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        if (Cardinal.getInstance().getConfig().getBoolean("worldEditPermissions")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                setWorldeditPermissions(player, true);
            }
        }
    }

    public PermissionAttachment getPlayerAttachment(Player player) {
        return attachmentMap.get(player);
    }

    public void disablePermission(Player player, String permission) {
        attachmentMap.get(player).setPermission(permission, false);
    }

    public void enablePermission(Player player, String permission) {
        attachmentMap.get(player).setPermission(permission, true);
    }

    public void mute(Player player) {
        if (!muted.contains(player)) {
            muted.add(player);
            disablePermission(player, "cardinal.chat.team");
            disablePermission(player, "cardinal.chat.global");
        }
    }

    public void unmute(Player player) {
        if (muted.contains(player)) {
            muted.remove(player);
            enablePermission(player, "cardinal.chat.team");
            enablePermission(player, "cardinal.chat.global");
        }
    }

    public boolean isMuted(Player player) {
        return muted.contains(player);
    }

    public void setWorldeditPermissions(Player player, boolean state) {
        attachmentMap.get(player).setPermission("worldedit.navigation.jumpto.tool", state);
        attachmentMap.get(player).setPermission("worldedit.navigation.thru.tool", state);

        attachmentMap.get(player).setPermission("worldedit.navigation.jumpto.command", state);
        attachmentMap.get(player).setPermission("worldedit.navigation.thru.command", state);

        attachmentMap.get(player).setPermission("worldedit.navigation.unstuck", state);
    }

}
