package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.content.components.logic.*;

@IsLogicProcess
public class HealthRegen extends IteratingSystem {
    ComponentMapper<HealthRegenComp> healthRegenMapper;
    ComponentMapper<HealthComp> healthMapper;

    public HealthRegen() {
        super(Aspect.all(HealthRegenComp.class, HealthComp.class));
    }

    @Override
    protected void process(int entityId) {
        HealthRegenComp healthRegen = healthRegenMapper.get(entityId);
        HealthComp health = healthMapper.get(entityId);
        health.health += healthRegen.rate;

        if (health.maxHealth != -1 && health.health > health.maxHealth) {
            health.health = health.maxHealth;
        }
    }
}
