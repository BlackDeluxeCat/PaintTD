package io.bdc.painttd.systems.utils;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.utils.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.physics.*;
import io.bdc.painttd.content.components.logic.target.*;
import io.bdc.painttd.content.components.marker.*;

public class StaticUtils extends BaseSystem {
    @All
    public EntitySubscription allEntitiesSub;

    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<VelocityComp> velocityMapper;
    public ComponentMapper<AccelerationComp> accelerationMapper;
    public ComponentMapper<HitboxComp> hitboxMapper;
    public ComponentMapper<HealthComp> healthMapper;
    public ComponentMapper<TeamComp> teamMapper;
    public ComponentMapper<TileStainComp> tileStainMapper;
    public ComponentMapper<EntityTypeComp> entityTypeMapper;
    public ComponentMapper<TileComp> tileMapper;
    public ComponentMapper<MarkerComp.Dead> deadMapper;

    public ComponentMapper<TargetSingleComp> targetSingleMapper;
    public ComponentMapper<TargetPosComp> targetPosMapper;

    public void clearWorld() {
        IntBag es = allEntitiesSub.getEntities();
        for (int i = es.size() - 1; i >= 0; i--) {
            world.delete(es.get(i));
        }
    }

    //checkers

    /**
     * @param tgtTeam 染色源自哪一队伍, 设为-1, 将血量清空, 队伍中立
     */
    public void putTileStain(int stainId, int tgtTeam, float healthAmt) {
        TileStainComp tileStain = tileStainMapper.get(stainId);
        HealthComp health = healthMapper.get(stainId);
        TeamComp team = teamMapper.get(stainId);

        if (tileStain != null && health != null && team != null) {
            if (team.team != tgtTeam) {
                health.health -= healthAmt;
                if (health.health <= 0) {
                    team.team = tgtTeam;
                    if (tgtTeam != -1) health.health = -health.health;
                }
            } else {
                health.health = Math.max(health.health, healthAmt);
            }
        }
    }

    public boolean isTeammateOrFriendly(int e1, int e2) {
        var t1 = teamMapper.get(e1);
        var t2 = teamMapper.get(e2);
        if (t1 == null || t2 == null) return true;
        return t1.team == t2.team;
    }

    //setters

    /** 转换source的瞄准组件, 传出数据给projectile的瞄准组件 */
    public void targetCompParser(int source, int projectile) {
        if (source == -1 || projectile == -1) return;
        TargetSingleComp sourceTarget = targetSingleMapper.get(source);
        if (sourceTarget != null) {
            TargetSingleComp targetSingle = targetSingleMapper.get(projectile);
            if (targetSingle != null) {
                targetSingle.targetId = sourceTarget.targetId;
            }

            TargetPosComp targetPos = targetPosMapper.get(projectile);
            if (targetPos != null && sourceTarget.targetId != -1) {
                PositionComp targetPosComp = positionMapper.get(sourceTarget.targetId);
                if (targetPosComp != null) {
                    targetPos.x = targetPosComp.x;
                    targetPos.y = targetPosComp.y;
                }
            }
        }
    }

    public void targetSingleToPos(int singleComp, int posComp) {
        TargetSingleComp single = targetSingleMapper.get(singleComp);
        TargetPosComp pos = targetPosMapper.get(posComp);
        if (single != null && pos != null) {
            PositionComp p = positionMapper.get(single.targetId);
            if (p != null) {
                pos.x = p.x;
                pos.y = p.y;
            }
        }
    }

    public void setProjectileBulletZ(float ticks, int e) {
        VelocityComp v = velocityMapper.get(e);
        AccelerationComp a = accelerationMapper.get(e);
        if (v != null && a != null) {
            v.z = ticks * -a.z / 2f;
        }
    }

    public void setTeam(int e, int team) {
        TeamComp teamComp = teamMapper.get(e);
        if (teamComp != null) teamComp.team = team;
    }

    public void setPosition(int e, float x, float y) {
        PositionComp p = positionMapper.get(e);
        p.x = x;
        p.y = y;
    }

    public boolean isDead(int e) {
        return deadMapper.has(e);
    }

    public void markDead(int e) {
        world.getEntity(e).edit().create(MarkerComp.Dead.class);
    }

    @Override
    protected void processSystem() {
    }
}
