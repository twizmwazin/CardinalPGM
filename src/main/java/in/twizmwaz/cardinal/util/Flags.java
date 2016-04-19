package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Flags {

    public static List<FlagObjective> getFlags() {
        return GameHandler.getGameHandler().getMatch().getModules().getModules(FlagObjective.class);
    }

    public static List<Post> getPosts() {
        return GameHandler.getGameHandler().getMatch().getModules().getModules(Post.class);
    }

    public static List<Net> getNets() {
        return GameHandler.getGameHandler().getMatch().getModules().getModules(Net.class);
    }

    public static FlagObjective getFlagById(String id) {
        for (FlagObjective flagObjective : getFlags()) {
            if (flagObjective.getId().equals(id)) return flagObjective;
        }
        return null;
    }

    public static Post getPostById(String id) {
        if (id == null) return null;
        for (Post post : getPosts()) {
            if (post.getId().equals(id)) return post;
        }
        return null;
    }

    public static Set<Net> getNetsByFlag(FlagObjective flag) {
        Set<Net> nets = new HashSet<>();
        for (Net net : getNets()) {
            if (net.getFlags().contains(flag)) nets.add(net);
        }
        return nets;
    }

    public static FlagObjective getFlag(Post post) {
        for (FlagObjective flagObjective : getFlags()) {
            if (flagObjective.getPost().equals(post)) return flagObjective;
        }
        return null;
    }

    public static Set<FlagObjective> getFlag(Net net) {
        return net.getFlags();
    }

    public static boolean hasFlag(Player player) {
        for (FlagObjective flagObjective : getFlags()) {
            if (flagObjective.getPicker() != null && flagObjective.getPicker().equals(player)) return true;
        }
        return false;
    }

    public static FlagObjective getFlag(Player player) {
        for (FlagObjective flagObjective : getFlags()) {
            if (flagObjective.getPicker() != null && flagObjective.getPicker().equals(player)) return flagObjective;
        }
        return null;
    }

    public static void setBannerFacing(BlockFace face, Banner banner) {
        org.bukkit.material.Banner data = (org.bukkit.material.Banner) banner.getMaterialData();
        data.setFacingDirection(face);
        banner.setMaterialData(data);
        banner.update();
    }

    public static BlockFace yawToFace(float yaw) {
        BlockFace[] RADIAL = {
                BlockFace.NORTH, BlockFace.NORTH_NORTH_EAST, BlockFace.NORTH_EAST, BlockFace.EAST_NORTH_EAST,
                BlockFace.EAST, BlockFace.EAST_SOUTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_SOUTH_EAST,
                BlockFace.SOUTH, BlockFace.SOUTH_SOUTH_WEST, BlockFace.SOUTH_WEST, BlockFace.WEST_SOUTH_WEST,
                BlockFace.WEST, BlockFace.WEST_NORTH_WEST, BlockFace.NORTH_WEST, BlockFace.NORTH_NORTH_WEST
        };
        int i = Math.round((yaw + 360f)/ 22.5f);
        return RADIAL[(i + 8)% 16];
    }


}
