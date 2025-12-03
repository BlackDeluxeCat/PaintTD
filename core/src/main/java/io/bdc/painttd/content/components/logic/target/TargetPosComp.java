package io.bdc.painttd.content.components.logic.target;

import io.bdc.painttd.content.components.*;

public class TargetPosComp extends CopyableComponent{
    public float x, y;

    public TargetPosComp(){
    }

    public TargetPosComp(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    protected void reset(){
        x = 0;
        y = 0;
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        TargetPosComp o = (TargetPosComp)other;
        x = o.x;
        y = o.y;
        return this;
    }
}
