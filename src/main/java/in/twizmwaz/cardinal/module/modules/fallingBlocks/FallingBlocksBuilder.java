package in.twizmwaz.cardinal.module.modules.fallingBlocks;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import org.jdom2.Element;

import java.util.HashSet;
import java.util.Set;

public class FallingBlocksBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<FallingBlocksModule> load(Match match) {
        Set<Rule> rules = new HashSet<>();
        for (Element fallingBlocks : match.getDocument().getRootElement().getChildren("falling-blocks")) {
            for (Element rule : fallingBlocks.getChildren("rule")) {
                FilterModule filter = FilterModuleBuilder.getAttributeOrChild("filter", "never", rule, fallingBlocks);
                FilterModule sticky = FilterModuleBuilder.getAttributeOrChild("sticky", "never", rule, fallingBlocks);
                int delay = Numbers.parseInt(Parser.getOrderedAttributeOrChild("delay", rule, fallingBlocks), 2);
                rules.add(new Rule(filter, sticky, delay));
            }
        }
        return new ModuleCollection<>(new FallingBlocksModule(rules));
    }

}
