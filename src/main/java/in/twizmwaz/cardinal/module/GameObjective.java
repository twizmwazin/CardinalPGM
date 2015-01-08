package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.teams.PgmTeam;

public interface GameObjective extends Module {

    public PgmTeam getTeam();

    public String getName();
    
    public String getId();

    public boolean isTouched();

    public boolean isComplete();

}
