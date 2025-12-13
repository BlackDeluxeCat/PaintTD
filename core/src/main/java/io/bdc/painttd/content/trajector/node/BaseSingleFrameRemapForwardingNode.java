package io.bdc.painttd.content.trajector.node;

import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.var.*;

public abstract class BaseSingleFrameRemapForwardingNode extends Node{
    public RouterV inPort = new RouterV();
    public RouterV outPort = new RouterV();

    @Override
    public void registerVars(){
        inputs.add(inPort);
        outputs.add(outPort);
    }

    @Override
    public void calc(float frame){
    }

    @Override
    public void syncLink(LinkableVar downStream, float frame, int targetOutputPort){
        calc(frame);
        if(targetOutputPort == 0){
            net.get(inPort.sourceNode).syncLink(downStream, transform(frame), inPort.sourceOutputPort);
        }
    }

    @Override
    public Node obtain(){
        return null;
    }

    @Override
    public void free(){

    }

    public float transform(float frame){
        return frame;
    }

    @Override
    public void reset(){

    }
}
