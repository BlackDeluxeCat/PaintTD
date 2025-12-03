package io.bdc.painttd.content.components;

import com.artemis.*;

/**
 * 可拷贝可序列化的组件
 * <p>组件默认深度序列化, 也可继承{@link com.badlogic.gdx.utils.Json.Serializable}自定义序列化
 * <p>不序列化的组件, 使用{@link com.artemis.annotations.Transient}注解
 * <p>反序列化实体将按原型{@link io.bdc.painttd.content.EntityType}逐组件{@link #refill(CopyableComponent)}重建.
 */
public abstract class CopyableComponent extends PooledComponent{

    /**将同类型组件{@code other}的属性拷贝到本组件.*/
    public abstract CopyableComponent copy(CopyableComponent other);

    /**反序列化后, 初始化属性字段.*/
    public void refill(CopyableComponent def){
    }
}
