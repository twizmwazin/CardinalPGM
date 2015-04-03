package in.twizmwaz.cardinal.module.modules.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.chatChannels.ChatChannelModule;
import in.twizmwaz.cardinal.module.modules.chatChannels.GlobalChannel;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.matchTranscript.MatchTranscript;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Stats implements Module {


    private List<MatchTracker> stats;
    private Map<OfflinePlayer, TeamModule> playerTeams = Maps.newHashMap();
    private File transcript;

    protected Stats() {
        stats = Lists.newArrayList();
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
                kills++;
            }
        }
        return kills;
    }

    public int getDeathsByPlayer(OfflinePlayer player) {
        int deaths = 0;
        if (player == null) return 0;
        for (MatchTracker tracker : this.stats) {
            if (tracker.getPlayer().equals(player)) {
                deaths++;
            }
        }
        return deaths;
    }

    public int getTotalKills() {
        int kills = 0;
        for (MatchTracker tracker : this.stats) {
            if (tracker.getKiller() != null) {
                kills++;
            }
        }
        return kills;
    }

    public int getTotalDeaths() {
        int deaths = 0;
        for (MatchTracker tracker : this.stats) {
            deaths++;
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
                ChatChannelModule global = GameHandler.getGameHandler().getMatch().getModules().getModule(GlobalChannel.class);
                global.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GOLD + "{0}", ChatConstant.UI_MATCH_REPORT_UPLOAD.asMessage()));
                String result = uploadStats();
                if (result == null || result.contains("error"))
                    global.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", ChatConstant.UI_MATCH_REPORT_FAILED.asMessage()));
                else global.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", ChatConstant.UI_MATCH_REPORT_SUCCESS.asMessage(new UnlocalizedChatMessage(result))));
            }
        }, 20);
    }

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


    private File generateStats() throws IOException {
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
                if (entry.getValue() == team) {
                    if (!team.isObserver()) {
                        teams.appendElement("p").text(entry.getKey().getName() + ": Kills: " + getKillsByPlayer(entry.getKey()) + ", Deaths: " + getDeathsByPlayer(entry.getKey()) + ", KD: " + Math.round(getKdByPlayer(entry.getKey()))).attr("class", "media-body");
                    }
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
        return file;
    }

    public String uploadStats() {
        try {
            HttpPost post = new HttpPost("http://m.twizmwaz.in/uploadmatch.php");
            NameValuePair id = new BasicNameValuePair("id", GameHandler.getGameHandler().getMatch().getUuid().toString().replaceAll("-", ""));
            MultipartEntityBuilder fileBuilder = MultipartEntityBuilder.create().addBinaryBody("match", generateStats());
            fileBuilder.addPart(id.getName(), new StringBody(id.getValue(), ContentType.TEXT_HTML));
            post.setEntity(fileBuilder.build());
            HttpClient client = HttpClientBuilder.create().build();
            return EntityUtils.toString(client.execute(post).getEntity());
        } catch (Exception e) {
            Bukkit.getLogger().warning("Unable to upload statistics");
            e.printStackTrace();
            return null;
        }
    }
}
