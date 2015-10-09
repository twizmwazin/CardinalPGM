package in.twizmwaz.cardinal.module.modules.monumentModes;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.bossBar.BossBar;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.MiscUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.List;

public class MonumentModes implements TaskedModule {

    private final Material material;
    private final int damageValue;
    private final String name;
    private int after, showBefore;
    private boolean ran;

    public MonumentModes(int after, final Material material, final int damageValue, final String name, int showBefore) {
        this.after = after;
        this.material = material;
        this.damageValue = damageValue;
        this.name = name;
        this.showBefore = showBefore;

        this.ran = false;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if (!this.ran && MatchTimer.getTimeInSeconds() >= this.after) {
                for (MonumentModes mode : GameHandler.getGameHandler().getMatch().getModules().getModules(MonumentModes.class)) {
                    if (mode.getTimeAfter() < this.after && !mode.hasRan()) {
                        return;
                    }
                }
                for (CoreObjective core : GameHandler.getGameHandler().getMatch().getModules().getModules(CoreObjective.class)) {
                    if (core.changesModes()) {
                        for (Block block : core.getCore()) {
                            if (core.partOfObjective(block)) {
                                block.setType(this.material);
                                block.setData((byte) this.damageValue);
                            }
                        }
                        core.setMaterial(this.material, this.damageValue);
                    }
                }
                for (DestroyableObjective destroyable : GameHandler.getGameHandler().getMatch().getModules().getModules(DestroyableObjective.class)) {
                    if (destroyable.changesModes()) {
                        for (Block block : destroyable.getMonument()) {
                            if (destroyable.partOfObjective(block)) {
                                block.setType(this.material);
                                block.setData((byte) this.damageValue);
                            }
                        }
                        destroyable.setMaterial(this.material, this.damageValue);
                    }
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (Settings.getSettingByName("Sounds") != null && Settings.getSettingByName("Sounds").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
                        player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, (float) 0.15, (float) 1.2);
                    }
                }
                ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "> > > > " + ChatColor.RED + name + ChatColor.DARK_AQUA + " < < < <"));
                this.ran = true;
            }
            ModuleCollection<MonumentModes> modes = GameHandler.getGameHandler().getMatch().getModules().getModules(MonumentModes.class);
            HashMap<MonumentModes, Integer> modesWithTime = new HashMap<>();
            for (MonumentModes modeForTime : modes) {
                modesWithTime.put(modeForTime, modeForTime.getTimeAfter());
            }
            List<MonumentModes> sortedModes = MiscUtil.getSortedHashMapKeyset(modesWithTime);
            int timeBeforeMode = 1;
            int showBefore = (after < 60 ? after : 60);
            String name = MonumentModes.getModeName();
            for (MonumentModes mode : sortedModes) {
                if (!mode.hasRan()) {
                    timeBeforeMode = mode.getTimeAfter() - (int) MatchTimer.getTimeInSeconds();
                    name = mode.getName();
                    showBefore = mode.getShowBefore();
                }
            }
            if (timeBeforeMode > 0 && timeBeforeMode <= showBefore && name != null) {
                if (showBefore >= TimeLimit.getMatchTimeLimit() || showBefore >= after) {
                    BossBar.sendGlobalBossBar(new UnlocalizedChatMessage(ChatColor.RED + name), 100F);
                } else {
                    int percent = (int) (timeBeforeMode * 100F) / showBefore;
                    if (timeBeforeMode <= 1) percent = 0;
                    BossBar.sendGlobalBossBar(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.UI_MODE_IN_TIME, new UnlocalizedChatMessage(ChatColor.RED + name + ChatColor.AQUA)), ((timeBeforeMode) == 1 ? new LocalizedChatMessage(ChatConstant.UI_SECOND, ChatColor.DARK_AQUA + "1" + ChatColor.AQUA) : new LocalizedChatMessage(ChatConstant.UI_SECONDS, ChatColor.DARK_AQUA + String.valueOf(timeBeforeMode) + ChatColor.AQUA))), percent);
                }
            }
            if (timeBeforeMode <= 0 || (TimeLimit.getMatchTimeLimit() == 0 && (timeBeforeMode > showBefore || timeBeforeMode <= 0))) {
                BossBar.delete();
            }
        }
    }

    public static String getModeName() {
        for (MonumentModes module : GameHandler.getGameHandler().getMatch().getModules().getModules(MonumentModes.class)) {
            if (!MonumentModes.hasRanMode()) {
                return module.getName();
            }
        }
        return null;
    }

    public static boolean hasRanMode() {
        for (MonumentModes module : GameHandler.getGameHandler().getMatch().getModules().getModules(MonumentModes.class)) {
            return module.hasRan();
        }
        return false;
    }

    public Material getType() {
        return material;
    }

    public boolean hasRan() {
        return ran;
    }

    public int getTimeAfter() {
        return after;
    }

    public String getName() {
        return name;
    }

    public int getShowBefore() {
        return showBefore;
    }

    public void setTimeAfter(int after) {
        this.after = after;
    }

}
