package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.math.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.physics.*;
import io.bdc.painttd.content.components.logic.target.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.utils.*;

import static io.bdc.painttd.game.Game.utils;

@IsLogicProcess
public class ShootSingleTargetBullet extends IteratingSystem{
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<BulletTypeComp> bulletTypeMapper;

    ComponentMapper<PositionComp> positionMapper;
    ComponentMapper<VelocityComp> velocityMapper;
    ComponentMapper<AccelerationComp> accelerationMapper;
    ComponentMapper<MarkerComp.BulletProjected> bulletProjectedMapper;
    ComponentMapper<MoveSpeedComp> moveSpeedMapper;

    ComponentMapper<TeamComp> teamMapper;
    ComponentMapper<ColorLevelComp> colorLevelMapper;
    ComponentMapper<StainSplashComp> stainSplashMapper;
    ComponentMapper<TargetSingleComp> targetSingleMapper;
    ComponentMapper<TargetPosComp> targetPosMapper;

    public ShootSingleTargetBullet(){
        super(Aspect.all(CooldownComp.class, BulletTypeComp.class, TargetSingleComp.class));
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);

            if(targetSingle.targetId != -1){
                BulletTypeComp bulletTypeComp = bulletTypeMapper.get(entityId);

                for(int i = 0; i < cooldown.shootCount * bulletTypeComp.amt; i++){
                    var bullet = bulletTypeComp.type.create();
                    int bulletId = bullet.getId();
                    PositionComp pos = positionMapper.get(entityId);
                    PositionComp bulletPos = positionMapper.get(bullet);
                    bulletPos.x = pos.x;
                    bulletPos.y = pos.y;

                    utils.setTeam(bulletId, teamMapper.get(entityId).team);
                    utils.targetCompParser(entityId, bulletId);

                    var event = EventTypes.bulletSpawnEvent;
                    event.reset();
                    event.b = bulletId;
                    event.source = entityId;
                    Events.fire(event);
                }
            }
        }
    }

    @Override
    protected void initialize(){
        super.initialize();

        //为子弹设置色阶伤害倍增
        Events.on(EventTypes.BulletSpawnEvent.class, e -> {
            if(e.handled) return;
            ColorLevelComp level = colorLevelMapper.get(e.b);
            StainSplashComp stainSplash = stainSplashMapper.get(e.b);
            if(level != null && stainSplash != null){
                stainSplash.damage = level.level;
            }
        });

        //为抛射行为子弹轻微随机落点
        //理论上在动量分量之前执行
        Events.on(EventTypes.BulletSpawnEvent.class, e -> {
            if(e.handled) return;
            if(bulletProjectedMapper.has(e.b) && targetPosMapper.has(e.b)){
                PositionComp pos = positionMapper.get(e.b);
                TargetPosComp targetPos = targetPosMapper.get(e.b);
                targetPos.x += MathUtils.random(-2, 2);
                targetPos.y += MathUtils.random(-2, 2);
            }
        });

        //为抛射行为子弹初始化动量分量
        Events.on(EventTypes.BulletSpawnEvent.class, e -> {
            if(e.handled) return;
            if(bulletProjectedMapper.has(e.b) && targetPosMapper.has(e.b)){
                PositionComp pos = positionMapper.get(e.b);
                VelocityComp vel = velocityMapper.get(e.b);
                AccelerationComp acc = accelerationMapper.get(e.b); //加速度z分量一般是定义好的
                TargetPosComp targetPos = targetPosMapper.get(e.b);
                MoveSpeedComp moveSpeed = moveSpeedMapper.get(e.b);

                var v = Vars.v1;
                v.set(targetPos.x - pos.x, targetPos.y - pos.y);
                float flightTicks = v.len() / moveSpeed.speed;
                v.nor().scl(moveSpeed.speed);
                vel.set(v);
                vel.z = -acc.z * flightTicks / 2f;
            }
        });
    }

    @Override
    protected void dispose(){
        super.dispose();
    }
}
