package io.bdc.painttd.content.trajector.var;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.trajector.*;

public abstract class LinkableVar extends BaseVar implements Linkable, Pool.Poolable{
    public int sourceNode;
    public int sourceOutputPort;
    public boolean cacheValue;
    public float cachedFrame;

    public LinkableVar(boolean cacheValue){
        this.cacheValue = cacheValue;
    }

    /** 从链接中同步到变量内部缓存. */
    public void sync(Net net, float frame){
        if(sourceNode == -1 || sourceOutputPort == -1) return;
        Node source = net.get(sourceNode);
        if(source == null) return;

        //禁用缓存强制重新同步 || 缓存帧与本次请求不同需要重新同步
        //重新计算上游节点, 同步远端数据到自身缓存
        if(!cacheValue || cachedFrame != frame){
            source.calc(frame);
            cachedFrame = frame;
            syncLink(source.getOutput(sourceOutputPort));
        }
    }

    /** 重置变量. */
    @Override
    public void reset(){
        sourceNode = -1;
        sourceOutputPort = -1;
        cachedFrame = -1;
        def();
    }

    public void def(){}
}
