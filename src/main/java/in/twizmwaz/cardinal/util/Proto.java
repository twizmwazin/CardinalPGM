package in.twizmwaz.cardinal.util;

/**
 * Class to represent XML protocols, based on semantic versioning.
 */
public class Proto {

    private final int major;
    private final int minor;
    private final int patch;

    public Proto(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    /**
     * @param in The semantic version string to be parsed.
     * @return The parsed Proto.
     */
    public static Proto parseProto(String in) {
        String[] components = in.split("\\.");
        if (components.length != 3) {
            throw new NumberFormatException("A proto must be a semantic version.");
        }
        int major = Integer.parseInt(components[0]);
        int minor = Integer.parseInt(components[1]);
        int patch = Integer.parseInt(components[2]);
        return new Proto(major, minor, patch);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Proto
                && this.major == ((Proto) object).getMajor()
                && this.minor == ((Proto) object).getMinor()
                && this.patch == ((Proto) object).getPatch();
    }

    public boolean biggerThan(Proto proto) {
        return this.major > proto.getMajor()
                || (this.major == proto.getMajor() && (this.minor > proto.getMinor()
                || (this.minor == proto.getMinor() && this.patch > proto.getPatch())));
    }

    public boolean biggerOrEqualTo(Proto proto) {
        return this.equals(proto) || this.biggerThan(proto);
    }

    public boolean smallerThan(Proto proto) {
        return this.major < proto.getMajor()
                || (this.major == proto.getMajor() && (this.minor < proto.getMinor()
                || (this.minor == proto.getMinor() && this.patch < proto.getPatch())));
    }

    public boolean smallerOrEqualTo(Proto proto) {
        return this.equals(proto) || this.smallerThan(proto);
    }

}
