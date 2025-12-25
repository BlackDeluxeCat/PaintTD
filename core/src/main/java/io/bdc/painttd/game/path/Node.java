package io.bdc.painttd.game.path;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.game.path.metadata.*;
import io.bdc.painttd.game.path.var.*;

public abstract class Node implements Pool.Poolable {
    public Array<BaseVar> vars = new Array<>();
    public Array<LinkableVar> inputs = new Array<>();
    public Array<LinkableVar> outputs = new Array<>();

    public transient NodeGraph nodeGraph;

    protected transient NodeMeta meta;

    //编辑器元素信息
    public float x, y;
    public boolean minimized, flipIOPosition;

    public Node() {
        initVars();
        registerVars();
    }

    public int id(){
        return nodeGraph == null ? -1 : nodeGraph.get(this);
    }

    /** 懒惰获取metadata */
    public NodeMeta getMeta() {
        if (meta == null) initMeta();
        return meta;
    }

    public PortMeta getInputMeta(int index) {
        return getMeta().getInputPort(index);
    }

    public PortMeta getOutputMeta(int index) {
        return getMeta().getOutputPort(index);
    }

    /**
     * 初始化享元元数据
     */
    protected void initMeta() {
        this.meta = NodeMetaRegistry.getInstance().getMeta(this.getClass());
    }

    /** 初始化变量字段 */
    public abstract void initVars();

    /** 注册变量到inputs/outputs数组 */
    public abstract void registerVars();

    public void setNodeGraph(NodeGraph nodeGraph) {
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

    public LinkableVar getInput(int index) {
        return inputs.get(index);
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