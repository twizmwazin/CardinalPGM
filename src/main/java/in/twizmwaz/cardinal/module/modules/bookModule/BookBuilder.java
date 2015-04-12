package in.twizmwaz.cardinal.module.modules.bookModule;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class BookBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<BookModule> results = new ModuleCollection<BookModule>();
        results.add(new BookModule(match));
        return results;
    }

}
