package in.twizmwaz.cardinal.module.modules.cardinalNotifications;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.GitUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MojangsonParseException;
import net.minecraft.server.MojangsonParser;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class CardinalNotifications implements Module {

    private final String notificationUrl = "https://raw.githubusercontent.com/twizmwazin/CardinalNotifications/master/update.json";
    private final String bookUrl = "https://raw.githubusercontent.com/twizmwazin/CardinalNotifications/master/book";

    public static BaseComponent[] chat = null;
    public static org.bukkit.inventory.ItemStack book = null;

    protected CardinalNotifications () {
        UpdateNotificationAndBook();
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (chat != null) event.getPlayer().sendMessage(chat);
    }

    public void UpdateNotificationAndBook(){
        Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    chat = ComponentSerializer.parse(GitUtil.getUpdateMessage(notificationUrl));

                    ItemStack NMSbook = new ItemStack(Item.getById(387));
                    try {
                        NBTBase nbtbase = MojangsonParser.parse(ChatColor.translateAlternateColorCodes('`', GitUtil.getUpdateMessage(bookUrl)));
                        NMSbook.setTag((NBTTagCompound) nbtbase);
                    } catch (MojangsonParseException mojangsonparseexception) {
                        Bukkit.getConsoleSender().sendMessage(mojangsonparseexception.getMessage());
                        return;
                    }
                    book = CraftItemStack.asBukkitCopy(NMSbook);
                } catch (IOException ignored) {
                }
            }
        });
    }

}