package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

@IsLogicProcess
public class RemoveDead extends IteratingSystem{
    public RemoveDead(){
        super(Aspect.all(MarkerComp.Dead.class));
    }

    @Override
    protected void process(int entityId){
        world.delete(entityId);
    }
}
