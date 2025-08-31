package io.blackdeluxecat.painttd;

import com.badlogic.gdx.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.struct.*;

import static io.blackdeluxecat.painttd.Core.*;
import static io.blackdeluxecat.painttd.Vars.worldViewport;
import static io.blackdeluxecat.painttd.game.Game.map;
import static io.blackdeluxecat.painttd.game.Game.utils;

public class Input{
    public static LayerManager<InputProcessor> inputProcessors = new LayerManager<>();

    //layers, z层级低的优先级高
    public static LayerManager.Layer<InputProcessor>
        stage = inputProcessors.registerLayer("stage", 0),
        placement = inputProcessors.registerLayer("placement", 1),
        camera = inputProcessors.registerLayer("camera", 2);

    public static InputAdapter placementInput = new InputAdapter(){
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button){
            if(Vars.hud.current != null){
                var v = Vars.v1;
                worldViewport.unproject(v.set(screenX, screenY));
                Vars.hud.current.draw(v.x, v.y);

                return true;
            }
            return false;
        }
    };

    public static void create(){
        stage.add(Core.stage);
        placement.add(placementInput);

        sort();
    }

    /**当修改了某个Layer之后, 进行排序.*/
    public static void sort(){
        inputProcessors.sort();
        inputMultiplexer.clear();
        for(var layer : inputProcessors.layers){
            for(var processor : layer.objects){
                inputMultiplexer.addProcessor(processor);
            }
        }
    }
}
