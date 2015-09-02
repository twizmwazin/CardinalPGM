package in.twizmwaz.cardinal.module.modules.teamManager;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.classModule.ClassModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.teamRegister.TeamRegisterModule;
import in.twizmwaz.cardinal.module.modules.tutorial.Tutorial;
import in.twizmwaz.cardinal.util.Contributor;
import in.twizmwaz.cardinal.util.Items;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TeamManagerModule implements Module {

    private final Match match;

    protected TeamManagerModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Players.resetPlayer(player);
        String playerName = event.getPlayer().getName();
        Map<String, String> playerTeams = GameHandler.getGameHandler().getMatch().getModules().getModule(TeamRegisterModule.class).getPlayersTeams();
        Teams.getTeamById("observers").get().add(player, true, false);
        event.getPlayer().getInventory().setItem(0, new ItemStack(Material.COMPASS));
        ItemStack howTo = Items.createBook(Material.WRITTEN_BOOK, 1, ChatColor.AQUA.toString() + ChatColor.BOLD + "Coming Soon", ChatColor.GOLD + "CardinalPGM");
        event.getPlayer().getInventory().setItem(1, howTo);
        if (!GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
            ItemStack picker = Items.createItem(Material.LEATHER_HELMET, 1, (short) 0,
                    ChatColor.GREEN + "" + ChatColor.BOLD + (GameHandler.getGameHandler().getMatch().getModules().getModule(ClassModule.class) != null ? new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(player.getLocale()) : new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(player.getLocale())),
                    Arrays.asList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_TIP).getMessage(player.getLocale())));
            player.getInventory().setItem(2, picker);
        }
        player.getInventory().setItem(3, Tutorial.getEmerald(player));
        if (player.hasPermission("tnt.defuse")) {
            ItemStack shears = Items.createItem(Material.SHEARS, 1, (short) 0, ChatColor.RED + new LocalizedChatMessage(ChatConstant.UI_TNT_DEFUSER).getMessage(player.getLocale()));
            player.getInventory().setItem(5, shears);
        }
        if(playerTeams.containsKey(playerName)) {
            Teams.getTeamByName(playerTeams.get(playerName)).get().add(player);
        }
        event.setJoinMessage(null);
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            if (!player1.equals(player)) {
                player1.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.UI_PLAYER_JOIN, Teams.getTeamColorByPlayer(player) + player.getDisplayName() + ChatColor.YELLOW)).getMessage(player1.getLocale()));
            }
        }
        Bukkit.getConsoleSender().sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.UI_PLAYER_JOIN, Teams.getTeamColorByPlayer(player) + player.getDisplayName() + ChatColor.YELLOW)).getMessage(Locale.getDefault().toString()));

        player.sendMessage(ChatColor.STRIKETHROUGH + "--------" + ChatColor.AQUA + ChatColor.BOLD + " " + GameHandler.getGameHandler().getMatch().getLoadedMap().getName() + " " + ChatColor.RESET + ChatColor.STRIKETHROUGH + "--------");
        String line = "";
        if (GameHandler.getGameHandler().getMatch().getLoadedMap().getObjective().contains(" ")) {
            for (String word : GameHandler.getGameHandler().getMatch().getLoadedMap().getObjective().split(" ")) {
                line += word + " ";
                if (line.trim().length() > 32) {
                    line = ChatColor.BLUE + "" + ChatColor.ITALIC + line.trim();
                    player.sendMessage(" " + line);
                    line = "";
                }
            }
            if (!line.trim().equals("")) {
                line = ChatColor.BLUE + "" + ChatColor.ITALIC + line.trim();
                player.sendMessage(" " + line);
                line = "";
            }
        } else {
            line = ChatColor.BLUE + "" + ChatColor.ITALIC + GameHandler.getGameHandler().getMatch().getLoadedMap().getObjective();
            player.sendMessage(" " + line);
        }
        String locale = player.getLocale();
        String result = ChatColor.DARK_GRAY + "Created by ";
        List<Contributor> authors = GameHandler.getGameHandler().getMatch().getLoadedMap().getAuthors();
        for (Contributor author : authors) {
            if (authors.indexOf(author) < authors.size() - 2) {
                result = result + ChatColor.GRAY + author.getName() + ChatColor.DARK_GRAY + ", ";
            } else if (authors.indexOf(author) == authors.size() - 2) {
                result = result + ChatColor.GRAY + author.getName() + ChatColor.DARK_GRAY + " " + new LocalizedChatMessage(ChatConstant.MISC_AND).getMessage(locale) + " ";
            } else if (authors.indexOf(author) == authors.size() - 1) {
                result = result + ChatColor.GRAY + author.getName();
            }
        }
        if (result.contains(" ")) {
            for (String word : result.split(" ")) {
                line += word + " ";
                if (line.trim().length() > 32) {
                    line = line.trim();
                    player.sendMessage(ChatColor.DARK_GRAY + " " + line);
                    line = "";
                }
            }
            if (!line.trim().equals("")) {
                line = line.trim();
                player.sendMessage(ChatColor.DARK_GRAY + " " + line);
            }
        } else {
            line = result;
            player.sendMessage(ChatColor.DARK_GRAY + " " + line);
        }
        player.sendMessage(ChatColor.STRIKETHROUGH + "---------------------------");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            if (!player1.equals(player)) {
                player1.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.UI_PLAYER_LEAVE, Teams.getTeamColorByPlayer(player) + player.getDisplayName() + ChatColor.YELLOW)).getMessage(player1.getLocale()));
            }
        }
        Bukkit.getConsoleSender().sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.UI_PLAYER_LEAVE, Teams.getTeamColorByPlayer(player) + player.getDisplayName() + ChatColor.YELLOW)).getMessage(Locale.getDefault().toString()));
        removePlayer(player);
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.STRIKETHROUGH + "--------" + ChatColor.AQUA + ChatColor.BOLD + " " + GameHandler.getGameHandler().getMatch().getLoadedMap().getName() + " " + ChatColor.RESET + ChatColor.STRIKETHROUGH + "--------");
            String line = "";
            if (GameHandler.getGameHandler().getMatch().getLoadedMap().getObjective().contains(" ")) {
                for (String word : GameHandler.getGameHandler().getMatch().getLoadedMap().getObjective().split(" ")) {
                    line += word + " ";
                    if (line.trim().length() > 32) {
                        line = ChatColor.BLUE + "" + ChatColor.ITALIC + line.trim();
                        player.sendMessage(" " + line);
                        line = "";
                    }
                }
                if (!line.trim().equals("")) {
                    line = ChatColor.BLUE + "" + ChatColor.ITALIC + line.trim();
                    player.sendMessage(" " + line);
                    line = "";
                }
            } else {
                line = ChatColor.BLUE + "" + ChatColor.ITALIC + GameHandler.getGameHandler().getMatch().getLoadedMap().getObjective();
                player.sendMessage(" " + line);
            }
            String locale = player.getLocale();
            String result = ChatColor.DARK_GRAY + "Created by ";
            List<Contributor> authors = GameHandler.getGameHandler().getMatch().getLoadedMap().getAuthors();
            for (Contributor author : authors) {
                if (authors.indexOf(author) < authors.size() - 2) {
                    result = result + ChatColor.GRAY + author.getName() + ChatColor.DARK_GRAY + ", ";
                } else if (authors.indexOf(author) == authors.size() - 2) {
                    result = result + ChatColor.GRAY + author.getName() + ChatColor.DARK_GRAY + " " + new LocalizedChatMessage(ChatConstant.MISC_AND).getMessage(locale) + " ";
                } else if (authors.indexOf(author) == authors.size() - 1) {
                    result = result + ChatColor.GRAY + author.getName();
                }
            }
            if (result.contains(" ")) {
                for (String word : result.split(" ")) {
                    line += word + " ";
                    if (line.trim().length() > 32) {
                        line = line.trim();
                        player.sendMessage(ChatColor.DARK_GRAY + " " + line);
                        line = "";
                    }
                }
                if (!line.trim().equals("")) {
                    line = line.trim();
                    player.sendMessage(ChatColor.DARK_GRAY + " " + line);
                }
            } else {
                line = result;
                player.sendMessage(ChatColor.DARK_GRAY + " " + line);
            }
            player.sendMessage(ChatColor.STRIKETHROUGH + "---------------------------");
        }
    }

    @EventHandler
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (event.getNewTeam().isPresent() && !event.getNewTeam().get().isObserver() && GameHandler.getGameHandler().getMatch().isRunning()) {
            Bukkit.dispatchCommand(event.getPlayer(), "match");
        }
    }

    private void removePlayer(Player player) {
        for (TeamModule team : match.getModules().getModules(TeamModule.class)) {
            if (team.contains(player)) {
                team.remove(player);
            }
        }
        this.clearHeldAttribute(player);
    }

    private void clearHeldAttribute(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        if (player.getInventory().getItemInHand() != null && !player.getInventory().getItemInHand().getType().equals(Material.AIR)) {
            craftPlayer.getHandle().getAttributeMap().a(((CraftInventoryPlayer) craftPlayer.getInventory()).getInventory().getItemInHand().B());
        }
    }

}
