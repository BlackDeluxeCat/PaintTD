package io.bdc.painttd.game.path.node;

import com.badlogic.gdx.graphics.*;
import io.bdc.painttd.game.*;
import io.bdc.painttd.game.path.*;
import io.bdc.painttd.game.path.context.*;
import io.bdc.painttd.game.path.metadata.*;
import io.bdc.painttd.game.path.var.*;

public class EntityPositionNode extends Node {

    public Vector2V entityPosition;

    public static void registerMeta(){
        NodeMetaRegistry.getInstance().register(EntityPositionNode.class,
            new NodeMeta()
                .setNodeType("entityPosition")
                .setBackgroundColor(Color.TEAL)
                .setIconName("")
                .setCategory("context")
                .addOutputPort(PortMeta.getDefault(Vector2V.class).copy()
                                       .setFieldName("entityPosition")
                                       .setUiBuilder(null))
        );
    }

    @Override
    public void initVars() {
        entityPosition = new Vector2V(id());
    }

    @Override
    public void registerVars() {
        outputs.add(entityPosition);
    }

    @Override
    public boolean calc(float frame) {
        var context = nodeGraph.contexts.getOrNull(EntityContext.class);
        if(context == null || !context.has()) {
            entityPosition.cache.setZero();
            return true;
        }

        var entity = context.getId();
        var pos = Game.utils.positionMapper.get(entity);
        if(pos == null) {
            entityPosition.cache.setZero();
            return true;
        }

        pos.out(entityPosition.cache);

        return true;//TODO cache
    }

    @Override
    public void reset() {
        entityPosition.reset();
    }
}
