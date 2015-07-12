package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import in.twizmwaz.cardinal.util.Strings;
import org.jdom2.Element;

public class TimeFilterParser extends FilterParser {

    private final int time;

    public TimeFilterParser(final Element element) {
        super(element);
        this.time = Strings.timeStringToSeconds(element.getText());
    }

    public int getTime() {
        return this.time;
    }

}