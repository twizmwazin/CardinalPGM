
package in.twizmwaz.cardinal;

import in.twizmwaz.cardinal.util.GitUtils;

public class UpdateHandler {

    static {
        handler = new UpdateHandler();
    }

    private static UpdateHandler handler;

    private boolean update;
    private final String localGitRevision;

    public UpdateHandler(){
        handler = this;
        this.localGitRevision = GameHandler.getGameHandler().getPlugin().getDescription().getVersion().substring(GameHandler.getGameHandler().getPlugin().getDescription().getVersion().length() - 6, GameHandler.getGameHandler().getPlugin().getDescription().getVersion().length());
    }

    /**
     * @return Returns true/false if an there is a new update in the update.txt file on github
     */
    public boolean checkUpdates() {
        String revision = GitUtils.getLatestGitRevision();
        if (revision != null && !revision.startsWith(localGitRevision)) {
            update = true;
        }
        return update;
    }

    public static UpdateHandler getUpdateHandler() {
        return handler;
    }


}