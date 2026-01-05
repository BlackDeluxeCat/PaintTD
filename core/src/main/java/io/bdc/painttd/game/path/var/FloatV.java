package io.bdc.painttd.game.path.var;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.game.path.metadata.*;
import io.bdc.painttd.game.path.metadata.builders.*;

public class FloatV extends LinkableVar {
    public static void registerMeta() {
        PortMeta.setDefault(FloatV.class, meta -> {
            meta.setColor(Color.CYAN);
            meta.setIconName("icon_float");
            meta.setUiBuilder(new FloatVTextFieldBuilder()
                                  .range(0, 1)
                                  .decimalPlaces(2));
        });
    }

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
            cache = json.readValue("value", float.class, jsonData);
        }
    }
}
