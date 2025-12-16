package io.bdc.painttd.content;

import com.artemis.*;
import com.artemis.utils.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.reflect.*;
import io.bdc.painttd.content.components.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.game.*;

import static io.bdc.painttd.game.Game.*;

public class EntityType {
    public String id;

    /** 默认组件包。默认组件使用构造函数创建，不需要池化管理。由{@link #create()}创建的实体将拷贝一份受到池化管理的组件包 */
    public OrderedMap<Class<? extends CopyableComponent>, CopyableComponent> def = new OrderedMap<>();
    public Bag<String> groups = new Bag<>();
    public String category;

    /** 推荐在匿名构造函数编辑默认组件包 */
    public EntityType(String id, String category) {
        this.id = id;
        this.category = category;
        add(new EntityTypeComp(id));
        EntityTypes.register(this);
    }

    public EntityType(String id, EntityType prototype, String category) {
        this(id, category);
        copyType(prototype);
        get(EntityTypeComp.class).type = id;
    }

    public void addGroup(String group) {
        if (!groups.contains(group)) groups.add(group);
    }

    public boolean removeGroup(String group) {
        return groups.remove(group);
    }

    public boolean hasGroup(String group) {
        return groups.contains(group);
    }

    /** 返回旧的组件, 如果有. */
    public CopyableComponent add(CopyableComponent component) {
        return def.put(component.getClass(), component);
    }

    public boolean has(Class<? extends CopyableComponent> clazz) {
        return def.containsKey(clazz);
    }

    public @Null <T extends CopyableComponent> T get(Class<T> clazz) {
        return (T)def.get(clazz);
    }

    public CopyableComponent remove(Class<? extends CopyableComponent> clazz) {
        return def.remove(clazz);
    }

    /** 创建新实例. 新实例的组件使用池化管理 */
    public Entity create() {
        Entity e = world.createEntity();
        for (CopyableComponent component : def.values()) {
            e.edit().create(component.getClass()).copy(component);
        }

        for (var group : groups) {
            Game.groups.add(e, group);
        }
        return e;
    }

    /** 补足反序列化后的实体中缺少的组件 */
    public void refill(Entity e) {
        for (CopyableComponent def : def.values()) {
            var comp = e.getComponent(def.getClass());
            if (comp == null) comp = e.edit().create(def.getClass());
            comp.refill(def);
        }
    }

    /** 拷贝基类的默认组件包到该子类 */
    public void copyType(EntityType superType) {
        for (CopyableComponent component : superType.def.values()) {
            try {
                add(ClassReflection.newInstance(component.getClass()).copy(component));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        for (var group : superType.groups) {
            groups.add(group);
        }
    }
}
