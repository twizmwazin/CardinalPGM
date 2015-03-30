package in.twizmwaz.cardinal.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonParser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GitUtils {

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
