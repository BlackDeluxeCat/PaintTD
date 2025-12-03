package io.bdc.painttd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.game.Game;
import io.bdc.painttd.ui.*;
import io.bdc.painttd.utils.*;

import static io.bdc.painttd.Core.*;
import static io.bdc.painttd.Vars.*;
import static io.bdc.painttd.game.Game.*;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class PaintTowerDefence extends ApplicationAdapter{

    @Override
    public void create(){
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.input.setInputProcessor(inputMultiplexer);

        Core.load();
        Fonts.load();
        assets.finishLoading();
        Styles.load();

        stage.setDebugUnderMouse(true);
        menu.create();

        InputProcessors.create();
        Game.create();
    }

    @Override
    public void render(){
        Time.update();
        prefs.save();

        ScreenUtils.clear(Color.CLEAR);

        lerpZoom = MathUtils.lerp(lerpZoom, zoom, 0.1f);
        worldViewport.setUnitsPerPixel(1f / lerpZoom);
        worldViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        shaper.setProjectionMatrix(worldViewport.getCamera().combined);
        batch.setProjectionMatrix(worldViewport.getCamera().combined);

        //world的现实时间增量
        //多数时候, world使用帧为时间单位
        if(inGame){
            world.setDelta(Time.delta((long)world.getDelta()));
            world.process();
        }

        stage.getViewport().apply(true);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height){
        super.resize(width, height);
        Core.stage.getViewport().update(width, height, true);
    }

    @Override
    public void resume(){
        super.resume();
    }

    @Override
    public void dispose(){
        Game.dispose();
        Core.atlas.dispose();
        Core.assets.dispose();
    }

    @Override
    public void pause(){
        super.pause();
    }
}
