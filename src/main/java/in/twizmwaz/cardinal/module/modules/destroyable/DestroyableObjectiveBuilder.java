package in.twizmwaz.cardinal.module.modules.destroyable;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderData(load = ModuleLoadTime.EARLIER)
public class DestroyableObjectiveBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection result = new ModuleCollection();
        for (Element element : match.getDocument().getRootElement().getChildren("destroyables")) {
            for (Element subElement : element.getChildren("destroyable")) {
                TeamModule owner;
                try {
                    owner = Teams.getTeamById(subElement.getAttributeValue("owner")).orNull();
                } catch (NullPointerException e) {
                    owner = Teams.getTeamById(element.getAttributeValue("owner")).orNull();
                }
                String name = "Monument";
                if (subElement.getAttributeValue("name") != null) {
                    name = subElement.getAttributeValue("name");
                } else if (element.getAttributeValue("name") != null) {
                    name = element.getAttributeValue("name");
                }
                String id = null;
                if (subElement.getAttributeValue("id") != null) {
                    id = subElement.getAttributeValue("id");
                } else if (element.getAttributeValue("id") != null) {
                    id = element.getAttributeValue("id");
                }
                ModuleCollection<RegionModule> regions = new ModuleCollection<>();
                if (subElement.getAttributeValue("region") != null) {
                    regions.add(RegionModuleBuilder.getRegion(subElement.getAttributeValue("region")));
                } else {
                    for (Element region : subElement.getChildren()) {
                        regions.add(RegionModuleBuilder.getRegion(region));
                    }
                }
                List<Material> types = new ArrayList<>();
                List<Integer> damageValues = new ArrayList<>();
                if (subElement.getAttributeValue("materials") != null) {
                    String materials = subElement.getAttributeValue("materials");
                    if (materials.contains(";")) {
                        for (String material : materials.split(";")) {
                            if (material.contains(":")) {
                                types.add(Material.matchMaterial(material.split(":")[0].trim()));
                                damageValues.add(Numbers.parseInt(material.split(":")[1].trim()));
                            } else {
                                types.add(Material.matchMaterial(material.trim()));
                                damageValues.add(-1);
                            }
                        }
                    } else {
                        if (materials.contains(":")) {
                            types.add(Material.matchMaterial(materials.split(":")[0].trim()));
                            damageValues.add(Numbers.parseInt(materials.split(":")[1].trim()));
                        } else {
                            types.add(Material.matchMaterial(materials.trim()));
                            damageValues.add(-1);
                        }
                    }
                } else if (element.getAttributeValue("materials") != null) {
                    String materials = element.getAttributeValue("materials");
                    if (materials.contains(";")) {
                        for (String material : materials.split(";")) {
                            if (material.contains(":")) {
                                types.add(Material.matchMaterial(material.split(":")[0].trim()));
                                damageValues.add(Numbers.parseInt(material.split(":")[1].trim()));
                            } else {
                                types.add(Material.matchMaterial(material.trim()));
                                damageValues.add(0);
                            }
                        }
                    } else {
                        if (materials.contains(":")) {
                            types.add(Material.matchMaterial(materials.split(":")[0].trim()));
                            damageValues.add(Numbers.parseInt(materials.split(":")[1].trim()));
                        } else {
                            types.add(Material.matchMaterial(materials.trim()));
                            damageValues.add(0);
                        }
                    }
                }
                double required = 1.0;
                if (subElement.getAttributeValue("completion") != null) {
                    required = Double.parseDouble(subElement.getAttributeValue("completion").replaceAll("%", "").replaceAll(" ", "")) / 100.0;
                } else if (element.getAttributeValue("completion") != null) {
                    required = Double.parseDouble(element.getAttributeValue("completion").replaceAll("%", "").replaceAll(" ", "")) / 100.0;
                }
                boolean showProgress = false;
                if (subElement.getAttributeValue("show-progress") != null) {
                    showProgress = subElement.getAttributeValue("show-progress").equalsIgnoreCase("true");
                } else if (element.getAttributeValue("show-progress") != null) {
                    showProgress = element.getAttributeValue("show-progress").equalsIgnoreCase("true");
                }
                boolean repairable = false;
                if (subElement.getAttributeValue("repairable") != null) {
                    repairable = subElement.getAttributeValue("repairable").equalsIgnoreCase("true");
                } else if (element.getAttributeValue("repairable") != null) {
                    repairable = element.getAttributeValue("repairable").equalsIgnoreCase("true");
                }
                boolean show = true;
                if (subElement.getAttributeValue("show") != null) {
                    show = !subElement.getAttributeValue("show").equalsIgnoreCase("false");
                } else if (element.getAttributeValue("show") != null) {
                    show = !element.getAttributeValue("show").equalsIgnoreCase("false");
                }
                boolean changesModes = false;
                if (subElement.getAttributeValue("mode-changes") != null) {
                    changesModes = subElement.getAttributeValue("mode-changes").equalsIgnoreCase("true");
                } else if (element.getAttributeValue("mode-changes") != null) {
                    changesModes = element.getAttributeValue("mode-changes").equalsIgnoreCase("true");
                }
                result.add(new DestroyableObjective(owner, name, id, new UnionRegion(null, regions), types, damageValues, required, show, changesModes, showProgress, repairable));
            }
            for (Element child : element.getChildren("destroyables")) {
                for (Element subChild : child.getChildren("destroyable")) {
                    TeamModule owner;
                    try {
                        owner = Teams.getTeamById(subChild.getAttributeValue("owner")).orNull();
                    } catch (NullPointerException e) {
                        try {
                            owner = Teams.getTeamById(child.getAttributeValue("owner")).orNull();
                        } catch (NullPointerException exc) {
                            owner = Teams.getTeamById(element.getAttributeValue("owner")).orNull();
                        }
                    }
                    String name = "Monument";
                    if (subChild.getAttributeValue("name") != null) {
                        name = subChild.getAttributeValue("name");
                    } else if (child.getAttributeValue("name") != null) {
                        name = child.getAttributeValue("name");
                    } else if (child.getAttributeValue("name") != null) {
                        name = element.getAttributeValue("name");
                    }
                    String id = null;
                    if (subChild.getAttributeValue("id") != null) {
                        id = subChild.getAttributeValue("id");
                    } else if (child.getAttributeValue("id") != null) {
                        id = child.getAttributeValue("id");
                    } else if (child.getAttributeValue("id") != null) {
                        id = element.getAttributeValue("id");
                    }
                    ModuleCollection<RegionModule> regions = new ModuleCollection<>();
                    if (subChild.getAttributeValue("region") != null) {
                        regions.add(RegionModuleBuilder.getRegion(subChild));
                    } else {
                        for (Element region : subChild.getChildren()) {
                            regions.add(RegionModuleBuilder.getRegion(region));
                        }
                    }
                    List<Material> types = new ArrayList<>();
                    List<Integer> damageValues = new ArrayList<>();
                    if (subChild.getAttributeValue("materials") != null) {
                        String materials = subChild.getAttributeValue("materials");
                        if (materials.contains(";")) {
                            for (String material : materials.split(";")) {
                                if (material.contains(":")) {
                                    types.add(Material.matchMaterial(material.split(":")[0].trim()));
                                    damageValues.add(Numbers.parseInt(material.split(":")[1].trim()));
                                } else {
                                    types.add(Material.matchMaterial(material.trim()));
                                    damageValues.add(-1);
                                }
                            }
                        } else {
                            if (materials.contains(":")) {
                                types.add(Material.matchMaterial(materials.split(":")[0].trim()));
                                damageValues.add(Numbers.parseInt(materials.split(":")[1].trim()));
                            } else {
                                types.add(Material.matchMaterial(materials.trim()));
                                damageValues.add(-1);
                            }
                        }
                    } else if (child.getAttributeValue("materials") != null) {
                        String materials = child.getAttributeValue("materials");
                        if (materials.contains(";")) {
                            for (String material : materials.split(";")) {
                                if (material.contains(":")) {
                                    types.add(Material.matchMaterial(material.split(":")[0].trim()));
                                    damageValues.add(Numbers.parseInt(material.split(":")[1].trim()));
                                } else {
                                    types.add(Material.matchMaterial(material.trim()));
                                    damageValues.add(-1);
                                }
                            }
                        } else {
                            if (materials.contains(":")) {
                                types.add(Material.matchMaterial(materials.split(":")[0].trim()));
                                damageValues.add(Numbers.parseInt(materials.split(":")[1].trim()));
                            } else {
                                types.add(Material.matchMaterial(materials.trim()));
                                damageValues.add(0);
                            }
                        }
                    } else if (element.getAttributeValue("materials") != null) {
                        String materials = element.getAttributeValue("materials");
                        if (materials.contains(";")) {
                            for (String material : materials.split(";")) {
                                if (material.contains(":")) {
                                    types.add(Material.matchMaterial(material.split(":")[0].trim()));
                                    damageValues.add(Numbers.parseInt(material.split(":")[1].trim()));
                                } else {
                                    types.add(Material.matchMaterial(material.trim()));
                                    damageValues.add(0);
                                }
                            }
                        } else {
                            if (materials.contains(":")) {
                                types.add(Material.matchMaterial(materials.split(":")[0].trim()));
                                damageValues.add(Numbers.parseInt(materials.split(":")[1].trim()));
                            } else {
                                types.add(Material.matchMaterial(materials.trim()));
                                damageValues.add(0);
                            }
                        }
                    }
                    double required = 1.0;
                    if (subChild.getAttributeValue("completion") != null) {
                        required = Double.parseDouble(subChild.getAttributeValue("completion").replaceAll("%", "").replaceAll(" ", "")) / 100.0;
                    } else if (child.getAttributeValue("completion") != null) {
                        required = Double.parseDouble(child.getAttributeValue("completion").replaceAll("%", "").replaceAll(" ", "")) / 100.0;
                    } else if (element.getAttributeValue("completion") != null) {
                        required = Double.parseDouble(element.getAttributeValue("completion").replaceAll("%", "").replaceAll(" ", "")) / 100.0;
                    }
                    boolean showProgress = false;
                    if (subChild.getAttributeValue("show-progress") != null) {
                        showProgress = subChild.getAttributeValue("show-progress").equalsIgnoreCase("true");
                    } else if (child.getAttributeValue("show-progress") != null) {
                        showProgress = child.getAttributeValue("show-progress").equalsIgnoreCase("true");
                    } else if (element.getAttributeValue("show-progress") != null) {
                        showProgress = element.getAttributeValue("show-progress").equalsIgnoreCase("true");
                    }
                    boolean repairable = false;
                    if (subChild.getAttributeValue("repairable") != null) {
                        repairable = subChild.getAttributeValue("repairable").equalsIgnoreCase("true");
                    } else if (child.getAttributeValue("repairable") != null) {
                        repairable = child.getAttributeValue("repairable").equalsIgnoreCase("true");
                    } else if (element.getAttributeValue("repairable") != null) {
                        repairable = element.getAttributeValue("repairable").equalsIgnoreCase("true");
                    }
                    boolean show = true;
                    if (subChild.getAttributeValue("show") != null) {
                        show = !subChild.getAttributeValue("show").equalsIgnoreCase("false");
                    } else if (child.getAttributeValue("show") != null) {
                        show = !child.getAttributeValue("show").equalsIgnoreCase("false");
                    } else if (element.getAttributeValue("show") != null) {
                        show = !element.getAttributeValue("show").equalsIgnoreCase("false");
                    }
                    boolean changesModes = false;
                    if (subChild.getAttributeValue("mode-changes") != null) {
                        changesModes = subChild.getAttributeValue("mode-changes").equalsIgnoreCase("true");
                    } else if (child.getAttributeValue("mode-changes") != null) {
                        changesModes = child.getAttributeValue("mode-changes").equalsIgnoreCase("true");
                    } else if (element.getAttributeValue("mode-changes") != null) {
                        changesModes = element.getAttributeValue("mode-changes").equalsIgnoreCase("true");
                    }
                    result.add(new DestroyableObjective(owner, name, id, new UnionRegion(null, regions), types, damageValues, required, show, changesModes, showProgress, repairable));
                }
            }
        }
        return result;
    }

}
