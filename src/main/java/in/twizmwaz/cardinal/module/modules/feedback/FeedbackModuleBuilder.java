package in.twizmwaz.cardinal.module.modules.feedback;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class FeedbackModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<FeedbackModule> load(Match match) {
        String feedback = match.getDocument().getRootElement().getChildText("feedback");
        ModuleCollection<FeedbackModule> result = new ModuleCollection<>();
        if (feedback != null) result.add(new FeedbackModule(feedback));
        return result;
    }

}
