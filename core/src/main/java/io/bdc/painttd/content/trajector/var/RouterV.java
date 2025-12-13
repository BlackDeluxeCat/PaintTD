package io.bdc.painttd.content.trajector.var;

import io.bdc.painttd.content.trajector.*;


public class RouterV extends LinkableVar{
    public RouterV(){
        super(false);
    }

    /** 维护路由信息, 代理触发上游更新 */
    @Override
    public void sync(Net net, float frame){
        if(sourceNode == -1 || sourceOutputPort == -1) return;
        Node source = net.get(sourceNode);
        if(source == null) return;

        if(!cacheValue || cachedFrame != frame){
            source.syncLink(this, frame, sourceOutputPort);
            cachedFrame = frame;
        }
    }

    /** 路由变量不缓存实际值 */
    @Override
    public void readLink(LinkableVar port){
    }

    /** 任何类型都可链接 */
    @Override
    public boolean canLink(LinkableVar port){
        return true;
    }
}
