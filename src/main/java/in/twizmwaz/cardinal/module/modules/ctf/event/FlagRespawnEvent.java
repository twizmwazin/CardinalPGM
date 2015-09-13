package in.twizmwaz.cardinal.module.modules.ctf.event;

import in.twizmwaz.cardinal.module.modules.ctf.Flag;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FlagRespawnEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Flag flag;
    private Post post;

    public FlagRespawnEvent(Flag flag, Post post) {
        this.flag = flag;
        this.post = post;
    }

    public Flag getFlag() {
        return flag;
    }

    public Post getPost() {
        return post;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
