package io.bdc.painttd.content.trajector;

import com.badlogic.gdx.scenes.scene2d.ui.*;

public abstract class Processor{
    /** 初始化节点的数组大小, 合理的值有助于节约内存 */
    public int maxChildrenSize, parameterSize, stateFloatsSize, stateIntsSize;

    //若需要方便调试和外部使用Vars, 推荐创建具名子类

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

    /** 定义每个变量位的用途 */
    public static class Var{
        /** 变量显示名 */
        public String name;
        /** 标识该变量所在索引号 */
        public int index;

        public Var(String name, int i){
            this.name = name;
            this.index = i;
        }
    }

    interface FloatVar{
        float get(Node node);
        void set(Node node, float value);
    }

    public static class ParamVar extends Var implements FloatVar{
        public ParamVar(String name, int i){
            super(name, i);
        }

        public float get(Node node){
            return node.parameter.data.get(index);
        }

        public void set(Node node, float value){
            node.parameter.data.set(index, value);
        }
    }

    public static class StateFVar extends Var implements FloatVar{
        public StateFVar(String name, int i){
            super(name, i);
        }

        public float get(Node node){
            return node.state.floats.get(index);
        }

        public void set(Node node, float value){
            node.state.floats.set(index, value);
        }
    }

    public static class StateIVar extends Var{
        public StateIVar(String name, int i){
            super(name, i);
        }

        public int get(Node node){
            return node.state.ints.get(index);
        }

        public void set(Node node, int value){
            node.state.ints.set(index, value);
        }
    }
}
