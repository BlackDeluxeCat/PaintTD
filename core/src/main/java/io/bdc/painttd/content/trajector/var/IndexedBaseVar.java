package io.bdc.painttd.content.trajector.var;

public class IndexedBaseVar extends BaseVar{
    public int index;

    public IndexedBaseVar(String name, int index){
        super(name);
        this.index = index;
    }
}
