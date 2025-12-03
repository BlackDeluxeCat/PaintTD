package io.bdc.painttd.systems.request;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.physics.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.systems.*;
import io.bdc.painttd.utils.Events;

import static io.bdc.painttd.game.Game.*;

/**
 * 执行碰撞检查. 产生碰撞请求.
 */
@IsLogicProcess
public class CollideQuadTreeDetectFireEvents extends BaseSystem{
    protected IntArray result = new IntArray();
    protected static Rectangle r1 = new Rectangle(), r2 = new Rectangle();

    @All(value = {PositionComp.class, HitboxComp.class, CollideComp.class, MarkerComp.UseQuadTree.class})
    @Exclude(value = {MarkerComp.Dead.class})
    EntitySubscription collideSubscription;

    ComponentMapper<CollideComp> collideMapper;
    ComponentMapper<PositionComp> posMapper;
    ComponentMapper<HitboxComp> hitboxMapper;

    @Override
    protected void processSystem(){
        IntBag bag = collideSubscription.getEntities();

        for(int i = 0; i < bag.size(); i++){
            result.clear();
            int entityId = bag.get(i);
            entities.hitbox(entityId, r1);
            entities.queryRect(r1.x, r1.y, r1.width, r1.height, result, null);

            for(int i1 = 0; i1 < result.size; i1++){
                int otherId = result.get(i1);
                if(otherId != entityId){
                    if(checkZAndComp(entityId, otherId)){
                        var event = EventTypes.collideEvent;
                        event.reset();
                        event.source = entityId;
                        event.target = otherId;
                        Events.fire(event);
                    }
                }
            }
        }
    }

    public boolean checkZAndComp(int entityId, int otherId){
        //检查碰撞类型掩码
        CollideComp collide1 = collideMapper.get(entityId);
        CollideComp collide2 = collideMapper.get(otherId);
        if(collide1 != null && collide2 != null && collide1.canCollide(collide2.type)){
            //检查碰撞盒, 只需检查z轴
            float z1 = posMapper.get(entityId).z;
            float z2 = posMapper.get(otherId).z;
            float d1 = hitboxMapper.get(entityId).z / 2f;
            float d2 = hitboxMapper.get(otherId).z / 2f;
            //比较上下端点
            if(z1 + d1 >= z2 - d2 && z1 - d1 <= z2 + d2){
                return true;
            }
        }
        return false;
    }
}