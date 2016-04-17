package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.Numbers;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class HalfParser extends RegionParser{

  private final Vector origin;
  private final Vector normal;

  public HalfParser(Element element) {
    super(element.getAttributeValue("name") != null ? element.getAttributeValue("name") : element.getAttributeValue("id"));
    String[] origin = element.getAttributeValue("origin").replace(" ", "").split(",", 3);
    String[] normal = element.getAttributeValue("normal").replace(" ", "").split(",", 3);
    this.origin = new Vector(Numbers.parseDouble(origin[0]), Numbers.parseDouble(origin[1]), Numbers.parseDouble(origin[2]));
    this.normal = new Vector(Numbers.parseDouble(normal[0]), Numbers.parseDouble(normal[1]), Numbers.parseDouble(normal[2]));
  }

  public Vector getOrigin() {
    return origin;
  }

  public Vector getNormal() {
    return normal;
  }

}
