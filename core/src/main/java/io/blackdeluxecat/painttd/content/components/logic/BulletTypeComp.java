package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.*;
import io.blackdeluxecat.painttd.content.components.*;

/**标记实体发射的子弹类型*/
public class BulletTypeComp extends CopyableComponent{
    public EntityType type;

    public BulletTypeComp(){}

    public BulletTypeComp(EntityType type){
        this.type = type;
    }

    @Override
    protected void reset(){
        type = null;
    }

    @Override
    public BulletTypeComp copy(CopyableComponent other){
        BulletTypeComp bulletTypeComp = (BulletTypeComp) other;
        this.type = bulletTypeComp.type;
        return this;
    }
}
