package io.bdc.painttd.content.trajector.var;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.trajector.*;

public interface Linkable{
    void readLink(Link link, Node self);

    @Null
    Linkable parseLink(Link link, Node self);

    default boolean canParseLink(Link link, Node self){
        return parseLink(link, self) != null;
    }
}