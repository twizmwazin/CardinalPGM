package in.twizmwaz.cardinal.util;

import com.google.common.collect.Maps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

public class Authenticator {

    static Map<String, String> teamsDocuments = Maps.newHashMap();

    public static boolean authenticateTeam(String p, String team) {
        if (teamsDocuments.containsKey(team) && teamsDocuments.get(team).contains("avatar.oc.tc/" + p + "/")) {
            return true;
        }
        return false;
    }
    public static void downloadTeamDocument(String team) throws IOException {
        if (teamsDocuments.containsKey(team)) {
            return;
        } else {
            Document doc = Jsoup.connect("http://oc.tc/teams/" + team.toLowerCase().replace(" ", "").replace("_", "")).get();
            Document doc2 = Jsoup.connect("http://oc.tc/teams/" + team.toLowerCase().replace(" ", "").replace("_", "") + "?page=2").get();
            if (doc.toString() == doc2.toString()) {
                teamsDocuments.put(team, doc.toString());
            } else if (doc.toString() != doc2.toString()) {
                teamsDocuments.put(team, doc.toString() + doc2.toString());
            }
        }
    }

}
