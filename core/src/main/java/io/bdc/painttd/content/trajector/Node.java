package io.bdc.painttd.content.trajector;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

/**
 * 节点
 * 指定节点轨迹类型, 存储运行状态, 轨迹状态, 子节点索引
 */
public class Node implements Pool.Poolable{
    /** 轨迹处理器类型 */
    public Processor processor;

    public transient Tree tree;

    public float cachedFrame = -1;

    public Vector2 shift = new Vector2();

    public FloatArray param = new FloatArray();

    /** 处理器变量的外部链接, 与Processor.inputs一一对应, 每个端口应有0个(null)或1个链接. */
    public Array<Link> links = new Array<>();

    public Node(){
    }

    public void setProcessor(Processor p){
        processor = p;
        processor.initial(this);
    }

    public void setLink(int source, int sourceOutput, int targetInput){
        links.add(Link.obtain().set(source, sourceOutput, targetInput));
    }

    public void update(float frame){
        if(processor == null){
            Gdx.app.debug("TrajectoryTree", "Node has no processor");
            return;
        }

        processor.update(frame, this);
    }

    /** 创建并返回节点的副本. 副本完整地复制参数, 拥有初始化的状态. 通常不需要为复制行为创建回调. */
    public Node copy(){
        var copy = Node.obtain();
        copy.setProcessor(processor);
        copy.param.clear();
        copy.param.addAll(param);
        processor.copyTo(this, copy);
        return copy;
    }

    @Override
    public void reset(){
        processor = null;
        tree = null;
        cachedFrame = -1;
        shift.setZero();
        param.clear();
        links.clear();
    }

    public void free(){
        pool.free(this);
    }

    public static Pool<Node> pool = Pools.get(Node.class, 10000);

    public static Node obtain(){
        return pool.obtain();
    }
}
