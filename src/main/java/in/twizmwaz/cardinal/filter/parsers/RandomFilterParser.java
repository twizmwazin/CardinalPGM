package in.twizmwaz.cardinal.filter.parsers;

import org.jdom2.Element;

public class RandomFilterParser {

    private double chance;

    private RandomFilterParser(final Element element) {
        try {
            chance = Double.parseDouble(element.getText());
        } catch (NumberFormatException e){
            String[] numbers = element.getText().replace("(", "").replace(")", "").replace("[", "").replace("]", "").split(",");
            chance = Double.parseDouble(numbers[1]) - Double.parseDouble(numbers[0]);
        }
    }

    public double getChance() {
        return chance;
    }

}
