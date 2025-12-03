package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;

public class CooldownComp extends CopyableComponent{
    public float cooldown;
    public float currentCooldown;
    public int shootCount;

    public CooldownComp(){
    }

    public CooldownComp(float cooldownTicks){
        this.cooldown = cooldownTicks;
        this.currentCooldown = cooldownTicks;
    }

    @Override
    public CooldownComp copy(CopyableComponent other){
        CooldownComp c = (CooldownComp)other;
        cooldown = c.cooldown;
        currentCooldown = c.currentCooldown;
        shootCount = c.shootCount;
        return this;
    }

    @Override
    protected void reset(){
        currentCooldown = 0;
    }

    @Override
    public void refill(CopyableComponent def){
        CooldownComp cooldownComp = (CooldownComp)def;
        cooldown = cooldownComp.cooldown;
    }
}
