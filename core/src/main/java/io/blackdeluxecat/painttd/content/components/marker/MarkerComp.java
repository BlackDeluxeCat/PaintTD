package io.blackdeluxecat.painttd.content.components.marker;

import io.blackdeluxecat.painttd.content.components.*;

public class MarkerComp extends CopyableComponent{

    public MarkerComp(){}

    @Override
    public CopyableComponent copy(CopyableComponent other){
        return this;
    }

    @Override
    protected void reset(){}

    /** 该组件标记的实体视为已失效 */
    public static class Dead extends MarkerComp{
        public Dead(){}
    }

    public static class Tile extends MarkerComp{
        public Tile(){}
    }

    /** 该组件标记的实体被自动插入四叉树 */
    public static class UseQuadTree extends MarkerComp{
        public UseQuadTree(){}
    }

    /** 该组件标记的实体在放置时自动对齐网格 */
    public static class PlaceSnapGrid extends MarkerComp{
        public PlaceSnapGrid(){}
    }
}
