package in.twizmwaz.cardinal.module.modules.teamRegister;

import com.google.common.collect.Lists;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class StringFilters {

    private final StringFilter teamFilter;
    private final StringFilter uuidFilter;

    public StringFilters(ConfigurationSection config) {
        teamFilter = parse(config, "team");
        uuidFilter = parse(config, "uuid");
    }

    private StringFilter parse(ConfigurationSection config, String child) {
        if (config != null && config.getKeys().contains(child)) {
            List<StringFilter> result = Lists.newArrayList();
            List<Map<?, ?>> filters = config.getMapList(child);
            for (Map<?, ?> filter : filters) {
                if (filter.containsKey("case") && filter.get("case") instanceof String) {
                    result.add(new CaseFilter(((String) filter.get("case")).equalsIgnoreCase("lowercase")));
                } else if (filter.containsKey("match") && filter.containsKey("replace")
                        && filter.get("match") instanceof String && filter.get("replace") instanceof String) {
                    result.add(new MatchFilter((String) filter.get("match"), (String) filter.get("replace")));
                }
            }
            return new FilterList(result);
        }
        return new FilterList(new ArrayList<>());
    }

    protected String getTeam(String team) {
        return teamFilter.transform(team);
    }

    protected String getUuid(String uuid) {
        return uuidFilter.transform(uuid);
    }

    private interface StringFilter {
        String transform(String string);
    }

    private class MatchFilter implements StringFilter {

        private final String regex;
        private final String replace;

        public MatchFilter(String regex, String replace) {
            this.regex = regex;
            this.replace = replace;
        }

        @Override
        public String transform(String string) {
            return string.replaceAll(regex, replace);
        }

    }

    private class CaseFilter implements StringFilter {

        private final boolean lowerCase;

        public CaseFilter(boolean lowerCase) {
            this.lowerCase = lowerCase;
        }

        @Override
        public String transform(String string) {
            return lowerCase ? string.toLowerCase() : string.toUpperCase();
        }

    }

    private class FilterList implements StringFilter {

        List<StringFilter> filters;

        public FilterList(List<StringFilter> filters) {
            this.filters = filters;
        }

        @Override
        public String transform(String string) {
            for (StringFilter filter : filters) {
                string = filter.transform(string);
            }
            return string;
        }

    }

}
