package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;

public class RangeComp extends CopyableComponent {
    public int range;

    public RangeComp() {
    }

    public RangeComp(int range) {
        this.range = range;
    }

    @Override
    protected void reset() {
        range = 0;
    }

    @Override
    public RangeComp copy(CopyableComponent other) {
        RangeComp rangeComp = (RangeComp)other;
        range = rangeComp.range;
        return this;
    }

    @Override
    public void refill(CopyableComponent def) {
        copy(def);
    }
}
