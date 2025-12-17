package io.bdc.painttd.game.path.metadata;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.bdc.painttd.game.path.var.*;

public interface LinkableVarBuilder<T extends LinkableVar> {
    void build(Table cont, T var);
}
