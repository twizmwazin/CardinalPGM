package in.twizmwaz.cardinal.module.modules.kit;

public class KitFly {

    private boolean canFly;
    private boolean flying;
    private float flySpeed;

    public KitFly(boolean canFly, boolean flying, float flySpeed) {
        this.canFly = canFly;
        this.flying = flying;
        this.flySpeed = flySpeed;
    }

    public boolean canFly() {
        return canFly;
    }

    public boolean isFlying() {
        return flying;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

}
