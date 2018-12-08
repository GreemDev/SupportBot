package net.greemdev.supportbot.files.objects;

import java.awt.Color;

public class ConfigColour {

    private int r;
    private int g;
    private int b;

    public ConfigColour(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color getAsColor() {
        return new Color(this.r, this.g, this.b);
    }

}
