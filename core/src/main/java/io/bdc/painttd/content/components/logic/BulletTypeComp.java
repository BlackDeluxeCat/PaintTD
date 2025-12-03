package io.bdc.painttd.content.components.logic;

import com.artemis.annotations.*;
import io.bdc.painttd.content.*;
import io.bdc.painttd.content.components.*;

/**
 * 标记实体发射的子弹类型
 */

@Transient
public class BulletTypeComp extends CopyableComponent{
    public EntityType type;

    public int amt = 1;

    public BulletTypeComp(){
    }

    public BulletTypeComp(int amt, EntityType type){
        this.type = type;
        this.amt = amt;
    }

    @Override
    protected void reset(){
        type = null;
        amt = 1;
    }

    @Override
    public BulletTypeComp copy(CopyableComponent other){
        BulletTypeComp bulletTypeComp = (BulletTypeComp)other;
        this.type = bulletTypeComp.type;
        this.amt = bulletTypeComp.amt;
        return this;
    }

    @Override
    public void refill(CopyableComponent def){
        copy(def);
    }
}
