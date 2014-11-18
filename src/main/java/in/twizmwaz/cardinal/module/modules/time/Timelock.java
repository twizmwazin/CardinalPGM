package in.twizmwaz.cardinal.module.modules.time;

import in.twizmwaz.cardinal.module.Module;
import org.w3c.dom.Node;

/**
 * Created by kevin on 11/17/14.
 */
public class Timelock extends Module {

    private boolean timelock;

    public Timelock(Node node) {

        if (node.getTextContent().equalsIgnoreCase("on")) {
            this.timelock = true;
        } else this.timelock = false;
    }

    public boolean getEnabled() {
            return this.timelock;

    }

}
