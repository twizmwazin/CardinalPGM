package in.twizmwaz.cardinal.module.modules.cores;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoreObjectiveBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> result = new ArrayList<>();
        for (Element element : match.getDocument().getRootElement().getChildren("cores")) {
            for (Element subElement : element.getChildren("core")) {
                PgmTeam team;
                try {
                    team = match.getTeamById(element.getAttributeValue("team"));
                } catch (NullPointerException e) {
                    team = match.getTeamById(subElement.getAttributeValue("team"));
                }
                String name = "Core";
                try {
                    name = subElement.getAttributeValue("name");
                } catch (NullPointerException e) {
                }
                if (element.getAttributeValue("name") != null) {
                    name = subElement.getAttributeValue("name");
                }
                String id = null;
                try {
                    id = subElement.getAttributeValue("id");
                } catch (NullPointerException e) {
                }
                UnionRegion region = null;
                if (subElement.getAttributeValue("region") != null) {
                    region = new UnionRegion(null, Arrays.asList(Region.getRegion(subElement)));
                } else {
                    List<Region> regions = new ArrayList<>();
                    try {
                        for (Element regionChild : subElement.getChild("regions").getChildren()) {
                            regions.add(Region.getRegion(regionChild));
                        }
                    } catch (NullPointerException e) {
                    }
                    if (subElement.getChild("region") != null) {
                        try {
                            regions.add(Region.getRegion(subElement.getChild("region")));
                        } catch (NullPointerException e) {
                        }
                        try {
                            for (Element subRegion : subElement.getChild("region").getChildren()) {
                                regions.add(Region.getRegion(subRegion));
                            }
                        } catch (NullPointerException e) {
                        }
                    }
                    if (regions.size() == 0) {
                        try {
                            regions.add(Region.getRegion(subElement.getChildren().get(0)));
                        } catch (NullPointerException e) {
                        }
                    }
                }
                int leak = 5;
                try {
                    leak = Integer.parseInt(subElement.getAttributeValue("leak").replaceAll(" ", ""));
                } catch (NullPointerException e) {
                }
                Material type;
                try {
                    type = Material.matchMaterial(element.getAttributeValue("material"));
                } catch (IllegalArgumentException e) {
                    type = Material.matchMaterial(subElement.getAttributeValue("material"));
                } catch (NullPointerException e) {
                    type = Material.matchMaterial(subElement.getAttributeValue("material"));
                }
                result.add(new CoreObjective(team, name, id, region, leak, type));
            }
            for (Element child : element.getChildren("cores")) {
                for (Element subChild : child.getChildren("core")) {
                    PgmTeam team;
                    try {
                        team = match.getTeamById(child.getAttributeValue("team"));
                    } catch (NullPointerException e) {
                        try {
                            team = match.getTeamById(subChild.getAttributeValue("team"));
                        } catch (NullPointerException ex) {
                            team = match.getTeamById(element.getAttributeValue("team"));
                        }
                    }
                    String name = "Core";
                    try {
                        name = subChild.getAttributeValue("name");
                    } catch (NullPointerException e) {
                    }
                    if (element.getAttributeValue("name") != null) {
                        name = subChild.getAttributeValue("name");
                    }
                    String id = null;
                    try {
                        id = subChild.getAttributeValue("id");
                    } catch (NullPointerException e) {
                    }
                    UnionRegion region = null;
                    if (subChild.getAttributeValue("region") != null) {
                        region = new UnionRegion(null, Arrays.asList(Region.getRegion(subChild)));
                    } else {
                        List<Region> regions = new ArrayList<>();
                        try {
                            for (Element regionChild : subChild.getChild("regions").getChildren()) {
                                regions.add(Region.getRegion(regionChild));
                            }
                        } catch (NullPointerException e) {
                        }
                        if (subChild.getChild("region") != null) {
                            try {
                                regions.add(Region.getRegion(subChild.getChild("region")));
                            } catch (NullPointerException e) {
                            }
                            try {
                                for (Element subRegion : subChild.getChild("region").getChildren()) {
                                    regions.add(Region.getRegion(subRegion));
                                }
                            } catch (NullPointerException e) {
                            }
                        }
                        if (regions.size() == 0) {
                            try {
                                regions.add(Region.getRegion(subChild.getChildren().get(0)));
                            } catch (NullPointerException e) {
                            }
                        }
                    }
                    int leak = 5;
                    try {
                        leak = Integer.parseInt(subChild.getAttributeValue("leak").replaceAll(" ", ""));
                    } catch (NullPointerException e) {
                    }
                    Material type;
                    try {
                        type = Material.matchMaterial(child.getAttributeValue("material"));
                    } catch (IllegalArgumentException e) {
                        type = Material.matchMaterial(subChild.getAttributeValue("material"));
                    } catch (NullPointerException e) {
                        type = Material.matchMaterial(subChild.getAttributeValue("material"));
                    }
                    result.add(new CoreObjective(team, name, id, region, leak, type));
                }
            }
        }
        return result;
    }

}
