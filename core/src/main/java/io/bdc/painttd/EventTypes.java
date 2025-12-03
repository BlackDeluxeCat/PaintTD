package io.bdc.painttd;

import io.bdc.painttd.utils.*;

/**单例事件*/
public class EventTypes{
    public static BulletSpawnEvent bulletSpawnEvent = new BulletSpawnEvent();
    public static CollideEvent collideEvent = new CollideEvent();
    public static DamageEvent damageEvent = new DamageEvent();
    public static CollideDamageEvent collideDamageEvent = new CollideDamageEvent();
    public static SplashDamageEvent splashDamageEvent = new SplashDamageEvent();
    public static StainSplashDamageEvent stainSplashDamageEvent = new StainSplashDamageEvent();
    public static StainDamageEvent stainDamageEvent = new StainDamageEvent();

    public static class BulletSpawnEvent extends Events.Event{
        public int source;
        public int b;
    }

    public static class CollideEvent extends Events.Event{
        public int source, target;
    }

    public static class DamageEvent extends Events.Event{
        public int source, target;
        public float damage;
    }

    public static class CollideDamageEvent extends Events.Event{
        public int source, target;
    }

    public static class SplashDamageEvent extends Events.Event{
        public int source;
        public float x, y;
        public float damage;
        public float radius;
    }

    public static class StainSplashDamageEvent extends Events.Event{
        public int source;
        public float x, y;
        public int team;
        public float damage;
        public float radius;
    }

    public static class StainDamageEvent extends Events.Event{
        public int source;
        public int team;
        public int target;
        public float damage;
    }
}
