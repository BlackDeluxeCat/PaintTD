package io.bdc.painttd.content.components.marker;

import com.artemis.annotations.*;
import io.bdc.painttd.content.components.*;

@Transient
public class MarkerComp extends CopyableComponent {
    public MarkerComp() {
    }

    @Override
    public CopyableComponent copy(CopyableComponent other) {
        return this;
    }

    @Override
    protected void reset() {
    }

    /** 标记的实体视为已失效 */
    public static class Dead extends MarkerComp {
        public Dead() {
        }
    }

    public static class OnCollideDead extends MarkerComp {
        public OnCollideDead() {
        }
    }

    /** 标记的实体被自动插入四叉树 */
    @Transient
    public static class UseQuadTree extends MarkerComp {
        public UseQuadTree() {
        }
    }

    /** 标记的实体响应光标悬浮 */
    @Transient
    public static class Hoverable extends MarkerComp {
        public Hoverable() {
        }
    }

    /** 标记的实体在放置时自动对齐网格 */
    @Transient
    public static class PlaceSnapGrid extends MarkerComp {
        public PlaceSnapGrid() {
        }
    }

    @Transient
    public static class HitboxScaleByHealth extends MarkerComp {
        public HitboxScaleByHealth() {
        }
    }

    /** 标记的实体攻击方式为碰撞 */
    @Transient
    public static class CollideAttacker extends MarkerComp {
        public CollideAttacker() {
        }
    }

    /** 标记的实体攻击方式为射击 */
    @Transient
    public static class ShootAttacker extends MarkerComp {
        public ShootAttacker() {
        }
    }

    @Transient
    public static class BulletHoming extends MarkerComp {
        public BulletHoming() {
        }
    }

    @Transient
    public static class BulletProjected extends MarkerComp {
        public BulletProjected() {
        }
    }
}
