package io.bdc.painttd.content.trajector.var;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.trajector.*;

public abstract class LinkableVar extends BaseVar implements Pool.Poolable{
    public int sourceNode;
    public int sourceOutputPort;
    public boolean cacheValue;
    public float cachedFrame;

    public LinkableVar(boolean cacheValue){
        this.cacheValue = cacheValue;
    }

    /** 同步决策者, 判断是否需要向上游节点请求同步. */
    public void sync(Net net, float frame){
        if(sourceNode == -1 || sourceOutputPort == -1) return;
        Node source = net.get(sourceNode);
        if(source == null) return;

        //禁用缓存强制重新同步 || 缓存帧与本次请求不同需要重新同步
        if(!cacheValue || cachedFrame != frame){
            //获取上游节点的输出端口
            LinkableVar sourcePort = source.getSyncOutput(frame, sourceOutputPort);
            
            //上游节点转发与映射失败时返回null, 结束本次同步
            if(sourcePort == null) return;
            
            //读取上游节点的输出端口
            readLink(sourcePort);
            
            //同步成功后更新缓存
            cachedFrame = frame;
        }
    }

    /** 做读取 */
    public abstract void readLink(@Null LinkableVar port);

    /** 对编辑器中创建link做有效性检查 */
    public abstract boolean canLink(LinkableVar port);

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
