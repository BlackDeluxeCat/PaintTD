package io.bdc.painttd.game.path.var;

public class FloatV extends LinkableVar {
    public float cache;

    public FloatV() {
        super();
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
