package in.twizmwaz.cardinal.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GitUtil {

    public static String getLatestGitRevision() {
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new InputStreamReader(new URL("https://api.github.com/repos/twizmwazin/CardinalPGM/git/refs/heads/master").openStream()));
            return jsonElement.getAsJsonObject().getAsJsonObject("object").get("sha").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUpdateMessage(String url) throws IOException {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(new InputStreamReader(new URL(url).openStream()));
        return jsonElement.toString();
    }
}
