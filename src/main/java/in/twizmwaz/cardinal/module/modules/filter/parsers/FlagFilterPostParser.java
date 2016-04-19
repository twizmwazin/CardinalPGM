package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import in.twizmwaz.cardinal.util.Flags;
import org.jdom2.Element;

public class FlagFilterPostParser extends FlagFilterParser {

    String id;
    private Post post;

    public FlagFilterPostParser(Element element) {
        super(element);
        this.id = element.getAttributeValue("post");
    }

    public Post getPost() {
        if (post == null) this.post = Flags.getPostById(id);
        return post;
    }

}
