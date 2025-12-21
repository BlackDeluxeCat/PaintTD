package io.bdc.painttd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.io.*;
import io.bdc.painttd.render.*;

public class Core {
    public static AssetManager assets = new AssetManager();
    public static TextureAtlas atlas = new TextureAtlas("PaintTDAssets.atlas");
    public static SpriteBatch batch = new SpriteBatch();
    public static ShapeRenderer shaper = new ShapeRenderer();

    /**
     * 集成的输入处理器。任何自定义处理器自行持有、在此插入一个InputProcessor。
     */
    public static InputMultiplexer inputMultiplexer = new InputMultiplexer();

    public static FileHandle gameDataFolder = Gdx.files.external("AppData/Roaming/PaintTD");

    public static PrefMgr prefs = new PrefMgr();

    public static I18NBundle i18n;
    public static FileHandle i18nFile;

    public static void load() {
        prefs.load();

        i18nFile = Gdx.files.internal("i18n/bundle");
        try {
            if (prefs.get("i18n") == null) {
                //系统默认语言
                i18n = I18NBundle.createBundle(i18nFile);
            } else {
                //设置项语言
                i18n = I18NBundle.createBundle(i18nFile, prefs.getString("i18n", "zh_CN"));
            }
        } catch (Exception e) {
            Gdx.app.error("I18N", "Load failed", e);
        }
        I18NBundle.setExceptionOnMissingKey(false);
    }
}
