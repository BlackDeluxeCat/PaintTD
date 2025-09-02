package io.blackdeluxecat.painttd.systems.render;

import com.artemis.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.game.*;

import static io.blackdeluxecat.painttd.Core.shaper;
import static io.blackdeluxecat.painttd.game.Game.flowField;

public class DrawMapGrid extends BaseSystem{
    @Override
    protected void processSystem(){
        shaper.begin(ShapeRenderer.ShapeType.Filled);
        for(int x = 0; x < Game.map.width; x++){
            for(int y = 0; y < Game.map.height; y++){
                if(Game.map.get(x, y).isWall){
                    //将墙壁绘制为白色矩形
                    shaper.setColor(Color.WHITE);
                    shaper.rect(x - 0.4f, y - 0.4f, 0.8f, 0.8f);
                }
            }
        }
        shaper.end();

        shaper.begin(ShapeRenderer.ShapeType.Line);
        for(int x = 0; x < Game.map.width; x++){
            for(int y = 0; y < Game.map.height; y++){
                shaper.setColor(Color.DARK_GRAY);
                shaper.rect(x - 0.5f, y - 0.5f, 1, 1);
            }
        }
        shaper.end();


        shaper.begin(ShapeRenderer.ShapeType.Line);
        for(int x = 0; x < Game.map.width; x++){
            for(int y = 0; y < Game.map.height; y++){
                //绘制流场方向
                var v = flowField.getDirection(x, y, Vars.v1).scl(0.5f);
                shaper.setColor(Color.GREEN);
                shaper.line(x, y, x + v.x, y + v.y);
            }
        }
        shaper.end();
    }
}
