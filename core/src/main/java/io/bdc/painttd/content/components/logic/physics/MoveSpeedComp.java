package io.bdc.painttd.content.components.logic.physics;

import com.artemis.annotations.*;
import io.bdc.painttd.content.components.*;

@Transient
public class MoveSpeedComp extends CopyableComponent {
    public float baseSpeed = 1;
    public float speed = 1;

    public MoveSpeedComp() {
    }

    public MoveSpeedComp(float baseSpeedUnitPerTick) {
        this.baseSpeed = baseSpeedUnitPerTick;
        this.speed = baseSpeedUnitPerTick;
    }

    @Override
    public MoveSpeedComp copy(CopyableComponent other) {
        MoveSpeedComp moveSpeedComp = (MoveSpeedComp)other;
        baseSpeed = moveSpeedComp.baseSpeed;
        speed = moveSpeedComp.speed;
        return this;
    }

    @Override
    protected void reset() {
        speed = baseSpeed;
    }

    @Override
    public void refill(CopyableComponent def) {
        MoveSpeedComp moveSpeedComp = (MoveSpeedComp)def;
        speed = moveSpeedComp.speed;
        baseSpeed = moveSpeedComp.baseSpeed;
    }
}
