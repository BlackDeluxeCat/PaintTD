package io.blackdeluxecat.painttd.content.trajector;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import java.util.*;

/**
 * 节点
 * 指定节点轨迹类型, 存储运行状态, 轨迹状态, 子节点索引
 */
public class Node implements Pool.Poolable{
    /** 轨迹处理器类型 */
    public Processor processor;

    public transient Tree tree;
    /** 父节点 */
    public transient @Null Node parent;
    /** 子节点, 由处理器自主检查和使用 */
    public transient @Null Array<Node> children = new Array<>();

    /** 参数 */
    public TrajectoryParameter parameter = new TrajectoryParameter();
    /** 状态 */
    public NodeState complete = NodeState.process;
    public TrajectoryState state = new TrajectoryState();

    public Node(){
    }

    public void setProcessor(Processor p){
        processor = p;
        processor.initial(this);
    }

    public void addChild(Node child){
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

    public @Null Node getChild(int index){
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
    public Node copy(){
        var copy = Node.obtain();
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

    public static Pool<Node> pool = Pools.get(Node.class, 10000);

    public static Node obtain(){
        return pool.obtain();
    }

    public enum NodeState{
        /** 节点可用 */
        process,
        /** 节点完成 */
        complete,
        /** 节点不可用: 意外的空处理器, 树上下文不符合要求, etc. */
        invalid
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
}
