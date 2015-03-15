package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.RectangleParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class RectangleRegion extends CuboidRegion {

    public RectangleRegion(String name, double xMin, double zMin, double xMax, double zMax) {
        super(name, new BlockVector(xMin, Double.NEGATIVE_INFINITY, zMin), new BlockVector(xMax, Double.POSITIVE_INFINITY, zMax));
    }

    public RectangleRegion(RectangleParser parser) {
        this(parser.getName(), parser.getXMin(), parser.getZMin(), parser.getXMax(), parser.getZMax());
    }

    public double getXMin() {
        return super.getXMin();
    }

    public double getZMin() {
        return super.getZMin();
    }

    public double getXMax() {
        return super.getXMax();
    }

    public double getZMax() {
        return super.getZMax();
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        for (int x = (int) getXMin(); x <= getXMax(); x++) {
            for (int z = (int) getZMin(); z <= getZMax(); z++) {
                for (int y = 0; y <= 256; y++) {
                    results.add((new Location(GameHandler.getGameHandler().getMatchWorld(), x, y, z).getBlock()));
                }
            }
        }
        return results;
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(null, new Vector(getXMin(), 0, getXMax()).midpoint(new Vector(getXMax(), 256, getZMax())));
    }

}
