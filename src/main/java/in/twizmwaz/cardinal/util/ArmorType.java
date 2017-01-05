package in.twizmwaz.cardinal.util;

public enum ArmorType {

    HELMET(39),
    CHESTPLATE(38),
    LEGGINGS(37),
    BOOTS(36);

    private final int slot;

    ArmorType(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public static ArmorType getArmorType(String string) {
        switch (string.toLowerCase()) {
            case "helmet":
                return HELMET;
            case "chestplate":
                return CHESTPLATE;
            case "leggings":
                return LEGGINGS;
            case "boots":
                return BOOTS;
            default:
                return null;
        }
    }

    public String toString() {
        switch (this) {
            case HELMET:
                return "helmet";
            case CHESTPLATE:
                return "chestplate";
            case LEGGINGS:
                return "leggings";
            case BOOTS:
                return "boots";
            default:
                return "";
        }
    }
}
