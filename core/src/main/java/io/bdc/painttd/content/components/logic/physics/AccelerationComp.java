package io.bdc.painttd.content.components.logic.physics;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.content.components.*;

public class AccelerationComp extends CopyableComponent{
    public float x, y;
    public float z;

    public AccelerationComp(){
    }

    public Vector2 get(Vector2 out){
        return out.set(x, y);
    }

    public void set(Vector2 in){
        x = in.x;
        y = in.y;
    }

    public AccelerationComp z(float z){
        this.z = z;
        return this;
    }

    @Override
    protected void reset(){
        x = 0;
        y = 0;
        z = 0;
    }

    @Override
    public AccelerationComp copy(CopyableComponent other){
        AccelerationComp acc = (AccelerationComp)other;
        x = acc.x;
        y = acc.y;
        z = acc.z;
        return this;
    }
}
