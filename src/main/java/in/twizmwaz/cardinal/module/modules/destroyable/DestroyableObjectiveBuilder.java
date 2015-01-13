package in.twizmwaz.cardinal.module.modules.destroyable;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class DestroyableObjectiveBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> result = new ArrayList<>();
        for (Element element : match.getDocument().getRootElement().getChildren("destroyables")) {
            for (Element subElement : element.getChildren("destroyable")) {
                PgmTeam owner;
                try {
                    owner = match.getTeamById(subElement.getAttributeValue("owner"));
                } catch (NullPointerException e) {
                    owner = match.getTeamById(element.getAttributeValue("owner"));
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
                List<Region> regions = new ArrayList<>();
                if (subElement.getAttributeValue("region") != null) {
                    regions.add(Region.getRegion(subElement));
                } else {
                    for (Element region : subElement.getChildren()) {
                        regions.add(Region.getRegion(region));
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
                                damageValues.add(Integer.parseInt(material.split(":")[1].trim()));
                            } else {
                                types.add(Material.matchMaterial(material.trim()));
                                damageValues.add(0);
                            }
                        }
                    } else {
                        if (materials.contains(":")) {
                            types.add(Material.matchMaterial(materials.split(":")[0].trim()));
                            damageValues.add(Integer.parseInt(materials.split(":")[1].trim()));
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
                                damageValues.add(Integer.parseInt(material.split(":")[1].trim()));
                            } else {
                                types.add(Material.matchMaterial(material.trim()));
                                damageValues.add(0);
                            }
                        }
                    } else {
                        if (materials.contains(":")) {
                            types.add(Material.matchMaterial(materials.split(":")[0].trim()));
                            damageValues.add(Integer.parseInt(materials.split(":")[1].trim()));
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
                result.add(new DestroyableObjective(owner, name, id, new UnionRegion(regions), types, damageValues, required, showProgress, repairable));
            }
            for (Element child : element.getChildren("destroyables")) {
                for (Element subChild : child.getChildren("destroyable")) {
                    PgmTeam owner;
                    try {
                        owner = match.getTeamById(subChild.getAttributeValue("owner"));
                    } catch (NullPointerException e) {
                        try {
                            owner = match.getTeamById(child.getAttributeValue("owner"));
                        } catch (NullPointerException exc) {
                            owner = match.getTeamById(element.getAttributeValue("owner"));
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
                    List<Region> regions = new ArrayList<>();
                    if (subChild.getAttributeValue("region") != null) {
                        regions.add(Region.getRegion(subChild));
                    } else {
                        for (Element region : subChild.getChildren()) {
                            regions.add(Region.getRegion(region));
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
                                    damageValues.add(Integer.parseInt(material.split(":")[1].trim()));
                                } else {
                                    types.add(Material.matchMaterial(material.trim()));
                                    damageValues.add(0);
                                }
                            }
                        } else {
                            if (materials.contains(":")) {
                                types.add(Material.matchMaterial(materials.split(":")[0].trim()));
                                damageValues.add(Integer.parseInt(materials.split(":")[1].trim()));
                            } else {
                                types.add(Material.matchMaterial(materials.trim()));
                                damageValues.add(0);
                            }
                        }
                    } else if (child.getAttributeValue("materials") != null) {
                        String materials = child.getAttributeValue("materials");
                        if (materials.contains(";")) {
                            for (String material : materials.split(";")) {
                                if (material.contains(":")) {
                                    types.add(Material.matchMaterial(material.split(":")[0].trim()));
                                    damageValues.add(Integer.parseInt(material.split(":")[1].trim()));
                                } else {
                                    types.add(Material.matchMaterial(material.trim()));
                                    damageValues.add(0);
                                }
                            }
                        } else {
                            if (materials.contains(":")) {
                                types.add(Material.matchMaterial(materials.split(":")[0].trim()));
                                damageValues.add(Integer.parseInt(materials.split(":")[1].trim()));
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
                                    damageValues.add(Integer.parseInt(material.split(":")[1].trim()));
                                } else {
                                    types.add(Material.matchMaterial(material.trim()));
                                    damageValues.add(0);
                                }
                            }
                        } else {
                            if (materials.contains(":")) {
                                types.add(Material.matchMaterial(materials.split(":")[0].trim()));
                                damageValues.add(Integer.parseInt(materials.split(":")[1].trim()));
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
                    result.add(new DestroyableObjective(owner, name, id, new UnionRegion(regions), types, damageValues, required, showProgress, repairable));
                }
            }
        }
        return result;
    }

}
