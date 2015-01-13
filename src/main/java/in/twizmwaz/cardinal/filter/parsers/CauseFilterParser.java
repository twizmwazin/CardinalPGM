package in.twizmwaz.cardinal.filter.parsers;

import in.twizmwaz.cardinal.filter.type.CauseFilter;
import org.jdom2.Element;

public class CauseFilterParser {

    private final CauseFilter.EventCause cause;

    public CauseFilterParser(final Element element) {
        this.cause = CauseFilter.EventCause.getEventCause(element.getText());
    }

    public CauseFilter.EventCause getCause() {
        return cause;
    }
}
