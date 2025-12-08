package io.bdc.painttd.content.trajector.processor;

import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.var.ParamF;

public class ScaleProcessor extends Processor{
    public static ParamF scaleX = new ParamF("scaleX", 0);
    public static ParamF scaleY = new ParamF("scaleY", 1);

    public ScaleProcessor(){
        super(1, 2, 0, 0);
    }

    @Override
    public void initial(Node node){
        super.initial(node);
        scaleX.setFloat(2, node);
        scaleY.setFloat(2, node);
    }

    @Override
    public boolean shouldComplete(Node node){
        if(node.children.size <= 0) return true;
        var child = node.children.get(0);
        return child == null || child.complete == Node.NodeState.complete;
    }

    @Override
    public void update(float deltaTicks, Node node){
        if(node.children.size <= 0) return;
        Node child = node.children.get(0);
        if(child != null){
            child.update(deltaTicks);
            node.state.shift.set(child.state.shift).scl(scaleX.asFloat(node), scaleY.asFloat(node));
        }
    }
}
