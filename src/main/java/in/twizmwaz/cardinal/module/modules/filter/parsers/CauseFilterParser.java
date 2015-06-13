package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import in.twizmwaz.cardinal.module.modules.filter.type.CauseFilter;
import org.jdom2.Element;

public class CauseFilterParser extends FilterParser {

    private final CauseFilter.EventCause cause;

    public CauseFilterParser(final Element element) {
        super(element);
        this.cause = CauseFilter.EventCause.getEventCause(element.getText());
    }

    public CauseFilter.EventCause getCause() {
        return cause;
    }
}
