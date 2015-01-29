package in.twizmwaz.cardinal.module.modules.hill;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.StringUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.jdom2.Element;

public class HillObjectiveBuilder implements ModuleBuilder {

    @SuppressWarnings("unchecked")
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        for (Element king : match.getDocument().getRootElement().getChildren("king")) {
            for (Element hill : king.getChildren("hill")) {
                TeamModule team = null;
                if (hill.getAttributeValue("initial-owner") != null) {
                    team = TeamUtils.getTeamById(hill.getAttributeValue("initial-owner"));
                }
                String name = null;
                if (hill.getAttributeValue("name") != null) {
                    name = hill.getAttributeValue("name");
                }
                String id = null;
                if (hill.getAttributeValue("id") != null) {
                    id = hill.getAttributeValue("id");
                } else {
                    if (name != null) {
                        id = name.toLowerCase();
                    }
                }
                int captureTime = 30;
                if (hill.getAttributeValue("capture-time") != null) {
                    captureTime = StringUtils.timeStringToSeconds(hill.getAttributeValue("capture-time"));
                }
                boolean showProgress = false;
                if (hill.getAttributeValue("show-progress") != null) {
                    showProgress = hill.getAttributeValue("show-progress").equalsIgnoreCase("true");
                }
                boolean show = true;
                if (hill.getAttributeValue("show") != null) {
                    show = !hill.getAttributeValue("show").equalsIgnoreCase("false");
                }
                results.add(new HillObjective(team, name, id, captureTime, showProgress, show));
            }
            for (Element hills : king.getChildren("hills")) {
                for (Element hill : hills.getChildren("hill")) {
                    TeamModule team = null;
                    if (hill.getAttributeValue("initial-owner") != null) {
                        team = TeamUtils.getTeamById(hill.getAttributeValue("initial-owner"));
                    } else if (hills.getAttributeValue("initial-owner") != null) {
                        team = TeamUtils.getTeamById(hills.getAttributeValue("initial-owner"));
                    }
                    String name = null;
                    if (hill.getAttributeValue("name") != null) {
                        name = hill.getAttributeValue("name");
                    } else if (hills.getAttributeValue("name") != null) {
                        name = hills.getAttributeValue("name");
                    }
                    String id = null;
                    if (hill.getAttributeValue("id") != null) {
                        id = hill.getAttributeValue("id");
                    } else if (hills.getAttributeValue("id") != null) {
                        id = hills.getAttributeValue("id");
                    } else {
                        if (name != null) {
                            id = name.toLowerCase();
                        }
                    }
                    int captureTime = 30;
                    if (hill.getAttributeValue("capture-time") != null) {
                        captureTime = StringUtils.timeStringToSeconds(hill.getAttributeValue("capture-time"));
                    } else if (hills.getAttributeValue("capture-time") != null) {
                        captureTime = StringUtils.timeStringToSeconds(hills.getAttributeValue("capture-time"));
                    }
                    boolean showProgress = false;
                    if (hill.getAttributeValue("show-progress") != null) {
                        showProgress = hill.getAttributeValue("show-progress").equalsIgnoreCase("true");
                    } else if (hills.getAttributeValue("show-progress") != null) {
                        showProgress = hills.getAttributeValue("show-progress").equalsIgnoreCase("true");
                    }
                    boolean show = true;
                    if (hill.getAttributeValue("show") != null) {
                        show = !hill.getAttributeValue("show").equalsIgnoreCase("false");
                    } else if (hills.getAttributeValue("show") != null) {
                        show = !hills.getAttributeValue("show").equalsIgnoreCase("false");
                    }
                    results.add(new HillObjective(team, name, id, captureTime, showProgress, show));
                }
            }
        }
        for (Element king : match.getDocument().getRootElement().getChildren("control-points")) {
            for (Element hill : king.getChildren("control-point")) {
                TeamModule team = null;
                if (hill.getAttributeValue("initial-owner") != null) {
                    team = TeamUtils.getTeamById(hill.getAttributeValue("initial-owner"));
                }
                String name = null;
                if (hill.getAttributeValue("name") != null) {
                    name = hill.getAttributeValue("name");
                }
                String id = null;
                if (hill.getAttributeValue("id") != null) {
                    id = hill.getAttributeValue("id");
                } else {
                    if (name != null) {
                        id = name.toLowerCase();
                    }
                }
                int captureTime = 30;
                if (hill.getAttributeValue("capture-time") != null) {
                    captureTime = StringUtils.timeStringToSeconds(hill.getAttributeValue("capture-time"));
                }
                boolean showProgress = false;
                if (hill.getAttributeValue("show-progress") != null) {
                    showProgress = hill.getAttributeValue("show-progress").equalsIgnoreCase("true");
                }
                boolean show = true;
                if (hill.getAttributeValue("show") != null) {
                    show = !hill.getAttributeValue("show").equalsIgnoreCase("false");
                }
                results.add(new HillObjective(team, name, id, captureTime, showProgress, show));
            }
            for (Element hills : king.getChildren("control-points")) {
                for (Element hill : hills.getChildren("control-point")) {
                    TeamModule team = null;
                    if (hill.getAttributeValue("initial-owner") != null) {
                        team = TeamUtils.getTeamById(hill.getAttributeValue("initial-owner"));
                    } else if (hills.getAttributeValue("initial-owner") != null) {
                        team = TeamUtils.getTeamById(hills.getAttributeValue("initial-owner"));
                    }
                    String name = null;
                    if (hill.getAttributeValue("name") != null) {
                        name = hill.getAttributeValue("name");
                    } else if (hills.getAttributeValue("name") != null) {
                        name = hills.getAttributeValue("name");
                    }
                    String id = null;
                    if (hill.getAttributeValue("id") != null) {
                        id = hill.getAttributeValue("id");
                    } else if (hills.getAttributeValue("id") != null) {
                        id = hills.getAttributeValue("id");
                    } else {
                        if (name != null) {
                            id = name.toLowerCase();
                        }
                    }
                    int captureTime = 30;
                    if (hill.getAttributeValue("capture-time") != null) {
                        captureTime = StringUtils.timeStringToSeconds(hill.getAttributeValue("capture-time"));
                    } else if (hills.getAttributeValue("capture-time") != null) {
                        captureTime = StringUtils.timeStringToSeconds(hills.getAttributeValue("capture-time"));
                    }
                    boolean showProgress = false;
                    if (hill.getAttributeValue("show-progress") != null) {
                        showProgress = hill.getAttributeValue("show-progress").equalsIgnoreCase("true");
                    } else if (hills.getAttributeValue("show-progress") != null) {
                        showProgress = hills.getAttributeValue("show-progress").equalsIgnoreCase("true");
                    }
                    boolean show = true;
                    if (hill.getAttributeValue("show") != null) {
                        show = !hill.getAttributeValue("show").equalsIgnoreCase("false");
                    } else if (hills.getAttributeValue("show") != null) {
                        show = !hills.getAttributeValue("show").equalsIgnoreCase("false");
                    }
                    results.add(new HillObjective(team, name, id, captureTime, showProgress, show));
                }
            }
        }
        return results;
    }
}