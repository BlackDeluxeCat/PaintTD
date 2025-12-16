package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.content.components.logic.*;

@IsLogicProcess
public class EnergyRegenerate extends IteratingSystem {
    public ComponentMapper<EnergyComp> em;
    public ComponentMapper<EnergyRegenComp> erm;

    public EnergyRegenerate() {
        super(Aspect.all(EnergyComp.class, EnergyRegenComp.class));
    }

    @Override
    protected void process(int entityId) {
        em.get(entityId).energy += erm.get(entityId).regenRate;
    }
}
