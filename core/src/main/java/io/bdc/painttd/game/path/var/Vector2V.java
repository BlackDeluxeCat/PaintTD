package io.bdc.painttd.game.path.var;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.game.path.*;

public class Vector2V extends LinkableVar {
    public Vector2 cache = new Vector2();

    public Vector2V(int ownerNode) {
        super(ownerNode);
    }

    @Override
    public void readLink(LinkableVar port) {
        if (port instanceof Vector2V parsedPort) {
            cache.set(parsedPort.cache);
        }
    }

    @Override
    public boolean canLink(LinkableVar source) {
        return source instanceof Vector2V;
    }
}
