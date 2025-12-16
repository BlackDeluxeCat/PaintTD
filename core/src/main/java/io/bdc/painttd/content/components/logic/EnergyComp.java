package io.bdc.painttd.content.components.logic;


import io.bdc.painttd.content.components.*;

/**
 * 能量系统的储量组件。
 */
public class EnergyComp extends CopyableComponent {
    public float energy, maxEnergy;

    public EnergyComp() {
    }

    public EnergyComp(float maxEnergy) {
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
    }

    @Override
    public EnergyComp copy(CopyableComponent other) {
        EnergyComp energyComp = (EnergyComp)other;
        energy = energyComp.energy;
        maxEnergy = energyComp.maxEnergy;
        return this;
    }

    @Override
    protected void reset() {
        energy = 0;
        maxEnergy = 0;
    }

    @Override
    public void refill(CopyableComponent def) {
        EnergyComp energyComp = (EnergyComp)def;
        maxEnergy = energyComp.maxEnergy;
    }
}
