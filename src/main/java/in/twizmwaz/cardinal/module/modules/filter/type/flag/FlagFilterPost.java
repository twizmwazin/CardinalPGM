package in.twizmwaz.cardinal.module.modules.filter.type.flag;

import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.FlagFilterPostParser;

public class FlagFilterPost extends FlagFilter {

    private Post post;
    private FlagFilterPostParser parser;

    public FlagFilterPost(FlagFilterPostParser parser) {
        super(parser);
        this.parser = parser;
    }

    public Post getPost() {
        if (this.post == null) this.post = parser.getPost();
        return post;
    }

    @Override
    public FilterState evaluate(Object... objects) {
        return FilterState.ABSTAIN;
    }

}
