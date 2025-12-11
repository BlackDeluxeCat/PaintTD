package io.bdc.painttd.content.trajector.var;

import io.bdc.painttd.content.trajector.*;

public interface AsVar<T>{
    void set(T value, Node node);
    T get(Node node);
}
