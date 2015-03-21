package in.twizmwaz.cardinal.util;

import java.util.UUID;

public enum AttributeType {

    GENERIC_MAX_HEALTH("generic.maxHealth", 0, Integer.MAX_VALUE),
    GENERIC_FOLLOW_RANGE("generic.followRange", 0, 2048),
    GENERIC_KNOCKBACK_RESISTANCE("generic.knockbackResistance", 0, 1),
    GENERIC_MOVEMENT_SPEED("generic.movementSpeed", 0, Double.MAX_VALUE),
    GENERIC_ATTACK_DAMAGE("generic.attackDamage", 0, Double.MAX_VALUE),

    HORSE_JUMP_STRENGTH("horse.jumpStrength", 0, 2);

    private String name;
    private double minValue;
    private double maxValue;

    public static UUID modifierUUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");

    AttributeType(String name, double minValue, double maxValue) {
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public static AttributeType fromName(String name) {
        for (AttributeType type: AttributeType.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

}
