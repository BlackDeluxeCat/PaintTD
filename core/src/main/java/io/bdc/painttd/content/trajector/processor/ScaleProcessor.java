package io.bdc.painttd.content.trajector.processor;

import io.bdc.painttd.content.trajector.*;

public class ScaleProcessor extends Processor{
    public static ParamVar scaleX = new ParamVar("scaleX", 0);
    public static ParamVar scaleY = new ParamVar("scaleY", 1);

    public ScaleProcessor(){
        super(1, 2, 0, 0);
    }

    @Override
    public void initial(Node node){
        super.initial(node);
        scaleX.set(node, 2);
        scaleY.set(node, 2);
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
            node.state.shift.set(child.state.shift).scl(scaleX.get(node), scaleY.get(node));
        }
    }
}
