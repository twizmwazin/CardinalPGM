package in.twizmwaz.cardinal.filter;

public enum FilterState {

    ALLOW(true),
    DENY(false),
    ABSTAIN(true);

    private final boolean state;

    private FilterState(boolean state) {
        this.state = state;
    }

    public boolean toBoolean() {
        return state;
    }

}
