package in.twizmwaz.cardinal.regions.type;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.regions.parsers.RectangleParser;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class RectangleRegion extends CuboidRegion {


    public RectangleRegion(String name, double xMin, double zMin, double xMax, double zMax) {
        super(xMin, Double.POSITIVE_INFINITY, zMin, xMax, Double.NEGATIVE_INFINITY, zMax);
    }

    public RectangleRegion(RectangleParser parser) {
        super(parser.getXMin(), Double.NEGATIVE_INFINITY, parser.getZMin(), parser.getXMax(), Double.POSITIVE_INFINITY, parser.getZMax());
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
}
