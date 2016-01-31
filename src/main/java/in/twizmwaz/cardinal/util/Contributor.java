package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;

public class Contributor {

    private String name;
    private String contribution;
    private UUID uniqueId;

    public Contributor(String name, String contribution) {
        this.name = name;
        this.contribution = contribution;
    }

    public Contributor(String name) {
        this(name, null);
    }

    public Contributor(UUID uniqueId, String contribution) {
        this.uniqueId = uniqueId;
        this.contribution = contribution;
    }

    public Contributor(UUID uniqueId) {
        this(uniqueId, null);
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        if (uniqueId == null) return ChatColor.DARK_AQUA + name;
        if (Bukkit.getOfflinePlayer(uniqueId).isOnline()) {
            return Bukkit.getPlayer(uniqueId).getDisplayName();
        } else {
            return Rank.getPrefix(uniqueId) + ChatColor.DARK_AQUA + name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContribution() {
        return contribution;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }
}
