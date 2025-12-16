package io.bdc.painttd.ui;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.*;

/**
 * ObjBuildStrategy 是一个通用的对象UI控件构建策略类。它支持继承和接口，能够根据对象的类层次结构
 * 查找合适的构建器。
 *
 * <p>该类使用 ObjectMap 存储类与构建器之间的映射关系，通过 register 方法注册
 * 特定类型的构建器，通过 buildObj 方法根据实例对象查找并执行相应的构建器。</p>
 *
 * <p>示例用法:</p>
 * <pre>
 * {@code
 * // 创建策略实例
 * ObjBuildStrategy<MyObject, Actor> strategy = new ObjBuildStrategy<>();
 * // 注册特定类的构建器
 * strategy.register(MyConcreteObject.class, (obj, actor) -> {
 * // 构建逻辑
 * });
 * // 注册接口的构建器
 * strategy.register(MyInterface.class, (obj, actor) -> {
 * // 构建逻辑
 * });
 *
 * // 使用构建器
 * MyObject obj = new MyConcreteObject();
 * Actor actor = new Actor();
 * boolean success = strategy.buildObj(obj, actor);
 * }
 *
 * </pre>
 *
 * @param <A> 要构建的对象类型
 * @param <B> 关联的Actor类型
 */

@SuppressWarnings({"unchecked", "rawtypes"})
public class ObjBuildStrategy<A, B extends Actor> {
    ObjectMap<Class<?>, ObjBuilder<A, B>> builders = new ObjectMap<>();

    public <T extends A> void register(Class<T> clazz, @Null ObjBuilder<T, B> cons) {
        if (clazz != null && cons != null) {
            builders.put(clazz, (ObjBuilder<A, B>)cons);
        }
    }

    public boolean buildObj(A instance, B actor) {
        return buildObj(instance.getClass(), instance, actor);
    }

    /**
     * 尝试查找与给定类匹配的构建器，考虑继承和接口实现
     *
     * @return 如果找到并成功调用构建器则返回true，否则返回false
     */
    public boolean buildObj(Class<?> clazz, A instance, B actor) {
        // 首先尝试精确匹配
        ObjBuilder<A, B> builder = builders.get(clazz);
        if (builder != null) {
            builder.build(instance, actor);
            return true;
        }

        // 如果没有精确匹配，则遍历类层次结构
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            // 检查所有接口
            for (Class<?> interfaceClass : currentClass.getInterfaces()) {
                builder = builders.get(interfaceClass);
                if (builder != null) {
                    builder.build(instance, actor);
                    return true;
                }

                // 递归检查接口的父接口
                if (buildObj(interfaceClass, instance, actor)) {
                    return true;
                }
            }

            // 检查父类
            currentClass = currentClass.getSuperclass();
            if (currentClass != null) {
                builder = builders.get(currentClass);
                if (builder != null) {
                    builder.build(instance, actor);
                    return true;
                }
            }
        }

        return false;
    }

    public interface ObjBuilder<A, B extends Actor> {
        void build(A instance, B actor);
    }
}
