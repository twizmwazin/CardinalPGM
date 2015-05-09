package in.twizmwaz.cardinal.rank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Rank {

    private String name;
    private String flair;
    private boolean isStaff;
    private boolean isDefault;
    private List<UUID> players;

    private static List<Rank> ranks = new ArrayList<>();

    public Rank(String name, String flair, boolean isStaff, boolean isDefault) {
        this.name = name;
        this.flair = flair;
        this.isStaff = isStaff;
        this.isDefault = isDefault;
        this.players = new ArrayList<>();
    }

    public static List<Rank> getRanks() {
        return ranks;
    }

    public String getName() {
        return name;
    }

    public String getFlair() {
        return flair;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public void addPlayer(UUID player) {
        players.add(player);
    }

    public void removePlayer(UUID player) {
        players.remove(player);
    }

    public static List<Rank> getDefaultRanks() {
        List<Rank> results = new ArrayList<>();
        for (Rank rank : ranks) {
            if (rank.isDefault()) {
                results.add(rank);
            }
        }
        return results;
    }

    public static List<Rank> getRanksByPlayer(UUID player) {
        List<Rank> results = new ArrayList<>();
        for (Rank rank : ranks) {
            if (rank.getPlayers().contains(player)) {
                results.add(rank);
            }
        }
        return results;
    }

}
