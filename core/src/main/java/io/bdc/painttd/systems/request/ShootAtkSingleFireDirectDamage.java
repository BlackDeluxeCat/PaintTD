package io.bdc.painttd.systems.request;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.target.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.systems.*;
import io.bdc.painttd.utils.*;

@IsLogicProcess
public class ShootAtkSingleFireDirectDamage extends IteratingSystem{
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<TargetSingleComp> targetSingleMapper;
    public ComponentMapper<DamageComp> damageMapper;

    public ShootAtkSingleFireDirectDamage(){
        super(Aspect.all(CooldownComp.class, DamageComp.class, TargetSingleComp.class, MarkerComp.ShootAttacker.class));
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);
            if(targetSingle.targetId != -1){
                DamageComp damage = damageMapper.get(entityId);

                for(int i = 0; i < cooldown.shootCount; i++){
                    var event = EventTypes.damageEvent;
                    event.reset();
                    event.source = entityId;
                    event.target = targetSingle.targetId;
                    event.damage = damage.damage;
                    Events.fire(event);
                }
            }
        }
    }
}
