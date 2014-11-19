package in.twizmwaz.cardinal.regions.parsers;

import org.jdom2.Element;

/**
 * Created by kevin on 10/26/14.
 */
public class SphereParser {

    private String origin;
    private String rad;
    private double originx;
    private double originy;
    private double originz;
    private double radius;

    public SphereParser(Element element) {
        this.origin = element.getAttribute("origin").getValue();
        this.rad = element.getAttribute("radius").getValue();
        String[] origina = this.origin.split(",");
        this.originx = Double.parseDouble(origina[0]);
        this.originy = Double.parseDouble(origina[1]);
        this.originz = Double.parseDouble(origina[2]);

        this.radius = Double.parseDouble(rad);
    }

    public double getOriginx() {
        return originx;
    }

    public double getOriginy() {
        return originy;
    }

    public double getOriginz() {
        return originz;
    }

    public double getRadius() {
        return radius;
    }

}
