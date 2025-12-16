package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;

/**
 * 能量系统回复组件。
 */
public class EnergyRegenComp extends CopyableComponent {
    public float regenRate;

    public EnergyRegenComp() {
    }

    public EnergyRegenComp(float regenPreTick) {
        this.regenRate = regenPreTick;
    }

    @Override
    public EnergyRegenComp copy(CopyableComponent other) {
        EnergyRegenComp energyRegenComp = (EnergyRegenComp)other;
        regenRate = energyRegenComp.regenRate;
        return this;
    }

    @Override
    protected void reset() {
        regenRate = 0;
    }
}
