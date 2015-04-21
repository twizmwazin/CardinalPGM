package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class PointParser extends RegionParser {

    private final Vector vector;
    private float yaw, pitch;

    public PointParser(Element element) {
        super(element.getAttributeValue("name"));
        double x, y, z;
        if (element.getChildren().size() > 0) {
            PointRegion subPoint = RegionModuleBuilder.getRegion(element.getChildren().get(0)).getRandomPoint();
            x = subPoint.getX();
            y = subPoint.getY();
            z = subPoint.getZ();
        } else {
            if (element.getText().contains(",")) {
                x = NumUtils.parseDouble(element.getText().split(",")[0].trim());
                y = NumUtils.parseDouble(element.getText().split(",")[1].trim());
                z = NumUtils.parseDouble(element.getText().split(",")[2].trim());
            } else {
                x = NumUtils.parseDouble(element.getText().trim().replaceAll(" ", ",").split(",")[0]);
                y = NumUtils.parseDouble(element.getText().trim().replaceAll(" ", ",").split(",")[1]);
                z = NumUtils.parseDouble(element.getText().trim().replaceAll(" ", ",").split(",")[2]);
            }
        }
        this.vector = new Vector(x, y, z);
        try {
            yaw = Float.parseFloat(element.getAttributeValue("yaw").trim());
        } catch (Exception e) {
            yaw = 0F;
        }
        try {
            pitch = Float.parseFloat(element.getAttributeValue("pitch").trim());
        } catch (Exception e) {
            pitch = 0F;
        }
    }

    public double getX() {
        return vector.getX();
    }

    public double getY() {
        return vector.getY();
    }

    public double getZ() {
        return vector.getZ();
    }

    public Vector getVector() {
        return vector;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
