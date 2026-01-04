package io.bdc.painttd.game.path.node;

import com.badlogic.gdx.graphics.*;
import io.bdc.painttd.game.*;
import io.bdc.painttd.game.path.*;
import io.bdc.painttd.game.path.context.*;
import io.bdc.painttd.game.path.metadata.*;
import io.bdc.painttd.game.path.var.*;

public class EntityTargetPositionNode extends Node {

    public Vector2V targetPosition;

    public static void registerMeta(){
        NodeMetaRegistry.getInstance().register(EntityTargetPositionNode.class,
            new NodeMeta()
                .setNodeType("entityTargetPosition")
                .setBackgroundColor(Color.TEAL)
                .setIconName("")
                .setCategory("context")
                .addOutputPort(PortMeta.getDefault(Vector2V.class).copy()
                                       .setFieldName("targetPosition")
                                       .setUiBuilder(null))
        );
    }

    @Override
    public void initVars() {
        targetPosition = new Vector2V(id());
    }

    @Override
    public void registerVars() {
        outputs.add(targetPosition);
    }

    @Override
    public boolean calc(float frame) {
        var context = nodeGraph.contexts.getOrNull(EntityContext.class);
        if(context == null || !context.has()) {
            targetPosition.cache.setZero();
            return true;
        }

        var entity = context.getId();
        var pos = Game.utils.targetPosMapper.get(entity);
        if(pos == null) {
            targetPosition.cache.setZero();
            return true;
        }

        targetPosition.cache.set(pos.x, pos.y);

        return true;//TODO cache
    }

    @Override
    public void reset() {
        targetPosition.reset();
    }
}
