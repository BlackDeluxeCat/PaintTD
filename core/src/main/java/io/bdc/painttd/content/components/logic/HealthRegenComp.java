package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;

public class HealthRegenComp extends CopyableComponent {
    public float rate;

    public HealthRegenComp(float ratePerTick) {
        this.rate = ratePerTick;
    }

    public HealthRegenComp() {
    }

    @Override
    public CopyableComponent copy(CopyableComponent other) {
        this.rate = ((HealthRegenComp)other).rate;
        return this;
    }

    @Override
    protected void reset() {
        rate = 0;
    }
}
