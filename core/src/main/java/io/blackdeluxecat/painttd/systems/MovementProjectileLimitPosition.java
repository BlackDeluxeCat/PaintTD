package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

public class MovementProjectileLimitPosition extends IteratingSystem{
    ComponentMapper<PositionComp> pm;

    public MovementProjectileLimitPosition(){
        super(Aspect.all(PositionComp.class, MarkerComp.BulletProjected.class));
    }

    @Override
    protected void process(int entityId){
        PositionComp p = pm.get(entityId);
        if(p.z < 0){
            p.z = 0;
        }
    }
}
