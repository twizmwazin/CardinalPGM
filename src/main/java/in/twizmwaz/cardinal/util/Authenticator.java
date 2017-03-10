package in.twizmwaz.cardinal.util;

import com.google.common.collect.Maps;
import in.twizmwaz.cardinal.module.modules.teamRegister.TeamRegisterModule;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class Authenticator {

    private static Map<String, String> teamDocuments = Maps.newHashMap();

    public static boolean authenticateTeam(UUID player, String team) {
        Pattern regex = Pattern.compile(Config.playerRegex
                .replace("{team}", TeamRegisterModule.filterTeam(team))
                .replace("{uuid}", TeamRegisterModule.filterUuid(player.toString())));
        return teamDocuments.containsKey(team) && regex.matcher(teamDocuments.get(team)).find();
    }
    public static void downloadTeamDocument(String teamName) throws IOException {
        Document doc = Jsoup.connect(Config.teamUrl.replace("{team}", TeamRegisterModule.filterTeam(teamName))).get();
        teamDocuments.put(teamName, doc.toString());
    }

    public static void removeTeamDocument(String teamName) {
        teamDocuments.remove(teamName);
    }

}
