package in.twizmwaz.cardinal.module.modules.monumentModes;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.util.ChatUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

public class MonumentModes implements TaskedModule {

    private int after, showBefore;
    private final Material material;
    private final int damageValue;
    private final String name;

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
            if (!this.ran && MatchTimer.getTimeInSeconds() - this.after <= showBefore) {
                int time = (int) (MatchTimer.getTimeInSeconds() - this.after);
                if (time % 5 == 0 || time <= 5) {
                    ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.WHITE + "{0}", new LocalizedChatMessage(ChatConstant.UI_CORE_MODE, name)));
                }
            }

            if (!this.ran && MatchTimer.getTimeInSeconds() >= this.after) {
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
                ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "> > > > " + ChatColor.RED + name + ChatColor.DARK_AQUA + " < < < <"));
                this.ran = true;
            }
        }
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

    public void setTimeAfter(int after) {
        this.after = after;
    }

}
