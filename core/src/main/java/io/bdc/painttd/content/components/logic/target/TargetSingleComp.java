package io.bdc.painttd.content.components.logic.target;

import com.artemis.annotations.*;
import io.bdc.painttd.content.components.*;

public class TargetSingleComp extends CopyableComponent {
    @EntityId
    public int targetId = -1;

    public TargetSingleComp() {
    }

    @Override
    protected void reset() {
        targetId = -1;
    }

    @Override
    public TargetSingleComp copy(CopyableComponent other) {
        TargetSingleComp tcc = (TargetSingleComp)other;
        targetId = tcc.targetId;
        return tcc;
    }
}
