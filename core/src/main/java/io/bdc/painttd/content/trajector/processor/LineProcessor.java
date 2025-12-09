package io.bdc.painttd.content.trajector.processor;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.var.*;

public class LineProcessor extends Processor{
    //每帧步进
    public static ParamF speed = new ParamF("speed", 0);

    public static ParamF x = new ParamF("x", 1);
    public static ParamF y = new ParamF("y", 2);

    public static Vector2V direction = new Vector2V("direction"){
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

    public LineProcessor(){
        super(0, 3, 0, 0);
    }

    @Override
    public void initial(Node node){
        super.initial(node);
        direction.set(1, 0, node);
        speed.setFloat(0.05f, node);
    }

    @Override
    public void update(float deltaTicks, Node node){
        node.state.shift.set(x.asFloat(node), y.asFloat(node)).setLength(speed.asFloat(node));
    }
}
