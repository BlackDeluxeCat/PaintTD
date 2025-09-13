package io.blackdeluxecat.painttd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.game.Game;
import io.blackdeluxecat.painttd.ui.fragment.SettingTable;
import io.blackdeluxecat.painttd.utils.*;

import static io.blackdeluxecat.painttd.Core.*;
import static io.blackdeluxecat.painttd.Vars.*;

public class InputProcessors{
    public static LayerManager<InputProcessor> inputProcessors = new LayerManager<>();

    //layers, z层级低的优先级高
    public static LayerManager.Layer<InputProcessor>
        stage = inputProcessors.registerLayer("stage", 0),
        placement = inputProcessors.registerLayer("placement", 1),
        camera = inputProcessors.registerLayer("camera", 2);

    public static InputAdapter placementInput = new InputAdapter(){
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button){
            if(Vars.hud.current != null && button == Input.Buttons.LEFT){
                var v = Vars.v1;
                worldViewport.unproject(v.set(screenX, screenY));
                if(!Game.map.validPos(Math.round(v.x), Math.round(v.y))) return false;
                Vars.hud.current.draw(v.x, v.y);

                return true;
            }
            return false;
        }
    };

    public static InputAdapter cameraZoom = new InputAdapter(){
        @Override
        public boolean scrolled(float amountX, float amountY){
            //反转缩放
            Vars.zoom = MathUtils.clamp(Vars.zoom - amountY * 5 * (SettingTable.mouseReverse?-1:1), 1f, 200);
            return super.scrolled(amountX, amountY);
        }
    },

    cameraMove = new InputAdapter(){
        float ox;
        float oy;
        Vector2 v = new Vector2();

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button){
            if(button == Input.Buttons.RIGHT){
                ox = screenX;
                oy = screenY;
                return true;
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button){
            if(button == Input.Buttons.RIGHT){
                v.set(ox, screenY).sub(screenX, oy).scl(worldViewport.getUnitsPerPixel());
                worldViewport.getCamera().position.add(v.x, v.y, 0);
                return true;
            }
            return false;
        }
    };

    public static void create(){
        stage.add(Core.stage);
        placement.add(placementInput);
        camera.add(cameraZoom);
        camera.add(cameraMove);
        sort();
    }

    /**
     * 当修改了某个Layer之后, 进行排序.
     */
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
