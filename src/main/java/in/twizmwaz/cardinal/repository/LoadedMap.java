package in.twizmwaz.cardinal.repository;

import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Contributor;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LoadedMap implements Consumer<LoadedMap> {

    private String name, version, objective;
    private List<Contributor> authors, contributors;
    private List<String> rules;
    private int maxPlayers;
    private final File folder;
    private int id = -1;

    private static int maxId = 0;

    /**
     * @param name    The name of the map
     * @param authors The authors of the map
     * @param folder  The folder where the map can be found
     */
    public LoadedMap(String name, String version, String objective, List<Contributor> authors,
                         List<Contributor> contributors, List<String> rules, int maxPlayers, File folder) {
        this.folder = folder;
        update(name, version, objective, authors, contributors, rules, maxPlayers);
    }

    private void update(String name, String version, String objective, List<Contributor> authors,
                        List<Contributor> contributors, List<String> rules, int maxPlayers) {
        this.name = name;
        this.version = version;
        this.objective = objective;
        this.authors = authors;
        this.contributors = contributors;
        this.rules = rules;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public void accept(LoadedMap map) {
        update(map);
    }

    public void update(LoadedMap map) {
        update(map.name, map.version, map.objective, map.authors, map.contributors, map.rules, map.maxPlayers);
    }

    public void load() {
        if (id == -1) id = maxId++;
    }

    public int getId() {
        return id;
    }

    /**
     * @return Returns the name of the map
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the map version as a String
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return Returns the objective of the map
     */
    public String getObjective() {
        return objective;
    }

    /**
     * @return Returns the authors of the map with their contributions
     */
    public List<Contributor> getAuthors() {
        return authors;
    }

    /**
     * @return Returns the contributors of the map with their contributions
     */
    public List<Contributor> getContributors() {
        return contributors;
    }

    /**
     * @return Returns the custom map rules
     */
    public List<String> getRules() {
        return rules;
    }

    /**
     * @return Returns the maximum number of players for the map
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @return Returns the folder where the map can be found
     */
    public File getFolder() {
        return folder;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof LoadedMap && folder.equals(((LoadedMap) other).folder);
    }

    public String toShortMessage(ChatColor color, boolean showId, boolean showVersion) {
        return toShortMessage(color + "", showId, showVersion);
    }

    public String toShortMessage(String color, boolean showId, boolean showVersion) {
        return (showId ? ChatColor.YELLOW + "#" + id + " " : "") +
                color + getName() + (showVersion ? " " + ChatColor.GRAY + getVersion() : "");
    }

    public ChatMessage toChatMessage(boolean showId) {
        return new LocalizedChatMessage(ChatConstant.MISC_BY,
                new UnlocalizedChatMessage(toShortMessage(ChatColor.GOLD, showId, false) + ChatColor.DARK_PURPLE),
                ChatUtil.toChatMessage(getAuthors().stream()
                        .map(Contributor::getName).collect(Collectors.toList())));
    }

    public ChatMessage toIndexedMessage(boolean showId) {
        return new UnlocalizedChatMessage("${index}. {0}", toChatMessage(showId));
    }

}
