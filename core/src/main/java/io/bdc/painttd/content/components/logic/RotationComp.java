package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;

public class RotationComp extends CopyableComponent{
    public float rotation;

    public RotationComp(){
    }

    public RotationComp(float rotation){
        this.rotation = rotation;
    }

    @Override
    protected void reset(){
        rotation = 0;
    }

    @Override
    public RotationComp copy(CopyableComponent other){
        RotationComp rotationComp = (RotationComp)other;
        rotation = rotationComp.rotation;
        return this;
    }

    @Override
    public void refill(CopyableComponent def){
        copy(def);
    }
}
