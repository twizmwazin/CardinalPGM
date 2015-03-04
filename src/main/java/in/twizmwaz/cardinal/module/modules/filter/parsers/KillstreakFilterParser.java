package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.jdom2.Element;

public class KillstreakFilterParser extends FilterParser {

    private int min, max, count;
    private boolean repeat;

    public KillstreakFilterParser(final Element element) {
        super(element);
        this.min = element.getAttributeValue("min") != null ? 
                NumUtils.parseInt(element.getAttributeValue("min")) : -1;
        this.max = element.getAttributeValue("max") != null ?
                NumUtils.parseInt(element.getAttributeValue("max")) : -1;
        this.count = element.getAttributeValue("count") != null ?
                NumUtils.parseInt(element.getAttributeValue("count")) : -1;
        this.repeat = element.getAttributeValue("repeat") != null && NumUtils.parseBoolean(element.getAttributeValue("repeat"));
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
