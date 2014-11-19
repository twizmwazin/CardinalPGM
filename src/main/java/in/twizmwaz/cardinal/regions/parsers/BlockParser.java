package in.twizmwaz.cardinal.regions.parsers;


import org.jdom2.Element;

/**
 * Created by kevin on 10/26/14.
 */
public class BlockParser {

    private double x;
    private double y;
    private double z;

    public BlockParser(Element element) {
        try {
            String[] coords = element.getAttribute("location").getValue().split(",");
            this.x = Double.parseDouble(coords[0]);
            this.y = Double.parseDouble(coords[1]);
            this.z = Double.parseDouble(coords[2]);
        } catch (NullPointerException ex) {
            String[] coords = element.getText().split(",");
            this.x = Double.parseDouble(coords[0]);
            this.y = Double.parseDouble(coords[1]);
            this.z = Double.parseDouble(coords[2]);

        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

}
