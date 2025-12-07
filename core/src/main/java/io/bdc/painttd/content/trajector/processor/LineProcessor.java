package io.bdc.painttd.content.trajector.processor;

import io.bdc.painttd.content.trajector.*;

public class LineProcessor extends Processor{
    //每帧步进
    public static ParamVar speed = new ParamVar("speed", 0);
    public static ParamVar directionX = new ParamVar("directionX", 0);
    public static ParamVar directionY = new ParamVar("directionY", 1);

    @Override
    public void initial(Node node){
        super.initial(node);
        speed.set(node, 0.05f);
        directionX.set(node, 1);
        directionY.set(node, 0);
    }

    @Override
    public void update(float deltaTicks, Node node){
        node.state.shift.set(directionX.get(node), directionY.get(node)).setLength(speed.get(node));
    }
}
