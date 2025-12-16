package io.bdc.painttd.render;

import com.badlogic.gdx.math.*;

import static io.bdc.painttd.Core.*;
import static io.bdc.painttd.ui.Styles.*;

public class Shapes {
    public static void rect(float x, float y, float width, float height) {
        batch.draw(whiteRegion, x, y, width, height);
    }

    public static void circle(float x, float y, float radius, int segments) {
        float angle = 2 * MathUtils.PI / segments;
        float cos = MathUtils.cos(angle);
        float sin = MathUtils.sin(angle);

        float cx = radius;
        float cy = 0;

        for (int i = 0; i < segments; i++) {
            float temp = cx;
            cx = cos * cx - sin * cy;
            cy = sin * temp + cos * cy;

            batch.draw(whiteRegion,
                x + cx, y + cy,
                2, 2);
        }
    }
}
