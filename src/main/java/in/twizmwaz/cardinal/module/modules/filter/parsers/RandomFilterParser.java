package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import org.jdom2.Element;

public class RandomFilterParser extends FilterParser {

    private double chance;

    public RandomFilterParser(final Element element) {
        super(element);
        try {
            chance = Double.parseDouble(element.getText());
        } catch (NumberFormatException e) {
            String[] numbers = element.getText().replace("(", "").replace(")", "").replace("[", "").replace("]", "").split(",");
            chance = Double.parseDouble(numbers[1]) - Double.parseDouble(numbers[0]);
        }
    }

    public double getChance() {
        return chance;
    }

}
