package in.twizmwaz.cardinal.util.polls;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Polls {

    private static Map<Integer, Poll> polls = new HashMap<>();

    public static boolean multiple() {
        return polls.size() > 1;
    }

    public static int getPoll() {
        return polls.size() == 0 ? 0 : polls.size() == 1 ? polls.keySet().iterator().next() : -1;
    }

    public static boolean isPoll(int id) {
        return polls.containsKey(id);
    }

    public static boolean isAny(int id) {
        return polls.get(id).any();
    }

    public static boolean vote(int id, Player player, boolean vote) {
        boolean result = polls.get(id).vote(player.getUniqueId(), vote);
        polls.get(id).updateTitle();
        return result;
    }

    public static void addPoll(CommandSender sender, String command, int time, boolean any) {
        int id = polls.size() == 0 ? 1 : Collections.max(polls.keySet()) + 1;
        polls.put(id, new Poll(id, sender, command, time, any));
        updatePolls();
    }

    public static void stopPoll(int id, CommandSender sender) {
        polls.get(id).stop(sender);
        updatePolls();
    }

    protected static void removePoll(int id) {
        polls.remove(id);
    }

    private static void updatePolls() {
        for (Poll poll : polls.values()) poll.updateTitle();
    }

}
