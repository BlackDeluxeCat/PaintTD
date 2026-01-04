package io.bdc.painttd.game.path.node;

import com.badlogic.gdx.graphics.*;
import io.bdc.painttd.game.*;
import io.bdc.painttd.game.path.*;
import io.bdc.painttd.game.path.context.*;
import io.bdc.painttd.game.path.metadata.*;
import io.bdc.painttd.game.path.var.*;

public class EntitySetVelocityNode extends Node {

    public Vector2V velocity;

    public static void registerMeta(){
        NodeMetaRegistry.getInstance().register(EntitySetVelocityNode.class,
            new NodeMeta()
                .setNodeType("entitySetVelocity")
                .setBackgroundColor(Color.MAROON)
                .setIconName("")
                .setCategory("trigger")
                .addInputPort(PortMeta.getDefault(Vector2V.class).copy()
                                       .setFieldName("velocity")
                                       .setUiBuilder(null))
        );
    }
    @Override
    public void initVars() {
        velocity = new Vector2V(id());
    }

    @Override
    public void registerVars() {
        inputs.add(velocity);
    }

    @Override
    public boolean calc(float frame) {
        velocity.sync(nodeGraph, frame);

        var context = nodeGraph.contexts.getOrNull(EntityContext.class);
        if(context == null || !context.has()) {
            return false;
        }

        var entity = context.getId();
        var vel = Game.utils.velocityMapper.get(entity);
        if(vel == null) {
            return false;
        }

        vel.set(velocity.cache);

        return false;
    }

    @Override
    public void reset() {
        velocity.reset();
    }
}
