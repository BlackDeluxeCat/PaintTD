package io.bdc.painttd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.game.Game;
import io.bdc.painttd.render.*;
import io.bdc.painttd.ui.*;
import io.bdc.painttd.utils.*;

import static io.bdc.painttd.Core.*;
import static io.bdc.painttd.game.Game.*;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class PaintTowerDefence extends ApplicationAdapter {

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.input.setInputProcessor(inputMultiplexer);

        Core.load();
        Fonts.load();
        assets.finishLoading();
        Styles.load();
        Renderer.load();

        UI.stage.setDebugUnderMouse(true);
        UI.stage.setDebugAll(true);
        UI.menu.create();

        InputProcessors.create();
        Game.create();
    }

    @Override
    public void render() {
        Time.update();
        prefs.save();

        ScreenUtils.clear(Color.CLEAR);
        Renderer.update();
        //world的现实时间增量
        //多数时候, world使用帧为时间单位
        if (UI.inGame) {
            world.setDelta(Time.delta((long)world.getDelta()));
            world.process();
        }

        UI.stage.getViewport().apply(true);
        UI.stage.act();
        UI.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        UI.stage.getViewport().update(width, height, true);
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        Game.dispose();
        Core.atlas.dispose();
        Core.assets.dispose();
    }

    @Override
    public void pause() {
        super.pause();
    }
}
