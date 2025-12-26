package io.bdc.painttd.game.path.node;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
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
                                  .setColor(PortMeta.getDefaultColor(RouterV.class))
                                  .setIconName("input_router"))
                .addInputPort(new PortMeta()
                                  .setFieldName("offset")
                                  .setDisplayNameKey("")  // 自动生成
                                  .setColor(PortMeta.getDefaultColor(FloatV.class))
                                  .setIconName("input_float")
                                  .setUiBuilder(new FloatVTextFieldBuilder()
                                                    .range(-1000, 1000)
                                                    .decimalPlaces(2)
                                                    .placeholder("Offset")))
                .addOutputPort(new PortMeta()
                                   .setFieldName("outPort")
                                   .setDisplayNameKey("")  // 自动生成
                                   .setColor(PortMeta.getDefaultColor(RouterV.class))
                                   .setIconName("output_router"))
        );
    }

    @Override
    public void initVars() {
        super.initVars();
        offset = new FloatV(id());
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
        if (upStream == null) return false;
        return upStream.calc(remappedFrame);
    }

    @Override
    public void reset() {
        offset.reset();
        inPort.reset();
        outPort.reset();
    }
}