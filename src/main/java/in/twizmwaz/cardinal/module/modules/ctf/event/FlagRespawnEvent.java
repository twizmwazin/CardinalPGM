package in.twizmwaz.cardinal.module.modules.ctf.event;

import in.twizmwaz.cardinal.module.modules.ctf.Flag;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FlagRespawnEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Flag flag;
    private Post post;
    private Block where;

    public FlagRespawnEvent(Flag flag, Post post, Block where) {
        this.flag = flag;
        this.post = post;
        this.where = where;
    }

    public Flag getFlag() {
        return flag;
    }

    public Post getPost() {
        return post;
    }

    public Block getBlock() {
        return where;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
