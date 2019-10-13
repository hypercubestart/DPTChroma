package com.andrewliu.gpuvideo;

public enum ColorBlindTypes {
    Deuteranope("Deuteranope"),
    Protanope("Protanope"),
    Tritanope("Tritanope"),
    Azure("Azure");

    private String type;

    ColorBlindTypes(String type) {
        this.type = type;
    }

    private String getType() {
        return type;
    }
}
