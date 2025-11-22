package io.blackdeluxecat.painttd.content;

import java.util.*;

public abstract class TrajectoryProcessor{
    /** 初始化节点的数组大小, 合理的值有助于节约内存 */
    public int maxChildrenSize, parameterSize, stateFloatsSize, stateIntsSize;

    //推荐显式声明parameter及state专用数组的索引号, 提高代码可读性
    //若需要方便调试和外部使用这些索引号, 推荐创建具名子类

    /** 为节点设置处理器, 准备合适的内存空间. 适合为节点参数提供初始值. */
    public void initial(TrajectoryTree.TrajectoryNode node){
        node.parameter.data.setSize(parameterSize);
        node.state.floats.setSize(stateFloatsSize);
        node.state.ints.setSize(stateIntsSize);
        reset(node);
    }

    /** 将节点状态重置. 适合为节点状态提供初始值 */
    public void reset(TrajectoryTree.TrajectoryNode node){
        node.state.ticks = 0f;
    }

    public abstract void update(float deltaTicks, TrajectoryTree.TrajectoryNode node);

    /** 完成的回调 */
    public void complete(TrajectoryTree.TrajectoryNode node){
        node.complete = TrajectoryTree.NodeState.complete;
    }

    /** 创建副本的回调 */
    public void copyTo(TrajectoryTree.TrajectoryNode node, TrajectoryTree.TrajectoryNode copy){
    }
}
