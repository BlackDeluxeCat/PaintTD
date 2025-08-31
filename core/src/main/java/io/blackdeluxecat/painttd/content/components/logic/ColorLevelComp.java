package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class ColorLevelComp extends CopyableComponent{
    public int level;

    public ColorLevelComp(){}

    public ColorLevelComp(int level){
        this.level = level;
    }

    @Override
    public ColorLevelComp copy(CopyableComponent other){
        ColorLevelComp colorLevelComp = (ColorLevelComp)other;
        level = colorLevelComp.level;
        return this;
    }

    @Override
    protected void reset(){
        level = 0;
    }
}
