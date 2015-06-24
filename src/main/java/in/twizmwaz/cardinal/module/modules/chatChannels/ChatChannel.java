package in.twizmwaz.cardinal.module.modules.chatChannels;

import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface ChatChannel extends Module {

    /**
     * @param string Raw String to be sent to all players in this channel
     */
    public void sendMessage(String string);

    /**
     * @param message The generic message to be translated
     */
    public void sendLocalizedMessage(ChatMessage message);

    /**
     * @return A set of all the members of the channel
     */
    public Collection<? extends Player> getMembers();

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
