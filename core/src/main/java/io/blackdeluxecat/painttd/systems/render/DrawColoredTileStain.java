package io.blackdeluxecat.painttd.systems.render;

import com.artemis.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;

import static io.blackdeluxecat.painttd.Core.shaper;
import static io.blackdeluxecat.painttd.game.Game.map;

public class DrawColoredTileStain extends BaseSystem{
    public ComponentMapper<HealthComp> healthMapper;
    public ComponentMapper<TileStainComp> tileStainMapper;

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        healthMapper = world.getMapper(HealthComp.class);
        tileStainMapper = world.getMapper(TileStainComp.class);
    }

    @Override
    protected void processSystem(){
        for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                var e = map.getEntity(x, y, "tileStain");
                if(e == -1) continue;
                var health = healthMapper.get(e);
                var stain = tileStainMapper.get(e);

                if(health.health > 0){
                    var color = map.colorPalette.getColor(MathUtils.ceil(health.health) - 1);
                    shaper.setColor(Vars.c1.set(color));
                    shaper.begin(ShapeRenderer.ShapeType.Filled);
                    shaper.rect(x - 0.4f, y - 0.4f, 0.8f, 0.8f);
                    shaper.end();
                }

                if(stain.isCore){
                    shaper.setColor(Color.WHITE);

                    shaper.begin(ShapeRenderer.ShapeType.Line);
                    shaper.circle(x, y, 0.3f, 6);
                    shaper.end();
                }
            }
        }
    }
}
