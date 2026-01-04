package io.bdc.painttd.game.path.node;

import com.badlogic.gdx.graphics.*;
import io.bdc.painttd.game.path.*;
import io.bdc.painttd.game.path.metadata.*;
import io.bdc.painttd.game.path.metadata.builders.*;
import io.bdc.painttd.game.path.var.*;

public class Vector2AddNode extends Node {
    public Vector2V a;
    public Vector2V b;
    public Vector2V out;

    /** 静态注册方法 */
    public static void registerMeta() {
        NodeMetaRegistry.getInstance().register(Vector2AddNode.class,
            new NodeMeta()
                .setNodeType("vector2Add")
                .setBackgroundColor(Color.valueOf("#4CAF50"))
                .setIconName("")
                .setCategory("transform")
                .addInputPort(PortMeta.getDefault(Vector2V.class).copy()
                                      .setFieldName("a")
                                      .setUiBuilder(new Vector2PortBuilder()
                                                        .setXRange(-100, 100)
                                                        .setYRange(-100, 100)
                                                        .setDecimalPlaces(2)))
                .addInputPort(PortMeta.getDefault(Vector2V.class).copy()
                                      .setFieldName("b")
                                      .setUiBuilder(new Vector2PortBuilder()
                                                        .setXRange(-100f, 100f)
                                                        .setYRange(-100f, 100f)
                                                        .setDecimalPlaces(2)))
                .addOutputPort(PortMeta.getDefault(Vector2V.class).copy()
                                       .setFieldName("out")
                                       .setUiBuilder(null))
        );
    }

    @Override
    public void initVars() {
        a = new Vector2V(id());
        b = new Vector2V(id());
        out = new Vector2V(id());
    }

    @Override
    public void registerVars() {
        inputs.add(a);
        inputs.add(b);
        outputs.add(out);
    }

    @Override
    public boolean calc(float frame) {
        a.sync(nodeGraph, frame);
        b.sync(nodeGraph, frame);
        out.cache.set(a.cache).add(b.cache);
        return true;
    }

    @Override
    public void reset() {
        a.reset();
        b.reset();
        out.reset();
    }
}
