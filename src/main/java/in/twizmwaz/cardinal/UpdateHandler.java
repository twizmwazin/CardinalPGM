
package in.twizmwaz.cardinal;

import com.google.gson.JsonParser;
import in.twizmwaz.cardinal.util.GitUtils;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class UpdateHandler {

    private static UpdateHandler handler;

    private final String updateLocation = "https://raw.githubusercontent.com/twizmwazin/CardinalNotifications/master/update.json";

    private boolean update;
    private String message;
    private final String localGitRevision;

    public UpdateHandler() throws IOException {
        handler = this;
        this.message = GitUtils.getUpdateMessage(updateLocation);
        this.localGitRevision = GameHandler.getGameHandler().getPlugin().getDescription().getVersion().substring(GameHandler.getGameHandler().getPlugin().getDescription().getVersion().length() - 6, GameHandler.getGameHandler().getPlugin().getDescription().getVersion().length());
    }

    public void sendUpdateMessage(Player player) {
        IChatBaseComponent chat = ChatSerializer.a(message);
        PacketPlayOutChat packet = new PacketPlayOutChat(chat);

        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        nmsPlayer.playerConnection.sendPacket(packet);
    }

    /**
     * @return Returns true/false if an there is a new update in the update.txt file on github
     */
    public boolean checkUpdates() {
        if (!GitUtils.getLatestGitRevision().startsWith(localGitRevision)) {
            update = true;
        }
        return update;
    }

    /**
     * @return Returns the URL of the file as a string
     */
    public String getUpdateLocation() {
        return updateLocation;
    }

    /**
     * @return Returns the URL of the file as a URL
     */
    public URL getUpdateURL() throws MalformedURLException {
        return new URL(updateLocation);
    }

    public static UpdateHandler getUpdateHandler() {
        return handler;
    }


}