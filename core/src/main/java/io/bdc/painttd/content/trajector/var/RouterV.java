package io.bdc.painttd.content.trajector.var;

import io.bdc.painttd.content.trajector.*;


public class RouterV extends LinkableVar{
    public RouterV(){
        super(false);
    }

    /** 路由变量标识上游节点转发关系, 无需实际同步 */
    @Override
    public void sync(Net net, float frame){
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
