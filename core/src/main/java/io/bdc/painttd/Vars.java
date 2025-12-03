package io.bdc.painttd;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;
import io.bdc.painttd.ui.*;

public class Vars{
    public static ScreenViewport worldViewport = new ScreenViewport();
    public static float zoom = 64f, lerpZoom = zoom;

    public static boolean inGame, inMenu = true;

    public static MenuGroup menu = new MenuGroup();
    public static HudGroup hud = new HudGroup();
    public static PreferenceDialog preferenceDialog = new PreferenceDialog();

    public static Vector2 v1 = new Vector2(), v2 = new Vector2(), v3 = new Vector2();
    public static Rectangle r1 = new Rectangle(), r2 = new Rectangle(), r3 = new Rectangle();
    public static Color c1 = new Color(), c2 = new Color();
}
