package in.twizmwaz.cardinal.module.modules.stats;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.matchTranscript.MatchTranscript;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.*;

public class Stats implements Module {


    private List<MatchTracker> stats;
    private HashMap<OfflinePlayer, TeamModule> playerTeams = new HashMap<>();

    private File transcript;

    protected Stats() {
        stats = new ArrayList<>();
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public void add(MatchTracker tracker) {
        stats.add(tracker);
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        this.add(new MatchTracker(event.getPlayer(), event.getKiller(), event.getPlayer().getItemInHand()));
    }

    public int getKillsByPlayer(OfflinePlayer player) {
        int kills = 0;
        if (player == null) return 0;
        for (MatchTracker tracker : this.stats) {
            if (tracker.getKiller() != null && tracker.getKiller().equals(player)) {
                kills ++;
            }
        }
        return kills;
    }

    public int getDeathsByPlayer(OfflinePlayer player) {
        int deaths = 0;
        if (player == null) return 0;
        for (MatchTracker tracker : this.stats) {
            if (tracker.getPlayer().equals(player)) {
                deaths ++;
            }
        }
        return deaths;
    }

    public int getTotalKills() {
        int kills = 0;
        for (MatchTracker tracker : this.stats) {
            if (tracker.getKiller() != null) {
                kills ++;
            }
        }
        return kills;
    }

    public int getTotalDeaths() {
        int deaths = 0;
        for (MatchTracker tracker : this.stats) {
                deaths ++;
        }
        return deaths;
    }

    public double getKdByPlayer(OfflinePlayer player) {
        double kd;
        if (player == null) return 0;
        kd = getDeathsByPlayer(player) == 0 ? (double) getKillsByPlayer(player) : (double) getKillsByPlayer(player) / (double) getDeathsByPlayer(player);
        return kd;
    }

    /**
     * Sends player stats to player
     */
    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Settings.getSettingByName("Stats") != null && Settings.getSettingByName("Stats").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
                player.sendMessage(ChatColor.GRAY + "Kills: " + ChatColor.GREEN + getKillsByPlayer(player) + ChatColor.AQUA + " | " + ChatColor.GRAY + "Deaths: " + ChatColor.DARK_RED + getDeathsByPlayer(player) + ChatColor.AQUA + " | " + ChatColor.GRAY + "KD: " + ChatColor.GOLD + String.format("%.2f", getKdByPlayer(player)));
            }
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(GameHandler.getGameHandler().getPlugin(), new Runnable() {
            public void run() {
                Bukkit.broadcastMessage(ChatColor.GOLD + "Uploading stats...");
                Bukkit.broadcastMessage(ChatColor.GREEN + uploadStats());
            }
        }, 20);
    }

    /**
     * Handling of uploading stats
     */

    @EventHandler
    public void onPlayerJoinTeam(PlayerChangeTeamEvent event) {
        this.playerTeams.put(event.getPlayer(), event.getNewTeam());
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        transcript = GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTranscript.class).getLogFile();
        try {
            File template = new File(GameHandler.getGameHandler().getMatchFile() + "/statistics.html");
            OutputStream out = new FileOutputStream(template);
            IOUtils.copy(GameHandler.getGameHandler().getPlugin().getResource("statistics.html"), out);
            out.close();
        } catch (IOException e) {
            Bukkit.getLogger().warning("Unable to copy template statistics file");
            e.printStackTrace();
        }
    }



    private String generateStats() throws IOException {
        File file = new File(GameHandler.getGameHandler().getMatchFile() + "/statistics.html");
        Document document = Jsoup.parse(file, "utf-8");
        for (Element element : document.getElementsContainingOwnText("%mapName")) {
            element.text(element.text().replace("%mapName", GameHandler.getGameHandler().getMatch().getLoadedMap().getName()));
        }
        for (Element element : document.getElementsContainingOwnText("%date")) {
            element.text(element.text().replace("%date", new Date().toString()));
        }
        for (Element element : document.getElementsContainingOwnText("%kills")) {
            element.text(element.text().replace("%kills", Integer.toString(getTotalKills())));
        }
        for (Element element : document.getElementsContainingOwnText("%deaths")) {
            element.text(element.text().replace("%deaths", Integer.toString(getTotalDeaths())));
        }
        for (Element element : document.getElementsContainingOwnText("%matchTime")) {
            element.text(element.text().replace("%matchTime", Double.toString(GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTimer.class).getEndTime())));
        }
        Element teams = document.getElementById("teams");
            for (TeamModule team : TeamUtils.getTeams()) {
                teams.appendElement("h3").text(team.getName());
                for (Map.Entry<OfflinePlayer, TeamModule> entry : playerTeams.entrySet()) {
                    if (entry.getValue().getName() == team.getName()) {
                        if (!team.isObserver())
                            teams.appendElement("p").text(entry.getKey().getName() + ": Kills: " + getKillsByPlayer(entry.getKey()) + ", Deaths: " + getDeathsByPlayer(entry.getKey()) + ", KD: " + Math.round(getKdByPlayer(entry.getKey())));
                        else teams.appendElement("p").text(entry.getKey().getName());
                    }
                }
            }
        Element transcript = document.getElementById("transcript");
        if (GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTranscript.class).getLog() != null)
            transcript.appendElement("pre").text(GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTranscript.class).getLog());
        Writer writer = new PrintWriter(file);
        writer.write(document.html());
        writer.close();
        return document.html();
    }

    public String uploadStats() {
        try {
            /**
             * Current uploads to pastehtml, needs to editied to upload to twiz's site
             */
            URL url = new URL("http://pastehtml.com/upload/create?input_type=txt&result=address");
            String postData = URLEncoder.encode("txt", "UTF-8") + "=" + URLEncoder.encode(generateStats(), "UTF-8");

            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = br.readLine();

            br.close();

            return response;
        } catch (Exception e) {
            Bukkit.getLogger().warning("Unable to upload statistics");
            e.printStackTrace();
            return null;
        }
    }



}
