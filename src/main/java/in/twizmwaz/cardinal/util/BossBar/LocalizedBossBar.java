package in.twizmwaz.cardinal.util.bossBar;


import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.util.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LocalizedBossBar {

    private ChatMessage bossBarTitle;
    private BarColor color;
    private BarStyle style;
    private Set<BarFlag> flags;
    private double progress;
    private boolean shown;
    private Map<Player, BossBar> playerBossBars = new HashMap<>();


    public LocalizedBossBar(ChatMessage bossBarTitle, BarColor color, BarStyle style, BarFlag... flags) {
        this.bossBarTitle = bossBarTitle;
        this.color = color;
        this.style = style;
        this.shown = true;
        this.flags = flags.length > 0 ? EnumSet.of(flags[0], flags):EnumSet.noneOf(BarFlag.class);
    }

    public BaseComponent getTitle(String locale) {
        return ChatUtil.baseComponentFromArray(TextComponent.fromLegacyText(bossBarTitle.getMessage(locale)));
    }

    public void setTitle(ChatMessage message) {
        this.bossBarTitle = message;
        for (Player player : playerBossBars.keySet()) {
            playerBossBars.get(player).setTitle(ChatUtil.baseComponentFromArray(TextComponent.fromLegacyText(bossBarTitle.getMessage(player.getLocale()))));
        }
    }

    public BarColor getColor() {
        return this.color;
    }

    public void setColor(BarColor color) {
        if (color != this.color) {
            this.color = color;
            for (BossBar bossbar : playerBossBars.values()) {
                bossbar.setColor(color);
            }
        }
    }

    public BarStyle getStyle() {
        return this.style;
    }

    public void setStyle(BarStyle style) {
        this.style = style;
        for (BossBar bossbar : playerBossBars.values()) {
            bossbar.setStyle(this.style);
        }
    }

    public void setFlags(Set<BarFlag> flags) {
        this.flags = flags;
        for (BossBar bossbar : playerBossBars.values()) {
            bossbar.setFlags(this.flags);
        }
    }

    public void removeFlag(BarFlag flag) {
        if (this.flags.contains(flag)) {
            this.flags.remove(flag);
            for (BossBar bossbar : playerBossBars.values()) {
                bossbar.setFlags(this.flags);
            }
        }
    }

    public void addFlag(BarFlag flag) {
        if (!this.flags.contains(flag)) {
            this.flags.remove(flag);
            for (BossBar bossbar : playerBossBars.values()) {
                bossbar.setFlags(this.flags);
            }
        }
    }

    public boolean hasFlag(BarFlag flag) {
        return this.flags.contains(flag);
    }

    public void setProgress(double progress) {
        if (progress == 0D || progress == 1D || Math.abs(this.progress - progress) > 0.0049D) {
            this.progress = progress;
            for (BossBar bossbar : playerBossBars.values()) {
                bossbar.setProgress(this.progress);
            }
        }
    }

    public double getProgress() {
        return this.progress;
    }

    public void addPlayer(Player player) {
        if (!playerBossBars.containsKey(player)) {
            BossBar bossBar = Bukkit.createBossBar(getTitle(player.getLocale()), this.color, this.style, this.flags.toArray(new BarFlag[flags.size()]));
            bossBar.setVisible(this.shown);
            bossBar.addPlayer(player);
            playerBossBars.put(player, bossBar);
        }
    }

    public void removePlayer(Player player) {
        if (playerBossBars.containsKey(player)) {
            playerBossBars.get(player).removePlayer(player);
            playerBossBars.remove(player);
        }
    }

    public void removeAll() {
        for (BossBar bossbar : playerBossBars.values()) {
            bossbar.removeAll();
        }
        playerBossBars.clear();
    }

    public void setVisible(Boolean visible) {
        if (visible != this.shown) {
            this.shown = visible;
            for (BossBar bossbar : playerBossBars.values()) {
                bossbar.setVisible(visible);
            }
        }
    }

}
