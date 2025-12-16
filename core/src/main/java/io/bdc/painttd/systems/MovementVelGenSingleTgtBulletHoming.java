package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.physics.*;
import io.bdc.painttd.content.components.logic.target.*;
import io.bdc.painttd.content.components.marker.*;

public class MovementVelGenSingleTgtBulletHoming extends IteratingSystem {
    public ComponentMapper<VelocityComp> velocityMapper;
    public ComponentMapper<TargetSingleComp> targetSingleMapper;
    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<MoveSpeedComp> moveSpeedMapper;

    public MovementVelGenSingleTgtBulletHoming() {
        super(Aspect.all(TargetSingleComp.class, VelocityComp.class, MarkerComp.BulletHoming.class, MoveSpeedComp.class));
    }

    @Override
    protected void process(int entityId) {
        VelocityComp vel = velocityMapper.get(entityId);
        TargetSingleComp target = targetSingleMapper.get(entityId);
        PositionComp pos = positionMapper.get(entityId);
        MoveSpeedComp moveSpeed = moveSpeedMapper.get(entityId);

        if (target.targetId != -1) {
            PositionComp epos = positionMapper.get(target.targetId);
            vel.set(Vars.v1.set(epos.x - pos.x, epos.y - pos.y).nor().scl(moveSpeed.speed));
        }
    }
}
