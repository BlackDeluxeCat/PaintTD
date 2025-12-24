package io.bdc.painttd;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.*;
import io.bdc.painttd.ui.*;

public class UI {
    public static boolean inGame;
    public static boolean inMenu = true;

    public static Stage stage = new Stage(new ScreenViewport(), Core.batch);

    public static NodeGraphEditorDialog nodeGraphEditorDialog = new NodeGraphEditorDialog();
    public static MenuGroup menu = new MenuGroup();
    public static HudGroup hud = new HudGroup();
    public static PreferenceDialog preferenceDialog = new PreferenceDialog();
}
