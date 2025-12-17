package io.bdc.painttd.game.path.node;

import io.bdc.painttd.game.path.*;
import io.bdc.painttd.game.path.var.*;

public abstract class BaseSingleFrameRemapForwardingNode extends Node {
    public RouterV inPort;
    public RouterV outPort;

    protected float remappedFrame;

    public static void registerMeta() {
        // 抽象类不直接注册，由子类实现
    }

    @Override
    public void initVars() {
        inPort = new RouterV();
        outPort = new RouterV();
    }

    @Override
    public void registerVars() {
        inputs.add(inPort);
        outputs.add(outPort);
    }

    /**
     * 缓存版本计算, 缓存决策, 更新计算: 包括更新重映射的帧, 转发计算请求, 转发请求的返回值
     *
     * @return 是否更新了缓存
     */
    @Override
    public abstract boolean calc(float frame);

    /** 转发提供inPort所连接的端口 */
    @Override
    public LinkableVar getSyncOutput(float frame, int targetOutputPort) {
        Node source = nodeGraph.get(inPort.sourceNode);
        if (source == null) return null;

        if (targetOutputPort == 0) {
            //转发获取上游节点的端口
            return source.getSyncOutput(remappedFrame, inPort.sourceOutputPort);
        } else {
            return null;
        }
    }

    @Override
    public Node obtain() {
        return null;
    }

    @Override
    public void free() {
    }

    @Override
    public void reset() {
        inPort.reset();
        outPort.reset();
    }
}
