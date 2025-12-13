package io.bdc.painttd.content.trajector.node;

import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.var.*;

public abstract class BaseSingleFrameRemapForwardingNode extends Node{
    public RouterV inPort = new RouterV();
    public RouterV outPort = new RouterV();
    
    protected float remappedFrame;

    @Override
    public void registerVars(){
        inputs.add(inPort);
        outputs.add(outPort);
    }

    /** 重映射帧, 赋给remappedFrame */
    @Override
    public abstract void calc(float frame);

    @Override
    public LinkableVar getSyncOutput(float frame, int targetOutputPort){
        Node source = net.get(inPort.sourceNode);
        if(source == null) return null;

        if(targetOutputPort == 0){
            //重映射帧
            calc(frame);
            //转发sync请求到上游节点
            return source.getSyncOutput(remappedFrame, inPort.sourceOutputPort);
        }else{
            return null;
        }
    }

    @Override
    public Node obtain(){
        return null;
    }

    @Override
    public void free(){
    }

    @Override
    public void reset(){
        inPort.reset();
        outPort.reset();
    }
}
