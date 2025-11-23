package io.blackdeluxecat.painttd.utils.color;

import com.badlogic.gdx.graphics.Color;

public class ColorTrans {
    private ColorTrans() {};
    public static HSVColor RGBtoHSV(Color color) {
        return new HSVColor(color);
    }

    public static Color HSVtoRGB(HSVColor color) {
        return color.toRGB();
    }
}
