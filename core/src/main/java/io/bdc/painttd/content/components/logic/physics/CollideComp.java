package io.bdc.painttd.content.components.logic.physics;

import io.bdc.painttd.content.components.*;

/**
 * 碰撞组件. 具有该组件的实体将参与碰撞检测, 并产生碰撞事件.
 * 碰撞掩码决定可碰撞的对象
 */
public class CollideComp extends CopyableComponent{
    public static long FLOOR = 1L, OVERLAY = 1L << 1, UNIT = 1L << 2, BUILDING = 1L << 3;

    /**碰撞体类型*/
    public long type;
    /**碰撞掩码*/
    public long mask;
    public boolean solid = false;

    public CollideComp(){
    }

    public CollideComp(long type, boolean solid){
        this.type = type;
        this.solid = solid;
    }

    public CollideComp setCollidesMask(long mask){
        this.mask = mask;
        return this;
    }

    public boolean canCollide(long targetType){
        return (mask & targetType) != 0;
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        CollideComp c = (CollideComp)other;
        this.mask = c.mask;
        this.type = c.type;
        this.solid = c.solid;
        return this;
    }

    @Override
    protected void reset(){
        this.mask = 0;
        this.type = 0;
        this.solid = false;
    }
}
