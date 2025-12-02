package io.blackdeluxecat.painttd.content.trajector;

import com.badlogic.gdx.scenes.scene2d.ui.*;

public abstract class Processor{
    /** 初始化节点的数组大小, 合理的值有助于节约内存 */
    public int maxChildrenSize, parameterSize, stateFloatsSize, stateIntsSize;

    //推荐显式声明parameter及state专用数组的索引号, 提高代码可读性
    //若需要方便调试和外部使用这些索引号, 推荐创建具名子类

    public Processor(){
        this(0, 0, 0, 0);
    }

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
        reset(node);
    }

    /** 将节点状态重置. 适合为节点状态提供初始值 */
    public void reset(Node node){
        node.state.ticks = 0f;
    }

    public abstract void update(float deltaTicks, Node node);

    /** 完成的回调 */
    public void complete(Node node){
        node.complete = Node.NodeState.complete;
    }

    /** 创建副本的回调 */
    public void copyTo(Node node, Node copy){
    }

    public void build(Node node, Table table){
    }

    public static class Var{
        /** 变量显示名 */
        public String name;
        /** 标识该变量所在索引号 */
        public int v;
        /** 标识该变量所在数组 */
        public VarType type;

        public Var(String name, int v, VarType type){
            this.name = name;
            this.v = v;
            this.type = type;
        }
    }

    public enum VarType{
        parameter, stateFloat, stateInt;
    }
}
