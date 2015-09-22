package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.ctf.Flag;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.List;

public class Flags {

    public static List<Flag> getFlags() {
        return GameHandler.getGameHandler().getMatch().getModules().getModules(Flag.class);
    }

    public static Flag getFlag(Post post) {
        for (Flag flag : getFlags()) {
            if (flag.getPost().equals(post)) return flag;
        }
        return null;
    }

    public static Flag getFlag(Net net) {
        for (Flag flag : getFlags()) {
            if (flag.getNets().contains(net)) return flag;
        }
        return  null;
    }

    public static boolean hasFlag(Player player) {
        for (Flag flag : getFlags()) {
            if (flag.getPicker() != null && flag.getPicker().equals(player)) return true;
        }
        return false;
    }

    public static Flag getFlag(Player player) {
        for (Flag flag : getFlags()) {
            if (flag.getPicker() != null && flag.getPicker().equals(player)) return flag;
        }
        return null;
    }

    public static void setBannerFacing(float yaw, Banner banner, boolean update) {
        org.bukkit.material.Banner data = (org.bukkit.material.Banner) banner.getMaterialData();
        data.setFacingDirection(yawToFace(yaw));
        banner.setMaterialData(data);
        if (update) banner.update();
    }

    private static BlockFace yawToFace(float yaw) {
        BlockFace[] faces = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };
        return faces[Math.round(yaw / 45f) & 0x7];
    }


}
