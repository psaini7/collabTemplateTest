package com.virtubuild.custom.abbeconf.general;

public enum Orientation {

    UNKNOWN("unknown"),
    HORIZONTAL("horizontal"),
    VERTICAL("vertical"),
    ;

    private final String value;

    private Orientation(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Orientation getOrientation(final String value) {
        for (Orientation type : Orientation.values()) {
            if (!type.equals(UNKNOWN) && type.toString().equals(value)) {
                return type;
            }
        }
        return UNKNOWN;
    }

}
