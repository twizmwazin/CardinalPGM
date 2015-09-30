package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.flag.FlagDropEvent;
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.List;

public class Flags {

    public static List<FlagObjective> getFlags() {
        return GameHandler.getGameHandler().getMatch().getModules().getModules(FlagObjective.class);
    }

    public static FlagObjective getFlagById(String id) {
        for (FlagObjective flagObjective : getFlags()) {
            if (flagObjective.getId().equals(id)) return flagObjective;
        }
        return null;
    }

    public static FlagObjective getFlag(Post post) {
        for (FlagObjective flagObjective : getFlags()) {
            if (flagObjective.getPost().equals(post)) return flagObjective;
        }
        return null;
    }

    public static FlagObjective getFlag(Net net) {
        for (FlagObjective flagObjective : getFlags()) {
            if (flagObjective.getNets().contains(net)) return flagObjective;
        }
        return  null;
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
        BlockFace[] RADIAL = {BlockFace.WEST, BlockFace.NORTH_WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST};
        return RADIAL[Math.round(yaw / 45f) & 0x7];
    }


}
