package io.bdc.painttd.systems.render;

import com.artemis.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import io.bdc.painttd.*;
import io.bdc.painttd.game.Game;

import static io.bdc.painttd.Core.shaper;
import static io.bdc.painttd.game.Game.flowField;

public class DrawFlowFieldDebug extends BaseSystem{
    public static boolean pref;

    @Override
    protected void initialize(){
        super.initialize();
        pref = Core.prefs.getBoolean("drawFlowField");
    }

    @Override
    protected void processSystem(){
        if(!pref) return;
        shaper.begin(ShapeRenderer.ShapeType.Line);
        shaper.getColor().set(Color.GREEN, 0.4f);
        for(int x = 0; x < Game.map.width; x++){
            for(int y = 0; y < Game.map.height; y++){
                var v = flowField.getDirection(x, y, Vars.v1).scl(0.5f);
                shaper.triangle(x + v.x, y + v.y, x - v.scl(0.25f).y, y - v.x, x + v.y, y + v.x);
            }
        }
        shaper.end();
    }
}
