package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.physics.*;

@IsLogicProcess
public class MovementVelPush extends IteratingSystem {
    public ComponentMapper<PositionComp> pm;
    public ComponentMapper<VelocityComp> vm;

    public MovementVelPush() {
        super(Aspect.all(PositionComp.class, VelocityComp.class));
    }

    @Override
    protected void process(int entityId) {
        PositionComp p = pm.get(entityId);
        VelocityComp v = vm.get(entityId);
        p.x += v.x;
        p.y += v.y;
        p.z += v.z;
    }
}
