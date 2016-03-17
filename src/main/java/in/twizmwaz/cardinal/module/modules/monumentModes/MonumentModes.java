package in.twizmwaz.cardinal.module.modules.monumentModes;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.bossBar.BossBars;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class MonumentModes implements TaskedModule {

    private final Pair<Material, Integer> material;
    private final String name;
    private int after, showBefore;
    private boolean ran;
    private String bossBar;

    public MonumentModes(int after, final Pair<Material, Integer> material, final String name, int showBefore) {
        this.after = after;
        this.material = material;
        this.name = name;
        this.showBefore = showBefore;

        this.ran = false;

        this.bossBar = BossBars.addBroadcastedBossBar(new UnlocalizedChatMessage(""), BarColor.BLUE, BarStyle.SOLID, false);
    }

    @Override
    public void unload() {
        BossBars.removeBroadcastedBossBar(bossBar);
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        BossBars.removeBroadcastedBossBar(bossBar);
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
                                block.setType(this.material.getLeft());
                                block.setData((byte)(int)this.material.getRight());
                            }
                        }
                        core.setMaterial(this.material.getLeft(), (byte)(int)this.material.getRight());
                    }
                }
                for (DestroyableObjective destroyable : GameHandler.getGameHandler().getMatch().getModules().getModules(DestroyableObjective.class)) {
                    if (destroyable.changesModes()) {
                        for (Block block : destroyable.getMonument()) {
                            if (destroyable.partOfObjective(block)) {
                                block.setType(this.material.getLeft());
                                block.setData((byte)(int)this.material.getRight());
                            }
                        }
                        destroyable.setMaterial(this.material.getLeft(), (byte)(int)this.material.getRight());
                    }
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (Settings.getSettingByName("Sounds") != null && Settings.getSettingByName("Sounds").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
                        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, (float) 0.15, (float) 1.2);
                    }
                }
                ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "> > > > " + ChatColor.RED + name + ChatColor.DARK_AQUA + " < < < <"));
                this.ran = true;
                BossBars.removeBroadcastedBossBar(bossBar);
            }
            double timeBeforeMode = getTimeAfter() - MatchTimer.getTimeInSeconds();
            if (timeBeforeMode > 0 && timeBeforeMode <= showBefore && name != null && !ran) {
                String time = String.valueOf((int)timeBeforeMode + 1);
                BossBars.setVisible(bossBar, true);
                ChatMessage message;
                if (timeBeforeMode > 3600) message = new UnlocalizedChatMessage(ChatColor.RED + name + ChatColor.AQUA);
                else message = new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.UI_MODE_IN_TIME, new UnlocalizedChatMessage(ChatColor.RED + name + ChatColor.AQUA)),
                            new LocalizedChatMessage(time.equals("1") ? ChatConstant.UI_SECOND : ChatConstant.UI_SECONDS, ChatColor.DARK_AQUA + time + ChatColor.AQUA));
                BossBars.setTitle(bossBar, message);
                BossBars.setProgress(bossBar, timeBeforeMode / showBefore);
            }
        }
    }

    public Material getType() {
        return material.getLeft();
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

    public void setTimeAfter(int after) {
        this.after = after;
    }

}
