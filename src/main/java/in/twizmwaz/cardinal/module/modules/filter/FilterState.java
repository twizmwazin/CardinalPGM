package in.twizmwaz.cardinal.module.modules.filter;

public enum FilterState {

    /**
     * The filter allows this event.
     */
    ALLOW(true),
    /**
     * The filter denies this event.
     */
    DENY(false),
    /**
     * The filter does not apply to the given event.
     */
    ABSTAIN(true);

    private final boolean state;

    private FilterState(boolean state) {
        this.state = state;
    }

    /**
     * @return Returns a boolean value to represent the FilterState
     */
    public boolean toBoolean() {
        return state;
    }

}
