package in.twizmwaz.cardinal.module.modules.projectileParticles;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class ProjectileParticlesModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ProjectileParticlesModule> load(Match match) {
        return new ModuleCollection<>(new ProjectileParticlesModule());
    }

}
