package in.twizmwaz.cardinal.demographics;

import in.twizmwaz.cardinal.Database;
import org.bukkit.entity.Player;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.net.URL;

public class LookupService implements Runnable {
    
    private final Player player;
    private final Database database;
    
    public LookupService(Player player, Database database) {
        this.player = player;
        this.database = database;
    }
    
    @Override
    public void run() {
        database.put(player, "lang", player.getLocale());
        try {
            Document data = new SAXBuilder().build(new URL("https://freegeoip.net/xml/" + player.getAddress().getAddress().toString()));
            database.put(player, "country", data.getRootElement().getChildText("CountryCode"));
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }
}
