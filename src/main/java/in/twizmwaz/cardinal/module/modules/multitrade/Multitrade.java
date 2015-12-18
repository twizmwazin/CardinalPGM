package in.twizmwaz.cardinal.module.modules.multitrade;

import in.twizmwaz.cardinal.module.Module;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IMerchant;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MerchantRecipe;
import net.minecraft.server.v1_8_R3.MerchantRecipeList;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftVillager;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class Multitrade implements Module {
    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void handleRightClick(PlayerInteractEntityEvent event) {
        if ((event.getRightClicked() instanceof Villager)) {
            event.setCancelled(true);
            EntityPlayer player = ((CraftPlayer)event.getPlayer()).getHandle();
            EntityVillager villager = ((CraftVillager)event.getRightClicked()).getHandle();
            openVillager(player, villager);
        }
    }

    public void openVillager(final EntityPlayer player, final EntityVillager villager) {
        player.openTrade(new IMerchant() {
            @Override
            public void a_(EntityHuman entityHuman) {

            }

            @Override
            public EntityHuman v_() {
                return player;
            }

            @Override
            public MerchantRecipeList getOffers(EntityHuman entityHuman) {
                return villager.getOffers(player);
            }

            @Override
            public void a(MerchantRecipe merchantRecipe) {

            }

            @Override
            public void a_(ItemStack itemStack) {

            }

            @Override
            public IChatBaseComponent getScoreboardDisplayName() {
                return villager.getScoreboardDisplayName();
            }
        });
    }
}
