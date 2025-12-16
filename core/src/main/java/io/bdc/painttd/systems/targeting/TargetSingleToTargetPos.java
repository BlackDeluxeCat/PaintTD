package io.bdc.painttd.systems.targeting;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.target.*;

public class TargetSingleToTargetPos extends IteratingSystem {
    ComponentMapper<TargetSingleComp> tsc;
    ComponentMapper<TargetPosComp> tpc;
    ComponentMapper<PositionComp> pm;

    public TargetSingleToTargetPos() {
        super(Aspect.all(TargetSingleComp.class, TargetPosComp.class));
    }

    @Override
    protected void process(int entityId) {
        TargetSingleComp current = tsc.get(entityId);
        TargetPosComp targetPos = tpc.get(entityId);
        targetPos.shoot = current.targetId != -1;
        if (current.targetId != -1) {
            PositionComp epos = pm.get(current.targetId);
            if (epos != null) {
                targetPos.x = epos.x;
                targetPos.y = epos.y;
            }
        }
    }
}
