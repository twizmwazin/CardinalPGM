package in.twizmwaz.cardinal.regions.parsers;

import in.twizmwaz.cardinal.util.NumUtils;
import org.jdom2.Element;

/**
 * Created by kevin on 10/26/14.
 */
public class CylinderParser {

    private double baseX;
    private double baseY;
    private double baseZ;
    private double radius;
    private double height;

    public CylinderParser(Element element) {
        this.radius = NumUtils.parseDouble(element.getAttribute("radius").getValue().replaceAll(" ", ""));
        this.height = NumUtils.parseDouble(element.getAttribute("height").getValue().replaceAll(" ", ""));
        String[] split = element.getAttribute("base").getValue().replaceAll(" ", "").split(",");
        this.baseX = NumUtils.parseDouble(split[0]);
        this.baseY = NumUtils.parseDouble(split[1]);
        this.baseZ = NumUtils.parseDouble(split[2]);
    }

    public double getBaseX() {
        return baseX;
    }

    public double getBaseY() {
        return baseY;
    }

    public double getBaseZ() {
        return baseZ;
    }

    public double getRadius() {
        return radius;
    }

    public double getHeight() {
        return height;
    }

}
