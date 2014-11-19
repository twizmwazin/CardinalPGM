package in.twizmwaz.cardinal.regions.parsers;

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
        this.radius = Double.parseDouble(element.getAttribute("radius").getValue());
        this.height = Double.parseDouble(element.getAttribute("height").getValue());

        String[] split = element.getAttribute("base").getValue().split(",");
        this.baseX = Double.parseDouble(split[0]);
        this.baseY = Double.parseDouble(split[1]);
        this.baseZ = Double.parseDouble(split[2]);
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
