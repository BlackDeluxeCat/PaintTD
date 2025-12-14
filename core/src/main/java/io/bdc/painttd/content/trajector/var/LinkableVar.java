package io.bdc.painttd.content.trajector.var;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.trajector.*;

public abstract class LinkableVar extends BaseVar implements Pool.Poolable{
    public int sourceNode = -1;
    public int sourceOutputPort = -1;
    public boolean cacheValue;
    public LinkableVar(boolean cacheValue){
        this.cacheValue = cacheValue;
    }

    /** input端口同步行为发起者, 向上游节点请求同步.
     * @return 是否上游节点更新且同步到本地
     */
    public boolean sync(Net net, float frame){
        if(sourceNode == -1 || sourceOutputPort == -1) return false;
        Node source = net.get(sourceNode);
        if(source == null) return false;

        //请求上游节点检查(和可能的计算), 返回true即上游计算更新, 需要同步
        //可能由上游转发请求
        if(source.calc(frame)){
            //获取上游节点的输出端口. 可能由上游转发端口
            LinkableVar sourcePort = source.getSyncOutput(frame, sourceOutputPort);
            if(sourcePort == null) return false;
            //读取上游节点的输出端口
            readLink(sourcePort);
            return true;
        }
        
        return false;
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
        def();
    }

    public void def(){}
}
