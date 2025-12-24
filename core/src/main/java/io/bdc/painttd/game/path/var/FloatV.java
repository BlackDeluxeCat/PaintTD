package io.bdc.painttd.game.path.var;

import io.bdc.painttd.game.path.*;

public class FloatV extends LinkableVar {
    public float cache;

    public FloatV(int ownerNode) {
        super(ownerNode);
    }

    @Override
    public void readLink(LinkableVar port) {
        if (port instanceof FloatV parsedPort) {
            cache = parsedPort.cache;
        }
    }

    @Override
    public boolean canLink(LinkableVar source) {
        return source instanceof FloatV;
    }
}
