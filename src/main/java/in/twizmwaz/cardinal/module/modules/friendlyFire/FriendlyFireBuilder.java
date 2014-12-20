package in.twizmwaz.cardinal.module.modules.friendlyFire;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;

import java.util.ArrayList;
import java.util.List;

public class FriendlyFireBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> results = new ArrayList<>(1);
        boolean enabled = true;
        boolean arrowReturn = true;
        try {
            if (match.getDocument().getRootElement().getChild("friendlyfire").getText().equalsIgnoreCase("on")) {
                enabled = false;
            }
        } catch (NullPointerException e) {

        }
        try {
            if (match.getDocument().getRootElement().getChild("friendlyfirerefund").getText().equalsIgnoreCase("off")) {
                arrowReturn = false;
            }
        } catch (NullPointerException e) {

        }
        results.add(new FriendlyFire(match, enabled, arrowReturn));
        return results;
    }

}
