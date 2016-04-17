package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.HalfParser;
import in.twizmwaz.cardinal.util.Numbers;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class HalfRegion extends RegionModule {

  private static double HALF_PI = Math.PI/2;

  private final Vector origin;
  private final Vector normal;

  public HalfRegion(String name, Vector origin, Vector normal) {
    super(name);
    this.origin = origin;
    this.normal = normal;
  }

  public HalfRegion(HalfParser parser) {
    this(parser.getName(), parser.getOrigin(), parser.getNormal());
  }

  @Override
  public boolean contains(Vector vector) {
    return this.normal.angle(vector.minus(origin)) <= HALF_PI;
  }

  @Override
  public PointRegion getRandomPoint() {
    while (true) {
      double
          x = Numbers.getRandom(Double.MIN_VALUE, Double.MAX_VALUE),
          y = Numbers.getRandom(Double.MIN_VALUE, Double.MAX_VALUE),
          z = Numbers.getRandom(Double.MIN_VALUE, Double.MAX_VALUE);
      if (contains(new Vector(x, y, z))) return new PointRegion(null, x, y, z);
    }
  }

  @Override
  public BlockRegion getCenterBlock() {
    return new BlockRegion(null, getMin().midpoint(getMax()));
  }

  @Override
  public List<Block> getBlocks() {
    return new ArrayList<>();
  }

  @Override
  public Vector getMin() {
    return new Vector(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
  }

  @Override
  public Vector getMax() {
    return new Vector(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
  }

}
