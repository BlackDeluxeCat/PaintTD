package io.bdc.painttd.content.trajector.var;

import io.bdc.painttd.content.trajector.*;


/**
 * 路由变量, 用于节点间连接, 仅持有转发信息, 无任何缓存
 * <p>应在外部使用这些转发信息
 * <p>例: 外部方法使用转发信息. {@code Node upStreamNode = node.nodeGraph.get(inputRouterV.sourceNode);}
 */
public class RouterV extends LinkableVar{
    public RouterV(){
        super(false);
    }

    /** 无逻辑, 不应被调用
     * @return 接口一致性, 无实际意义
     */
    @Override
    public boolean sync(NodeGraph nodeGraph, float frame){
        return false;
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
