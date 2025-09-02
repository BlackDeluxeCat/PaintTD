package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.CopyableComponent;

//标记炮塔类型
public class BuildTypeComp extends CopyableComponent {
    static final int NONE = 0;
    public static final int SINGLE_DAMAGE = 1;
    public static final int GROUP_DAMAGE = 2;

    public int type;

    public BuildTypeComp(){
        this.type = 0;
    }

    public BuildTypeComp(int type){
        this.type = type;
    }

    @Override
    public CopyableComponent copy(CopyableComponent other) {
        this.type = ((BuildTypeComp)other).type;
        return this;
    }

    @Override
    protected void reset() {
        this.type = NONE;
    }
}
