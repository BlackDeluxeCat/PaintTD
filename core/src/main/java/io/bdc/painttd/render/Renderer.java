package io.bdc.painttd.render;

import static io.bdc.painttd.ui.Styles.whiteRegion;

public class Renderer {
    public static Fill fill = new Fill();
    public static Line line = new Line();

    public static void load() {
        fill.setRegion(whiteRegion);
        line.setRegion(whiteRegion);
    }
}
