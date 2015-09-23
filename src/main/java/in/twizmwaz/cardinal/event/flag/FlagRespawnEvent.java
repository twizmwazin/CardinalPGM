package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import org.bukkit.block.Block;

public class FlagRespawnEvent extends FlagEvent {

    private FlagObjective flagObjective;
    private Post post;
    private Block where;

    public FlagRespawnEvent(FlagObjective flagObjective, Post post, Block where) {
        this.flagObjective = flagObjective;
        this.post = post;
        this.where = where;
    }

    @Override
    public FlagObjective getFlagObjective() {
        return flagObjective;
    }

    public Post getPost() {
        return post;
    }

    public Block getBlock() {
        return where;
    }

}
