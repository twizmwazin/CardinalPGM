package in.twizmwaz.cardinal.util;

import com.google.common.collect.Lists;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.DataWatcherObject;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArmorStand;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import org.bukkit.ChatColor;

import java.lang.reflect.Field;
import java.util.List;

public class Watchers {

    public static final DataWatcher.Item<?> INVISIBLE = new DataWatcher.Item<>(Entity.class, Entity.FLAGS, (byte) 0x20);
    public static final DataWatcher.Item<?> SNOWFLAKE = new DataWatcher.Item<>(EntityItem.class, getDataWatcher(EntityItem.class, "c", ItemStack.class), new ItemStack(Item.getById(332)));
    public static final DataWatcher.Item<?> HAT_ON = new DataWatcher.Item<>(EntityPlayer.class, EntityPlayer.SKIN_PARTS, (byte) 0x40);
    public static final DataWatcher.Item<?> HAT_OFF = new DataWatcher.Item<>(EntityPlayer.class, EntityPlayer.SKIN_PARTS, (byte) 0x0);

    public static List<DataWatcher.Item<?>> toList(DataWatcher.Item<?>... items) {
        return Lists.newArrayList(items);
    }

    @SuppressWarnings("unchecked")
    private static <T> DataWatcherObject<T> getDataWatcher(Class<? extends Entity> cl, String field, Class<T> result) {
        try {
            Field entryField = cl.getDeclaredField(field);
            entryField.setAccessible(true);
            return (DataWatcherObject<T>) entryField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<DataWatcher.Item<?>> getHealth(float health) {
        return toList(new DataWatcher.Item<>(EntityLiving.class, EntityLiving.HEALTH, health));
    }

    public static List<DataWatcher.Item<?>> getDamageIndicator(double damage) {
        Class<EntityArmorStand> cl = EntityArmorStand.class;
        return Lists.newArrayList(
                INVISIBLE,                                                                          // (0) Sets invisible
                new DataWatcher.Item<>(cl, getDataWatcher(Entity.class, "aA", String.class), "" +
                        ChatColor.RED + ChatColor.BOLD + Math.round(damage / 0.2)),                 // (2) Custom Name
                new DataWatcher.Item<>(cl, getDataWatcher(Entity.class, "aB", Boolean.class), true),// (3) Custom Name visible
                new DataWatcher.Item<>(cl, EntityArmorStand.a, (byte) 0x10));                       // (11) Marker Armor Stand
    }

}
