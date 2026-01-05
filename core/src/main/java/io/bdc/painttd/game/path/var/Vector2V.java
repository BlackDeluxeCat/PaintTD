package io.bdc.painttd.game.path.var;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.game.path.metadata.*;
import io.bdc.painttd.game.path.metadata.builders.*;

public class Vector2V extends LinkableVar {
    public static void registerMeta() {
        PortMeta.setDefault(Vector2V.class, meta -> {
            meta.setColor(Color.BLUE);
            meta.setIconName("icon_vector2");
            meta.setUiBuilder(new Vector2PortBuilder()
                                  .setXRange(-1, 1)
                                  .setYRange(-1, 1)
                                  .setDecimalPlaces(1));
        });
    }

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


    @Override
    public void write(Json json) {
        super.write(json);
        if(sourceInvalid()) {
            json.writeValue("value", cache);
        }
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        if(sourceInvalid()) {
            cache = json.readValue("value", Vector2.class, jsonData);
        }
    }
}
