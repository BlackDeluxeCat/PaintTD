package io.blackdeluxecat.painttd.content;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

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

    public void add(Trajectory.TrajectoryProcessor type, @Null TrajectoryNode parent){
        TrajectoryNode newNode = TrajectoryNode.obtain();
        newNode.processor = type;
        if(parent == null) nodes.add(new TrajectoryNode());
        nodes.add(parent);
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
        public Trajectory.TrajectoryProcessor processor;

        public transient TrajectoryTree tree;
        /** 父节点 */
        public transient @Null TrajectoryNode parent;
        /** 子节点, 由处理器自主解释 */
        public transient @Null Array<TrajectoryNode> children;

        /** 参数 */
        public TrajectoryParameter parameter = new TrajectoryParameter();
        /** 状态 */
        public NodeState complete = NodeState.ready;
        public TrajectoryState state = new TrajectoryState();

        public TrajectoryNode(){}

        public void addChild(TrajectoryNode child){
            //可用性
            if(child == null || !processor.hasChildren) return;
            //添加子节点
            if(children == null) children = new Array<>();
            children.add(child);
            child.parent = this;
        }

        public void update(float delta){
            if(complete == NodeState.invalid) return;
            if(processor == null){
                //这样的节点显然有问题
                Gdx.app.debug("TrajectoryTree", "Node has no processor");
                complete = NodeState.invalid;
                return;
            }
            if(state.ticks++ >= parameter.maxTicks){
                complete = NodeState.complete;
                return;
            }
            if(complete == NodeState.complete) return;
            processor.update(delta, this);
        }

        public TrajectoryNode copy(){
            var copy = TrajectoryNode.obtain();
            copy.processor = processor;
            copy.parameter.maxTicks = parameter.maxTicks;
            copy.parameter.data.addAll(parameter.data);
            copy.state = state;
            processor.copyTo(this, copy);
            return copy;
        }

        @Override
        public void reset(){
            processor = null;
            tree = null;
            parent = null;
            children.clear();
            complete = NodeState.invalid;
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
        public float maxTicks = Float.MAX_VALUE;
        public FloatArray data = new FloatArray();

        public TrajectoryParameter(){}

        @Override
        public void reset(){
            maxTicks = Float.MAX_VALUE;
            data.clear();
        }
    }

    public static class TrajectoryState implements Pool.Poolable{
        /** 增量位移 */
        public Vector2 shift = new Vector2();
        /** 存在时间 */
        public float ticks;
        /** 自定义状态 */
        public FloatArray floats = new FloatArray();
        public IntArray ints = new IntArray();

        public TrajectoryState(){}

        @Override
        public void reset(){
            shift.setZero();
            ticks = 0;
            floats.clear();
            ints.clear();
        }
    }

    public enum NodeState{
        /** 节点未初始化 */
        invalid,
        /** 节点就绪 */
        ready,
        /** 节点处理中 */
        processing,
        /** 节点结束 */
        complete,
        /** 节点被跳过 */
        skipped,
        /** 节点其他状态 */
        other
    }
}
