package io.bdc.painttd.content.trajector.processor;

import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.var.ParamF;

public class LineProcessor extends Processor{
    //每帧步进
    public static ParamF speed = new ParamF("speed", 0);
    public static ParamF x = new ParamF("x", 0);
    public static ParamF y = new ParamF("y", 1);

    @Override
    public void initial(Node node){
        super.initial(node);
        speed.setFloat(0.05f, node);
        x.setFloat(1, node);
        y.setFloat(0, node);
    }

    @Override
    public void update(float deltaTicks, Node node){
        node.state.shift.set(x.asFloat(node), y.asFloat(node)).setLength(speed.asFloat(node));
    }
}
