package io.bdc.painttd.content.components.logic;

import com.artemis.annotations.*;
import com.badlogic.gdx.math.*;
import io.bdc.painttd.content.components.*;

@Transient
public class MovementNextPathComp extends CopyableComponent {
    public Vector2 current = new Vector2(), next = new Vector2();

    public MovementNextPathComp() {
    }

    @Override
    protected void reset() {
        current.setZero();
        next.setZero();
    }

    @Override
    public MovementNextPathComp copy(CopyableComponent other) {
        return this;
    }
}
