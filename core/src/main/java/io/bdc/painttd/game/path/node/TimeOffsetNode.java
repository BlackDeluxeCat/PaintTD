package io.bdc.painttd.game.path.node;

import com.badlogic.gdx.graphics.*;
import io.bdc.painttd.game.path.*;
import io.bdc.painttd.game.path.metadata.*;
import io.bdc.painttd.game.path.metadata.builders.*;
import io.bdc.painttd.game.path.var.*;

public class TimeOffsetNode extends BaseSingleFrameRemapForwardingNode {
    public FloatV offset;  // 应该是输入端口

    public static void registerMeta() {
        NodeMetaRegistry.getInstance().register(TimeOffsetNode.class,
            new NodeMeta()
                .setNodeType("timeOffset")
                .setDisplayNameKey("name")
                .setDescriptionKey("description")
                .setBackgroundColor(Color.valueOf("#FF9800"))  // 橙色
                .setIconName("time_icon")
                .setCategory("time")
                .addInputPort(new PortMeta()
                                  .setFieldName("inPort")
                                  .setDisplayNameKey("")  // 自动生成
                                  .setColor(Color.valueOf("#9C27B0"))  // 紫色
                                  .setIconName("input_router"))
                .addInputPort(new PortMeta()
                                  .setFieldName("offset")
                                  .setDisplayNameKey("")  // 自动生成
                                  .setColor(Color.valueOf("#FF5722"))  // 深橙色
                                  .setIconName("input_float")
                                  .setUiBuilder(new FloatVTextFieldBuilder()
                                                    .range(-1000, 1000)
                                                    .decimalPlaces(2)
                                                    .placeholder("Offset")))
                .addOutputPort(new PortMeta()
                                   .setFieldName("outPort")
                                   .setDisplayNameKey("")  // 自动生成
                                   .setColor(Color.valueOf("#3F51B5"))  // 靛蓝色
                                   .setIconName("output_router"))
        );
    }

    @Override
    public void initVars() {
        super.initVars();
        offset = new FloatV(true);
    }

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