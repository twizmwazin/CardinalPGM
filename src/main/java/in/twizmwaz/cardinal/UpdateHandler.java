package in.twizmwaz.cardinal;

import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateHandler {

    private final String updateLocation = "https://raw.githubusercontent.com/twizmwazin/CardinalPGM/master/update.txt";

    private boolean update = false;

    private String version;
    private String link;
    private boolean urgent;
    private String message;

    public UpdateHandler() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(getUpdateURL().openStream()));
        setVersion(in.readLine().replace("version: ", ""));
        setLink(in.readLine().replace("link: ", ""));
        setUrgent(Boolean.parseBoolean(in.readLine().replace("urgent: ", "")));
        setMessage(in.readLine().replace("message: ", ""));
        if (checkUpdates()) {
            broadcastUpdate();
        }
    }

    public void broadcastUpdate() {
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.STRIKETHROUGH + "----------------" + ChatColor.BOLD + "" + ChatColor.GOLD + " Cardinal Update " + ChatColor.WHITE + "(" + version + ")"  + ChatColor.DARK_RED + " " + ChatColor.STRIKETHROUGH + "----------------");
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Urgent: " + (urgent ? ChatColor.RED : ChatColor.GREEN) + urgent);
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "Link: " + link);
        Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Message: " + message);
    }


    /**
     * @return Returns true/false if an there is a new update in the update.txt file on github
     */
    public boolean checkUpdates() {
        if (!GameHandler.getGameHandler().getPlugin().getConfig().get("current-version").toString().startsWith(version)) {
            Bukkit.broadcastMessage(version.trim().replaceAll(" ", "#"));
            Bukkit.broadcastMessage(GameHandler.getGameHandler().getPlugin().getConfig().get("current-version").toString().trim().replaceAll(" ", "#"));
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

    /**
     * @param version version of plugin in update.txt
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return Returns the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param link link to download of new update
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return Returns the link to the new download
     */
    public String getLink() {
        return link;
    }

    /**
     * @param urgent if the update is urgent
     */
    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    /**
     * @return Returns weather the update is urgent
     */
    public boolean isUrgent() {
        return urgent;
    }

    /**
     * @param message sets the message of the update
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return Returns the update message
     */
    public String getMessage() {
        return message;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

}
