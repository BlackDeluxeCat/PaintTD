package io.bdc.painttd.game.path.node;

import com.badlogic.gdx.graphics.*;
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
                .setBackgroundColor(Color.valueOf("#4CAF50"))
                .setIconName("scale_icon")
                .setCategory("transform")
                .addInputPort(PortMeta.getDefault(Vector2V.class).copy()
                                  .setFieldName("shiftI")
                                  .setUiBuilder(new Vector2PortBuilder()
                                                    .setXRange(-100, 100)
                                                    .setYRange(-100, 100)
                                                    .setDecimalPlaces(2)))
                .addInputPort(PortMeta.getDefault(Vector2V.class).copy()
                                  .setFieldName("scaleI")
                                  .setUiBuilder(new Vector2PortBuilder()
                                                    .setXRange(-10f, 10f)
                                                    .setYRange(-10f, 10f)
                                                    .setDecimalPlaces(2)))
                .addOutputPort(PortMeta.getDefault(Vector2V.class).copy()
                                   .setFieldName("shiftO")
                                   .setUiBuilder(null))
        );
    }

    @Override
    public void initVars() {
        scaleI = new Vector2V(id());

        shiftI = new Vector2V(id());

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
    public void reset() {
        scaleI.reset();
        shiftI.reset();
        shiftO.reset();
    }
}
