package io.bdc.painttd.content.trajector.node;

import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.var.*;

public class TimeOffsetNode extends BaseSingleFrameRemapForwardingNode{
    public FloatV offset = new FloatV(false);

    @Override
    public void registerVars(){
        super.registerVars();
        outputs.add(offset);
    }

    @Override
    public boolean calc(float frame){
        //特别留意时间参考系在不同分支间隔离
        //offset及其分支仍然在未映射的参考系中
        offset.sync(nodeGraph, frame);

        //转发请求的上游分支在重映射的参考系中
        remappedFrame = frame + offset.cache;
        //转发计算请求, 转发请求的返回值
        Node upStream = nodeGraph.get(inPort.sourceNode);
        return upStream.calc(remappedFrame);
    }
}
