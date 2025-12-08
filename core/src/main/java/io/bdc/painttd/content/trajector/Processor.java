package io.bdc.painttd.content.trajector;

import com.badlogic.gdx.scenes.scene2d.ui.*;

public abstract class Processor{
    /** 初始化节点的数组大小, 合理的值有助于节约内存 */
    public int maxChildrenSize, parameterSize, stateFloatsSize, stateIntsSize;

    //若需要方便调试和外部使用Vars, 推荐创建具名子类

    public Processor(){
        this(0, 0, 0, 0);
    }

    /** 注册变量位 */
    public Processor(int maxChildrenSize, int parameterSize, int stateFloatsSize, int stateIntsSize){
        this.maxChildrenSize = maxChildrenSize;
        this.parameterSize = parameterSize;
        this.stateFloatsSize = stateFloatsSize;
        this.stateIntsSize = stateIntsSize;
    }

    /** 为节点设置处理器, 准备合适的内存空间. 适合为节点参数提供初始值. */
    public void initial(Node node){
        node.parameter.data.setSize(parameterSize);
        node.state.floats.setSize(stateFloatsSize);
        node.state.ints.setSize(stateIntsSize);
        restart(node);
    }

    /** 将节点状态重置. 适合为节点状态提供初始值 */
    public void restart(Node node){
        node.complete = Node.NodeState.process;
        node.state.ticks = 0f;
    }

    public abstract void update(float deltaTicks, Node node);

    /** 节点完成的判断规则. */
    public boolean shouldComplete(Node node){
        return node.parameter.maxTicks <= node.state.ticks;
    }

    /** 节点完成的回调 */
    public void complete(Node node){
        node.complete = Node.NodeState.complete;
    }

    /** 创建副本节点的回调 */
    public void copyTo(Node node, Node copy){
    }

    public void build(Node node, Table table){
    }
}
