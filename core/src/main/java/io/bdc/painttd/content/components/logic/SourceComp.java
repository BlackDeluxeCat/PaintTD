package io.bdc.painttd.content.components.logic;

import com.artemis.annotations.*;
import io.bdc.painttd.content.components.*;

public class SourceComp extends CopyableComponent {
    @EntityId
    public int source;

    public SourceComp() {
    }

    @Override
    protected void reset() {
        source = -1;
    }

    @Override
    public SourceComp copy(CopyableComponent other) {
        SourceComp comp = (SourceComp)other;
        source = comp.source;
        return this;
    }
}
