package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.regions.parsers.PointParser;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class PointRegion extends BlockRegion {

    private final Vector vector, look;

    public PointRegion(String name, Vector vector, Vector look) {
        super(name, vector);
        this.vector = vector;
        this.look = look;
    }

    public PointRegion(String name, double x, double y, double z) {
        super(name, x, y, z);
        this.vector = new BlockVector(x, y, z);
        this.look = new Vector();
    }

    public PointRegion(PointParser parser) {
        this(parser.getName(), parser.getVector(), parser.getLook());
    }

    public PointRegion(String name, Vector vector) {
        this(name, vector, new Vector());
    }

    public float getYaw() {
        return vector.toLocation(null).getYaw();
    }

    public float getPitch() {
        return vector.toLocation(null).getPitch();
    }

    public Block getBlock() {
        return vector.toLocation(GameHandler.getGameHandler().getMatchWorld()).getBlock();
    }

    public Vector getLook() {
        return look;
    }
}
