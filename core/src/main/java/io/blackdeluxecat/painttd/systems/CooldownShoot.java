package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.utils.IntArray;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.Game;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.struct.func.IntBoolf;

import static io.blackdeluxecat.painttd.game.Game.damageQueue;

/**
 * 带有冷却的武器的攻击
 * */
public class CooldownShoot extends IteratingSystem{
    public ComponentMapper<CooldownComp> cm;
    public ComponentMapper<DamageComp> dm;
    public ComponentMapper<TargetComp> tm;
    public ComponentMapper<BuildTypeComp> bm;
    public ComponentMapper<PositionComp> pm;
    public ComponentMapper<TeamComp> teamM;
    public static IntArray innerResults = new IntArray();

    public CooldownShoot(){
        super(Aspect.all(TargetComp.class, CooldownComp.class, DamageComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        cm = world.getMapper(CooldownComp.class);
        dm = world.getMapper(DamageComp.class);
        tm = world.getMapper(TargetComp.class);
        bm = world.getMapper(BuildTypeComp.class);
        pm = world.getMapper(PositionComp.class);
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cm.get(entityId);
        if(cooldown.currentCooldown <= 0){
            cooldown.currentCooldown += cooldown.cooldown;
            int tgt = tm.get(entityId).targetId;
            if(tgt != -1){
                if(bm.get(entityId).type == BuildTypeComp.SINGLE_DAMAGE){
                    damageQueue.add(entityId, tgt, DamageQueue.DamageType.direct);
                } else if (bm.get(entityId).type == BuildTypeComp.GROUP_DAMAGE) {
                    Game.entities.queryCircle(
                        pm.get(tgt).x,
                        pm.get(tgt).y,
                        20.0f,
                        innerResults,
                        new IntBoolf() {
                            @Override
                            public boolean get(int i) {
                                return teamM.get(i).team != 0;
                            }
                        }
                    );
                    for(int i = 0; i < innerResults.size; i++){
                        int id = innerResults.get(i);
                        damageQueue.add(entityId, id, DamageQueue.DamageType.direct);
                    }
                }
            }
        }else{
            cooldown.currentCooldown -= 1;
        }
    }
}
