package io.bdc.painttd.ui;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;

import static io.bdc.painttd.Core.*;

public class Fonts {
    public static FreetypeFontLoader.FreeTypeFontLoaderParameter params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();

    public static BitmapFont standard;

    public static void load() {
        assets.setLoader(BitmapFont.class, new FreetypeFontLoader(assets.getFileHandleResolver()));
        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(assets.getFileHandleResolver()));

        params.fontFileName = "fonts/SourceHanSansHWSC-VF.ttf";
        params.fontParameters.size = 32;
        params.fontParameters.color = Color.WHITE;
        params.fontParameters.borderWidth = 1;
        params.fontParameters.incremental = true;
        params.fontParameters.minFilter = Texture.TextureFilter.Linear;
        params.fontParameters.magFilter = Texture.TextureFilter.Linear;

        assets.load("fonts/SourceHanSansHWSC-VF.ttf", BitmapFont.class, params);
        assets.finishLoadingAsset("fonts/SourceHanSansHWSC-VF.ttf");
        standard = assets.get("fonts/SourceHanSansHWSC-VF.ttf", BitmapFont.class);
    }
}
