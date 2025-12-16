package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.content.components.logic.physics.*;

@IsLogicProcess
public class MovementAccelerationPush extends IteratingSystem {
    ComponentMapper<AccelerationComp> accelerationMapper;
    ComponentMapper<VelocityComp> velocityMapper;

    public MovementAccelerationPush() {
        super(Aspect.all(VelocityComp.class, AccelerationComp.class));
    }

    @Override
    protected void process(int entityId) {
        AccelerationComp acceleration = accelerationMapper.get(entityId);
        VelocityComp velocity = velocityMapper.get(entityId);
        velocity.x += acceleration.x;
        velocity.y += acceleration.y;
        velocity.z += acceleration.z;
    }
}
