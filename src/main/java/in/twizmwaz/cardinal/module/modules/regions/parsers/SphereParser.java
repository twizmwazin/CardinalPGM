package in.twizmwaz.cardinal.module.modules.regions.parsers;

import org.w3c.dom.Node;

/**
 * Created by kevin on 10/26/14.
 */
public class SphereParser {

    private String name;
    private String origin;
    private String rad;
    private double originx;
    private double originy;
    private double originz;
    private double radius;

    public SphereParser(Node node) {
        this.name = node.getAttributes().getNamedItem("name").getNodeValue();
        this.origin = node.getAttributes().getNamedItem("origin").getNodeValue();
        this.rad = node.getAttributes().getNamedItem("radius").getNodeValue();

        String[] origina = this.origin.split(",");
        this.originx = Double.parseDouble(origina[0]);
        this.originy = Double.parseDouble(origina[1]);
        this.originz = Double.parseDouble(origina[2]);

        this.radius = Double.parseDouble(rad);
    }

    public String getName() {
        return  name;
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
