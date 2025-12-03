package io.bdc.painttd.content.components.logic.target;

import io.bdc.painttd.content.components.*;

/**
 * 本组件标识实体的索敌类型.
 * 索敌逻辑和目标存储需要自定义地实现, 以适应各种具体的索敌方式.
 */
public class TargetPriorityComp extends CopyableComponent{
    public static final int CLOSEST = 0, MAX_HEALTH = 1, MIN_HEALTH = 2, MAX_ARMOR = 3;

    public int priority = CLOSEST;

    public TargetPriorityComp(){
    }

    public TargetPriorityComp(int priority){
        this.priority = priority;
    }

    @Override
    protected void reset(){
        priority = CLOSEST;
    }

    @Override
    public TargetPriorityComp copy(CopyableComponent other){
        TargetPriorityComp tpc = (TargetPriorityComp)other;
        priority = tpc.priority;
        return tpc;
    }
}