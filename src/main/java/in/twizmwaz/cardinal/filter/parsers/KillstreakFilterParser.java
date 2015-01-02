package in.twizmwaz.cardinal.filter.parsers;

import org.jdom2.Element;

public class KillstreakFilterParser {

    private int min, max, count;
    private boolean repeat;

    public KillstreakFilterParser(final Element element) {
        try {
            this.min = Integer.parseInt(element.getAttributeValue("min"));
        } catch (NumberFormatException e) {
            this.min = -1;
        }
        try {
            this.max = Integer.parseInt(element.getAttributeValue("max"));
        } catch (NumberFormatException e) {
            this.max = -1;
        }
        try {
            this.count = Integer.parseInt(element.getAttributeValue("count"));
        } catch (NumberFormatException e) {
            this.count = -1;
        }
        try {
            this.repeat = Boolean.parseBoolean(element.getAttributeValue("repeat"));
        } catch (NumberFormatException e) {
            this.repeat = false;
        }
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getCount() {
        return count;
    }

    public boolean isRepeat() {
        return repeat;
    }

}
