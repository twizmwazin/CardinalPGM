package in.twizmwaz.cardinal.module.modules.hill;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class RegionSave {

    private final BlockVector min;
    private final BlockVector size;
    private final List<Pair<Material,Byte>> blocks;

    public RegionSave(RegionModule region) {
        BlockVector min = blockAlign(region.getMin());
        BlockVector max = blockAlign(region.getMax());
        BlockVector size = max.minus(min).toBlockVector();

        this.min = min;
        this.size = size;

        List<Pair<Material,Byte>> blocks = new ArrayList<>();
        for (int z = min.getBlockZ(); z < max.getBlockZ(); z++) {
            for (int y = min.getBlockY(); y < max.getBlockY(); y++) {
                for (int x = min.getBlockX(); x < max.getBlockX(); x++) {
                    Block block = new Location(GameHandler.getGameHandler().getMatchWorld(), x, y, z).getBlock();
                    blocks.add(new ImmutablePair<>(block.getType(), block.getData()));
                }
            }
        }
        this.blocks = blocks;
    }

    public BlockVector blockAlign(Vector vector) {
        return new BlockVector((int) vector.getX() + 0.5d, (int) vector.getY() + 0.5d, (int) vector.getZ() + 0.5d);
    }

    public Pair<Material,Byte> getBlockAt(BlockVector loc) {
        int x = loc.getBlockX() - min.getBlockX();
        int y = (loc.getBlockY() - this.min.getBlockY()) * this.size.getBlockX();
        int z = (loc.getBlockZ() - this.min.getBlockZ()) * this.size.getBlockX() * this.size.getBlockY();
        return blocks.get(x + y + z);
    }

}
