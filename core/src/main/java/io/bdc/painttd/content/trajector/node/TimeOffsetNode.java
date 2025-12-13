package io.bdc.painttd.content.trajector.node;

import io.bdc.painttd.content.trajector.var.*;

public class TimeOffsetNode extends BaseSingleFrameRemapForwardingNode{
    public FloatV offset = new FloatV(false);
    
    @Override
    public void calc(float frame){
        offset.sync(net, frame);    //这里要特别留意, 变量准备阶段应该使用未映射的frame
        remappedFrame = frame + offset.cache;
    }
}
