package io.bdc.painttd.content.trajector.var;

import io.bdc.painttd.content.trajector.*;

public interface VarInt{
    void setInt(int value, Node node);
    int asInt(Node node);
}
