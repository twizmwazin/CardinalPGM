package in.twizmwaz.cardinal.module;

public abstract class Module {

    public Module() {
        this.onEnable();

    }

    protected abstract void onEnable();


}
