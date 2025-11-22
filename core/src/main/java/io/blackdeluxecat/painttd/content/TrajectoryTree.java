package io.blackdeluxecat.painttd.content;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import java.util.*;

/**
 * 轨迹树, 维护扁平化的节点数组
 * 树结构由{@link TrajectoryTree.TrajectoryNode}{@code .children}表示
 */

public class TrajectoryTree{
    /** 根节点索引 */
    public int rootIndex;
    public Array<TrajectoryNode> nodes;

    public TrajectoryTree(){
        nodes = new Array<>();
    }

    public TrajectoryNode add(TrajectoryProcessor type, @Null TrajectoryNode parent){
        TrajectoryNode newNode = TrajectoryNode.obtain();
        newNode.setProcessor(type);
        newNode.tree = this;
        nodes.add(newNode);
        if(parent == null){
            rootIndex = nodes.size - 1;
            //TODO更新相关子节点
        }else{
            //子节点可用的情况下, 添加
            parent.addChild(newNode);
        }
        return newNode;
    }

    /** 从树中移除一个节点, 其子节点转移到其父节点上. */
    public void remove(TrajectoryNode node){
        if(node == null) return;
        int removed = nodes.indexOf(node, true);
        if(removed == -1) return;
        nodes.removeIndex(removed);
        //尝试将所有子节点接到父节点末尾
        for(var child : node.children){
            node.parent.addChild(child);
        }
        node.free();
    }

    public void clear(){
        for(var node : nodes){
            node.free();
        }
        nodes.clear();
        rootIndex = 0;
    }

    public void update(float delta){
        if(nodes == null) return;
        if(nodes.size == 0) return;
        if(rootIndex >= nodes.size) return;
        nodes.get(rootIndex).update(delta);
    }

    public Vector2 getShift(){
        return nodes.get(rootIndex).state.shift;
    }

    /**
     * 节点
     * 指定节点轨迹类型, 存储运行状态, 轨迹状态, 子节点索引
     * */
    public static class TrajectoryNode implements Pool.Poolable{
        /** 轨迹处理器类型 */
        public TrajectoryProcessor processor;

        public transient TrajectoryTree tree;
        /** 父节点 */
        public transient @Null TrajectoryNode parent;
        /** 子节点, 由处理器自主检查和使用 */
        public transient @Null Array<TrajectoryNode> children = new Array<>();

        /** 参数 */
        public TrajectoryParameter parameter = new TrajectoryParameter();
        /** 状态 */
        public NodeState complete = NodeState.process;
        public TrajectoryState state = new TrajectoryState();

        public TrajectoryNode(){}

        public void setProcessor(TrajectoryProcessor p){
            processor = p;
            processor.initial(this);
        }

        public void addChild(TrajectoryNode child){
            //可用性
            if(processor.maxChildrenSize <= 0) return;
            if(children.size >= processor.maxChildrenSize) return;
            //添加子节点, 更新关系
            children.add(child);
            child.parent = this;
        }

        public void update(float delta){
            if(complete == NodeState.complete || complete == NodeState.invalid) return;
            if(processor == null){
                Gdx.app.debug("TrajectoryTree", "Node has no processor");
                complete = NodeState.invalid;
                return;
            }
            if(complete == NodeState.process && state.ticks < parameter.maxTicks){
                processor.update(delta, this);
                state.ticks += delta;
            }

            if(complete == NodeState.process && state.ticks >= parameter.maxTicks){
                processor.complete(this);
            }
        }

        public @Null TrajectoryNode getChild(int index){
            if(index < 0 || index >= children.size) return null;
            return children.get(index);
        }

        /** Get parameters */
        public float gp(int index){
            return parameter.data.get(index);
        }

        /** Get states.floats */
        public float gsf(int index){
            return state.floats.get(index);
        }

        /** Get states.ints */
        public int gsi(int index){
            return state.ints.get(index);
        }

        /** Set states.floats */
        public void ssf(int index, float value){
            state.floats.set(index, value);
        }

        /** Set states.ints */
        public void ssi(int index, int value){
            state.ints.set(index, value);
        }

        /** 创建并返回节点的副本. 副本完整地复制参数, 拥有初始化的状态. 通常不需要为复制行为创建回调. */
        public TrajectoryNode copy(){
            var copy = TrajectoryNode.obtain();
            copy.setProcessor(processor);
            copy.parameter.maxTicks = parameter.maxTicks;
            copy.parameter.data.addAll(parameter.data);
            processor.copyTo(this, copy);
            return copy;
        }

        @Override
        public void reset(){
            processor = null;
            tree = null;
            parent = null;
            children.clear();
            complete = NodeState.process;
            parameter.reset();
            state.reset();
        }

        public void free(){
            pool.free(this);
        }

        public static Pool<TrajectoryNode> pool = Pools.get(TrajectoryNode.class, 10000);

        public static TrajectoryNode obtain(){
            return pool.obtain();
        }
    }

    public static class TrajectoryParameter implements Pool.Poolable{
        public float maxTicks = 300;
        public FloatArray data = new FloatArray(0);

        public TrajectoryParameter(){}

        @Override
        public void reset(){
            maxTicks = Float.MAX_VALUE;
            Arrays.fill(data.items, 0);
            data.clear();
        }
    }

    public static class TrajectoryState implements Pool.Poolable{
        /** 增量位移 */
        public Vector2 shift = new Vector2();
        /** 存在时间 */
        public float ticks;
        /** 自定义状态 */
        public FloatArray floats = new FloatArray(0);
        public IntArray ints = new IntArray(0);

        public TrajectoryState(){}

        @Override
        public void reset(){
            shift.setZero();
            ticks = 0;
            Arrays.fill(floats.items, 0);
            floats.clear();
            Arrays.fill(ints.items, 0);
            ints.clear();
        }
    }

    public enum NodeState{
        /** 节点可用 */
        process,
        /** 节点完成 */
        complete,
        /** 节点不可用 */
        invalid
    }
}
