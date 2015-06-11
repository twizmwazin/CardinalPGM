package in.twizmwaz.cardinal.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class MojangUtils {

    public static String getNameByUUID(UUID uuid) {
        try {
            JSONObject response = (JSONObject) new JSONParser().parse(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "")).openStream()));
            return (String) response.get("name");
        } catch (IOException | ParseException e) {
            return null;
        }
    }

}
