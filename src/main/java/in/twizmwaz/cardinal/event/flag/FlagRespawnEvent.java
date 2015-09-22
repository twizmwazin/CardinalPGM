package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.Flag;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FlagRespawnEvent extends FlagEvent {

    private Flag flag;
    private Post post;
    private Block where;

    public FlagRespawnEvent(Flag flag, Post post, Block where) {
        this.flag = flag;
        this.post = post;
        this.where = where;
    }

    @Override
    public Flag getFlag() {
        return flag;
    }

    public Post getPost() {
        return post;
    }

    public Block getBlock() {
        return where;
    }

}
