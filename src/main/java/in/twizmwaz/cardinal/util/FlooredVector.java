package in.twizmwaz.cardinal.util;

import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class FlooredVector extends BlockVector {

    public FlooredVector(double x, double y, double z) {
        super(Math.floor(x), Math.floor(y), Math.floor(z));
    }

    public FlooredVector(float x, float y, float z) {
        super(Math.floor(x), Math.floor(y), Math.floor(z));
    }

    public FlooredVector(int x, int y, int z) {
        super(Math.floor(x), Math.floor(y), Math.floor(z));
    }

    public FlooredVector(Vector vec) {
        super(Math.floor(vec.getX()), Math.floor(vec.getY()), Math.floor(vec.getZ()));
    }

}
