package io.bdc.painttd.game.path.node;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.game.path.*;
import io.bdc.painttd.game.path.metadata.*;
import io.bdc.painttd.game.path.metadata.builders.*;
import io.bdc.painttd.game.path.var.*;

public class Vector2ScaleNode extends Node {
    public Vector2V scaleI;
    public Vector2V shiftI;
    public Vector2V shiftO;

    /** 静态注册方法 */
    public static void registerMeta() {
        NodeMetaRegistry.getInstance().register(Vector2ScaleNode.class,
            new NodeMeta()
                .setNodeType("vector2Scale")
                .setDisplayNameKey("name")
                .setDescriptionKey("description")
                .setBackgroundColor(Color.valueOf("#4CAF50"))  // 绿色
                .setIconName("scale_icon")
                .setCategory("transform")
                .addInputPort(new PortMeta()
                                  .setFieldName("shiftI")
                                  .setDisplayNameKey("")  // 自动生成
                                  .setColor(Color.valueOf("#FF9800"))  // 橙色
                                  .setIconName("input_vector")
                                  .setUiBuilder(new Vector2PortBuilder()
                                                    .setXRange(-100, 100)
                                                    .setYRange(-100, 100)
                                                    .setDecimalPlaces(2)))
                .addInputPort(new PortMeta()
                                  .setFieldName("scaleI")
                                  .setDisplayNameKey("")  // 自动生成
                                  .setColor(Color.valueOf("#E91E63"))  // 粉色
                                  .setIconName("input_vector")
                                  .setUiBuilder(new Vector2PortBuilder()
                                                    .setXRange(0.1f, 10f)
                                                    .setYRange(0.1f, 10f)
                                                    .setDecimalPlaces(2)))
                .addOutputPort(new PortMeta()
                                   .setFieldName("shiftO")
                                   .setDisplayNameKey("")  // 自动生成
                                   .setColor(Color.valueOf("#2196F3"))  // 蓝色
                                   .setIconName("output_vector"))
        );
    }

    @Override
    public void initVars() {
        scaleI = new Vector2V(id()) {
            @Override
            public void reset() {
                cache.set(1f, 1f);
            }
        };

        shiftI = new Vector2V(id()) {
            @Override
            public void reset() {
                cache.set(0f, 0f);
            }
        };

        shiftO = new Vector2V(id());
    }

    @Override
    public void registerVars() {
        inputs.add(shiftI);
        inputs.add(scaleI);
        outputs.add(shiftO);
    }

    @Override
    public boolean calc(float frame) {
        scaleI.sync(nodeGraph, frame);
        shiftI.sync(nodeGraph, frame);
        shiftO.cache.set(shiftI.cache).scl(scaleI.cache);
        return true;
    }

    @Override
    public Vector2ScaleNode obtain() {
        return pool.obtain();
    }

    private final Pool<Vector2ScaleNode> pool = new ReflectionPool<>(Vector2ScaleNode.class, 100);

    @Override
    public void free() {
        pool.free(this);
    }

    @Override
    public void reset() {
        scaleI.reset();
        shiftI.reset();
        shiftO.reset();
    }
}
