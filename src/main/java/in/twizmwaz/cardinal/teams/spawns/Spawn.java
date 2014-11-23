package in.twizmwaz.cardinal.teams.spawns;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.point.PointRegion;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by kevin on 11/21/14.
 */
public class Spawn {

    private Element element;
    private PgmTeam team;
    private int yaw;
    private List<Region> regions = new ArrayList<Region>();
    private boolean safe;
    private boolean sequential;


    public Spawn(Element element) {
        for (Element child : element.getChildren()) {
            regions.add(Region.newRegion(element));
        }
        this.yaw = Integer.parseInt(element.getAttributeValue("yaw"));
        try {
            this.team = GameHandler.getGameHandler().getMatch().getTeamByName(element.getAttributeValue("team"));
        } catch (NullPointerException ex) {

        }
    }

    public PointRegion getPoint() {
        Random random = new Random();
        int index = random.nextInt(regions.size());
        PointRegion point = regions.get(index).getRandomPoint();
        return new PointRegion(point.getX(), point.getY(), point.getZ(), this.yaw, 0);
    }


}
