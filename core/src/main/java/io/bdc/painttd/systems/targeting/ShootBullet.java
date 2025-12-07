package io.bdc.painttd.systems.targeting;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.systems.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.target.*;
import io.bdc.painttd.utils.*;

/**
 * 冷却射击生成子弹, 为子弹实体设置源
 * 通过事件和策略自定义初始化子弹
 */
public class ShootBullet extends IteratingSystem{
    ComponentMapper<CooldownComp> cooldownMapper;
    ComponentMapper<BulletTypeComp> bulletTypeMapper;
    ComponentMapper<PositionComp> positionMapper;

    ComponentMapper<TeamComp> teamMapper;
    ComponentMapper<SourceComp> sourceMapper;

    ComponentMapper<TargetPosComp> targetPosMapper;
    ComponentMapper<TargetSingleComp> targetSingleMapper;

    public ShootBullet(){
        super(Aspect.all(CooldownComp.class, BulletTypeComp.class, PositionComp.class));
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            BulletTypeComp bulletTypeComp = bulletTypeMapper.get(entityId);

            for(int i = 0; i < cooldown.shootCount * bulletTypeComp.amt; i++){
                var bullet = bulletTypeComp.type.create();
                int bulletId = bullet.getId();

                //设置子弹位置
                PositionComp pos = positionMapper.get(entityId);
                PositionComp bulletPos = positionMapper.get(bullet);
                bulletPos.x = pos.x;
                bulletPos.y = pos.y;

                //设置子弹源
                sourceMapper.get(bulletId).source = entityId;
                //设置子弹队伍
                TeamComp team = teamMapper.get(entityId);
                TeamComp bulletTeam = teamMapper.get(bulletId);
                bulletTeam.team = team.team;

                var event = EventTypes.projectileSpawnEvent;
                event.reset();
                event.projectile = bulletId;
                event.source = entityId;
                Events.fire(event);
            }
        }
    }

    public Object tokenTargetSingleHoming;

    @Override
    protected void initialize(){
        super.initialize();

        tokenTargetSingleHoming = Events.on(EventTypes.BulletSpawnEvent.class, e -> {
            if(targetSingleMapper.has(e.projectile) &&
            targetPosMapper.has(e.projectile) &&
            targetSingleMapper.has(e.source)){
                TargetSingleComp sourceTargetSingle = targetSingleMapper.get(e.source);
                TargetSingleComp bulletTargetSingle = targetSingleMapper.get(e.projectile);
                bulletTargetSingle.targetId = sourceTargetSingle.targetId;
            }
        });
    }
}