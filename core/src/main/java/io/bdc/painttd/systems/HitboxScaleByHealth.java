package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.physics.*;
import io.bdc.painttd.content.components.marker.*;

@IsLogicProcess
public class HitboxScaleByHealth extends IteratingSystem {
    ComponentMapper<HitboxComp> hitboxMapper;
    ComponentMapper<HealthComp> healthMapper;

    public HitboxScaleByHealth() {
        super(Aspect.all(HitboxComp.class, HealthComp.class, MarkerComp.HitboxScaleByHealth.class));
    }

    @Override
    protected void process(int entityId) {
        HitboxComp hitbox = hitboxMapper.get(entityId);
        HealthComp health = healthMapper.get(entityId);
        hitbox.scale = 1f + 0.1f * (health.health <= 0 ? 0f : (float)Math.pow(health.health, 0.65f));
    }
}
