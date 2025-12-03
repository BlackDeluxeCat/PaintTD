package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;

public class ArmorComp extends CopyableComponent{
    public float armor;

    public ArmorComp(){
    }

    public ArmorComp(float armor){
        this.armor = armor;
    }

    @Override
    public ArmorComp copy(CopyableComponent other){
        ArmorComp armorComp = (ArmorComp)other;
        armor = armorComp.armor;
        return this;
    }

    @Override
    protected void reset(){
        armor = 0;
    }
}
