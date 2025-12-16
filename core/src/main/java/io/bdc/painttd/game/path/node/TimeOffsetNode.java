package io.bdc.painttd.game.path.node;

import io.bdc.painttd.game.path.*;
import io.bdc.painttd.game.path.var.*;
@NodeInfo(
    nodeType = "timeOffset",
    displayName = "name",
    description = "description",
    backgroundColor = "#FF9800",  // 橙色，表示时间相关节点
    icon = "time_icon",
    category = "time",
    inputPorts = {
        @NodeInfo.Port(
            fieldName = "inPort",
            color = "#9C27B0",  // 紫色
            icon = "input_router"
        ),
        @NodeInfo.Port(
            fieldName = "offset",
            color = "#FF5722",  // 深橙色
            icon = "output_float"
        )
    },
    outputPorts = {
        @NodeInfo.Port(
            fieldName = "outPort",
            color = "#3F51B5",  // 靛蓝色
            icon = "output_router"
        )
    }
)
public class TimeOffsetNode extends BaseSingleFrameRemapForwardingNode {
    public FloatV offset = new FloatV(false);

    @Override
    public void registerVars() {
        super.registerVars();
        inputs.add(offset);
    }

    @Override
    public boolean calc(float frame) {
        //特别留意时间参考系在不同分支间隔离
        //offset及其分支仍然在未映射的参考系中
        offset.sync(nodeGraph, frame);

        //转发请求的上游分支在重映射的参考系中
        remappedFrame = frame + offset.cache;
        //转发计算请求, 转发请求的返回值
        Node upStream = nodeGraph.get(inPort.sourceNode);
        return upStream.calc(remappedFrame);
    }
}