package in.twizmwaz.cardinal.chat;

import org.bukkit.entity.Player;

import java.util.Set;

public interface ChatChannel {

    /**
     * @param string Raw String to be sent to all players in this channel
     */
    public void sendMessage(String string);

    /**
     * @param message The generic message to be translated
     * @param strings String arguments for the localized message
     */
    public void sendLocalizedMessage(ChatMessage message, String[] strings);

    /**
     * @return A set of all the members of the channel
     */
    public Set<Player> getMembers();

    /**
     * @param player Adds player as a member
     */
    public void addMember(Player player);

    /**
     * @param player Removes player as a member
     */
    public void removeMember(Player player);

    /**
     * Removes all members from the team
     */
    public void resetMembers();
    
}
