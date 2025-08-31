package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

public class MarkHealthDead extends IteratingSystem{
    public ComponentMapper<HealthComp> hm;

    public MarkHealthDead(){
        super(Aspect.all(HealthComp.class).exclude(MarkerComp.Dead.class, TileStainComp.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        hm = world.getMapper(HealthComp.class);
    }

    @Override
    protected void process(int entityId){
        if(hm.get(entityId).health <= 0) world.getEntity(entityId).edit().create(MarkerComp.Dead.class);
    }
}
