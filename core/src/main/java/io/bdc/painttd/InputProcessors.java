package io.bdc.painttd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.game.Game;
import io.bdc.painttd.utils.*;

import static io.bdc.painttd.Core.*;
import static io.bdc.painttd.Vars.*;

public class InputProcessors{
    public static LayerManager<InputProcessor> inputProcessors = new LayerManager<>();

    //layers, z层级低的优先级高
    public static LayerManager.Layer<InputProcessor>
        stage = inputProcessors.registerLayer("stage", 0),
        placement = inputProcessors.registerLayer("placement", 10),
        select = inputProcessors.registerLayer("select", 50),
        camera = inputProcessors.registerLayer("camera", 30);

    public static InputAdapter placementInput = new InputAdapter(){
        boolean drawing;
        long pressed;
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button){
            if(Vars.hud.current != null && button == Input.Buttons.LEFT){
                var v = Vars.v1;
                worldViewport.unproject(v.set(screenX, screenY));
                if(!Game.map.validPos(Math.round(v.x), Math.round(v.y))) return false;
                Vars.hud.current.draw(v.x, v.y);

                pressed = TimeUtils.millis();
                drawing = true;
                return true;
            }
            drawing = false;
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer){
            return drawing && hud.current != null && hud.current.longPress && TimeUtils.millis() - pressed > 100 ? touchDown(screenX, screenY, pointer, Input.Buttons.LEFT) : super.touchDragged(screenX, screenY, pointer);
        }
    },

    cameraZoom = new InputAdapter(){
        @Override
        public boolean scrolled(float amountX, float amountY){
            Vars.zoom = MathUtils.clamp(Vars.zoom - amountY * 5, 1f, 200);
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
    },

    selectBuilding = new InputAdapter(){
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button){
            if(button == Input.Buttons.LEFT){
                int e = Game.hovered.hovered;
                hud.hoveredTable.build(e);
            }
            return false;
        }
    }
    ;

    public static void create(){
        stage.add(Core.stage);
        placement.add(placementInput);
        camera.add(cameraZoom);
        camera.add(cameraMove);
        select.add(selectBuilding);
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
