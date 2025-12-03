package io.bdc.painttd.ui;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import io.bdc.painttd.*;

import static io.bdc.painttd.ui.Fonts.*;

public class Styles{
    public static float buttonSize = 36f;
    public static TextureAtlas.AtlasRegion whiteRegion;

    public static TextButton.TextButtonStyle sTextB, sTextBEmpty;
    public static Label.LabelStyle sLabel;
    public static TextField.TextFieldStyle sTextF;
    public static Window.WindowStyle sWindow;

    public static TextureRegionDrawable white;
    public static Drawable black, black3, black5, black8, menu;
    public static NinePatchDrawable ninePatch;

    public static void load(){
        whiteRegion = Core.atlas.findRegion("white");
        white = new TextureRegionDrawable(whiteRegion);
        black = white.tint(Color.BLACK);
        black3 = white.tint(new Color(0f, 0f, 0f, 0.3f));
        black5 = white.tint(new Color(0f, 0f, 0f, 0.5f));
        black8 = white.tint(new Color(0f, 0f, 0f, 0.8f));
        menu = white.tint(new Color(0.1f, 0.2f, 1f, 0.2f));

        ninePatch = new NinePatchDrawable(Core.atlas.createPatch("ui-button"));

        sTextB = new TextButton.TextButtonStyle(ninePatch, ninePatch, ninePatch, standard);
        sTextBEmpty = new TextButton.TextButtonStyle(null, black8, black3, standard);
        sLabel = new Label.LabelStyle(standard, Color.WHITE);
        sTextF = new TextField.TextFieldStyle(standard, Color.WHITE, null, null, ninePatch);

        sWindow = new Window.WindowStyle(standard, new Color(0.1f, 0.2f, 1f, 1f), ninePatch);
    }
}
