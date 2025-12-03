package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.marker.*;

/**
 * 带有冷却的武器的攻击
 */
@IsLogicProcess
public class CooldownShoot extends IteratingSystem{
    public ComponentMapper<CooldownComp> cm;

    public CooldownShoot(){
        super(Aspect.all(CooldownComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cm.get(entityId);
        cooldown.shootCount = 0;
        cooldown.currentCooldown -= 1;
        while(cooldown.currentCooldown <= 0){
            cooldown.currentCooldown += cooldown.cooldown;
            cooldown.shootCount += 1;
        }
    }
}