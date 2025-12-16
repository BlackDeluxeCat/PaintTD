package io.bdc.painttd.game.path;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.game.path.var.*;

public abstract class Node implements Pool.Poolable {
    public Array<BaseVar> vars = new Array<>();
    public Array<LinkableVar> inputs = new Array<>();
    public Array<LinkableVar> outputs = new Array<>();

    public transient NodeGraph nodeGraph;

    // 享元元数据
    protected transient NodeMetadata metadata;

    //编辑器元素信息
    public float x, y;
    public boolean minimized, flipIOPosition;

    public Node() {
        registerVars();
    }

    /** 懒惰获取metadata */
    public NodeMetadata getMetadata(){
        if(metadata == null) initMetadata();
        return metadata;
    }

    /**
     * 初始化享元元数据
     */
    protected void initMetadata() {
        this.metadata = NodeMetadataRegistry.getInstance().getMetadata(this.getClass());
    }

    public abstract void registerVars();

    public void setNet(NodeGraph nodeGraph) {
        this.nodeGraph = nodeGraph;
    }

    /**
     * 缓存版本计算, 缓存决策者, input端口同步启动者, 更新计算
     *
     * @return 是否更新了缓存
     */
    public abstract boolean calc(float frame);

    /** 下游请求sync的output端口提供者 可以重写转发端口. */
    public @Null LinkableVar getSyncOutput(float frame, int targetOutputPort) {
        return getOutput(targetOutputPort);
    }

    public LinkableVar getOutput(int index) {
        return outputs.get(index);
    }

    public Node obtainCopy() {
        var newNode = obtain();
        this.copyTo(newNode);
        return newNode;
    }

    public void copyTo(Node copy) {
    }

    public abstract Node obtain();

    public abstract void free();
}