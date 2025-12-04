package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.content.components.logic.*;

public class TrajectorySystem extends IteratingSystem{
    public ComponentMapper<TrajectoryComp> tm;

    public TrajectorySystem(){
        super(Aspect.all(TrajectoryComp.class));
    }

    @Override
    protected void process(int entityId){
        var tree = tm.get(entityId).tree;
        tree.inject(entityId);
        tree.update(1f);
    }
}
