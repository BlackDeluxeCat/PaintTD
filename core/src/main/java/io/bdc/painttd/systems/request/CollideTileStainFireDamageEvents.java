package io.bdc.painttd.systems.request;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.math.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.physics.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.game.*;
import io.bdc.painttd.systems.*;
import io.bdc.painttd.utils.*;

import static io.bdc.painttd.game.Game.*;

@IsLogicProcess
public class CollideTileStainFireDamageEvents extends IteratingSystem{
    public ComponentMapper<CollideComp> collideMapper;
    public ComponentMapper<HitboxComp> hitboxMapper;
    public ComponentMapper<PositionComp> positionMapper;

    public CollideTileStainFireDamageEvents(){
        super(Aspect.all(CollideComp.class, HealthComp.class, TeamComp.class, PositionComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void process(int entityId){
        if(true) return;
        CollideComp collide = collideMapper.get(entityId);
        if(collide.canCollide(CollideComp.OVERLAY)){
            PositionComp positionComp = positionMapper.get(entityId);
            HitboxComp hitboxComp = hitboxMapper.get(entityId);

            int cx = positionComp.tileX(), cy = positionComp.tileY();

            int minx = MathUtils.floor(cx - hitboxComp.x() / 2f + 0.5f), maxx = MathUtils.ceil(cx + hitboxComp.x() / 2f - 0.5f);
            int miny = MathUtils.floor(cy - hitboxComp.y() / 2f + 0.5f), maxy = MathUtils.ceil(cy + hitboxComp.y() / 2f - 0.5f);

            for(int x = minx; x <= maxx; x++){
                for(int y = miny; y <= maxy; y++){
                    if(Game.map.validPos(x, y)){
                        int stainId = map.getTileStain(x, y);
                        var event = EventTypes.collideDamageEvent;
                        event.reset();
                        event.source = entityId;
                        event.target = stainId;
                        Events.fire(event);
                    }
                }
            }

        }
    }
}
