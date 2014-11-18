package in.twizmwaz.cardinal.module.modules.time;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.StringUtils;
import org.bukkit.Bukkit;
import org.w3c.dom.Node;

import java.text.ParseException;
import java.util.logging.Level;

/**
 * Created by kevin on 11/17/14.
 */
public class Time extends Module {

    private int time; //in seconds
    private String result;

    public Time(Node node) {
        try {
            this.time = StringUtils.timeStringToSeconds(node.getTextContent());
            this.result = node.getAttributes().getNamedItem("result").getTextContent();
        } catch (ParseException ex) {
            Bukkit.getLogger().log(Level.WARNING, "Could not parse time correctly.");
        } catch (NullPointerException ex) {
            Bukkit.getLogger().log(Level.WARNING, "Could not parse time correctly.");
        }

    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
