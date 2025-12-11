package io.bdc.painttd.content.trajector.processor;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.var.*;

@Deprecated
public class LineProcessor extends Processor{
    //每帧步进
    public PortFloat speed;

    public PortFloat x;
    public PortFloat y;

    public PortVector2 direction;

    public LineProcessor(){
        super(3);
    }

    @Override
    public void registerVars(){
        super.registerVars();
        speed = new PortFloat("speed", 0);

        x = new PortFloat("x", 1);
        y = new PortFloat("y", 2);

        direction = new PortVector2("direction"){
            @Override
            public Vector2 get(Node node){
                return tmp.set(x.asFloat(node), y.asFloat(node));
            }

            @Override
            public void set(Vector2 value, Node node){
                speed.setFloat(value.len(), node);
                x.setFloat(value.x, node);
                y.setFloat(value.y, node);
            }
        };

        inputs.add(speed);
        inputs.add(direction);
    }

    @Override
    public void initial(Node node){
        super.initial(node);
        direction.set(1, 0, node);
        speed.setFloat(0.05f, node);
    }

    @Override
    public void update(float frame, Node node){
        node.shift.set(x.asFloat(node), y.asFloat(node)).setLength(speed.asFloat(node));
    }
}
