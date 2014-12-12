package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.teams.PgmTeam;

public interface GameCondition extends Module {

    public PgmTeam getTeam();

    public boolean isTouched();

    public boolean isComplete();

}
