package io.bdc.painttd.content.trajector.var;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.content.trajector.*;

public interface VarVector2 extends AsVar<Vector2>, Linkable{
    @Override
    default void readLink(Link link, Node self){
        var v = parseLink(link, self);
        if(v != null){
            Node source = self.tree.get(link.source);
            set(v.get(source), self);
        }
    }

    @Override
    default VarVector2 parseLink(Link link, Node self){
        if(self.tree.get(link.source).processor.outputs.get(link.sourceOutputPort) instanceof VarVector2 v){
            return v;
        }
        return null;
    }
}
