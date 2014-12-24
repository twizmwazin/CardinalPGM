package in.twizmwaz.cardinal.regions.parsers;

import in.twizmwaz.cardinal.util.NumUtils;
import org.jdom2.Element;

/**
 * Created by kevin on 10/26/14.
 */
public class CircleParser {

    private double centerX;
    private double centerZ;
    private double radius;

    public CircleParser(Element element) {
        String[] centArray = element.getAttribute("center").getValue().split(",");
        this.centerX = NumUtils.parseDouble(centArray[0]);
        this.centerZ = NumUtils.parseDouble(centArray[1]);
        this.radius = NumUtils.parseDouble(element.getAttribute("radius").getValue());

    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterZ() {
        return centerZ;
    }

    public double getRadius() {
        return radius;
    }
}
