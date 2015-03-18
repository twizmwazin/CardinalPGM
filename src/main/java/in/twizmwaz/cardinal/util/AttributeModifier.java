package in.twizmwaz.cardinal.util;

public class AttributeModifier {

    private AttributeType attributeType;
    private double value;
    private String operation;

    public AttributeModifier(AttributeType attributeType, double value, String operation) {
        this.attributeType = attributeType;
        this.value = value;
        this.operation = operation;

    }

    public AttributeType getAttributeType() {
        return attributeType;
    }

    public double getValue() {
        return value;
    }

    public String getOperation() {
        return operation;
    }

    public int getOperationValue() {
        if (this.operation.equalsIgnoreCase("add")) {
            return 0;
        } else if (this.operation.equalsIgnoreCase("base")) {
            return 1;
        } else if (this.operation.equalsIgnoreCase("multiple")) {
            return 2;
        }
        return 0;
    }

}
