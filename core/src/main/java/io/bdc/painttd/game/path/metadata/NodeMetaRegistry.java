package io.bdc.painttd.game.path.metadata;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.game.path.*;

import java.lang.reflect.*;

/**
 * 节点元数据注册中心，使用单例模式和享元模式
 * 负责创建、缓存和管理所有节点的元数据
 *
 * <p>主要功能：
 * <ul>
 *   <li>单例模式：全局唯一实例</li>
 *   <li>享元模式：缓存NodeMeta避免重复创建</li>
 *   <li>静态注册：支持节点类静态注册方法</li>
 *   <li>链式注册：支持代码配置</li>
 * </ul>
 *
 * <p>注册优先级：
 * <ol>
 *   <li>显式注册（通过{@link #register}方法）</li>
 *   <li>静态注册方法（节点类的registerMeta静态方法）</li>
 *   <li>默认配置</li>
 * </ol>
 *
 * <p>使用示例：
 * <pre>{@code
 * // 方式1：显式注册（推荐）
 * NodeMetaRegistry.getInstance().register(Vector2ScaleNode.class,
 *     new NodeMeta()
 *         .setNodeType("vector2Scale")
 *         .setDisplayNameKey("name")
 *         .addInputPort(...));
 *
 * // 方式2：静态注册方法（在节点类中）
 * public class MyNode extends Node {
 *     public static void registerMeta() {
 *         NodeMetaRegistry.getInstance().register(MyNode.class, ...);
 *     }
 * }
 * }</pre>
 */
public class NodeMetaRegistry {
    private static final NodeMetaRegistry instance = new NodeMetaRegistry();
    private final ObjectMap<Class<? extends Node>, NodeMeta> metaMap = new ObjectMap<>();

    /** 私有构造函数，防止外部创建实例 */
    private NodeMetaRegistry() {}

    /** 获取单例实例 */
    public static NodeMetaRegistry getInstance() {
        return instance;
    }

    /**
     * 显式注册节点元数据（最高优先级）
     *
     * @param nodeClass 节点类
     * @param meta      节点元数据
     */
    public void register(Class<? extends Node> nodeClass, NodeMeta meta) {
        if (nodeClass == null || meta == null) {
            throw new IllegalArgumentException("nodeClass and meta cannot be null");
        }

        // 确保nodeType不为空
        if (meta.nodeType == null || meta.nodeType.isEmpty()) {
            meta.nodeType = camelToCamel(nodeClass.getSimpleName(), true);
        }

        metaMap.put(nodeClass, meta);
    }

    /**
     * 获取节点元数据（享元的核心）
     * 如果已存在则返回缓存的实例，否则按优先级创建新的并缓存
     *
     * @param nodeClass 节点类
     *
     * @return 节点元数据
     */
    public NodeMeta getMeta(Class<? extends Node> nodeClass) {
        // 如果已经存在，直接返回（享元）
        if (metaMap.containsKey(nodeClass)) {
            return metaMap.get(nodeClass);
        }

        // 按优先级尝试创建metadata
        NodeMeta meta = null;

        // 1. 尝试调用静态注册方法（如果存在）
        meta = tryStaticRegistration(nodeClass);

        // 2. 如果静态注册失败，创建默认meta
        if (meta == null) {
            meta = createDefaultMeta(nodeClass);
        }

        // 缓存并返回
        metaMap.put(nodeClass, meta);
        return meta;
    }

    /**
     * 尝试调用节点的静态注册方法
     *
     * @param nodeClass 节点类
     *
     * @return 注册的metadata，如果失败返回null
     */
    private NodeMeta tryStaticRegistration(Class<? extends Node> nodeClass) {
        try {
            // 查找名为"registerMeta"的静态方法
            Method registerMethod = nodeClass.getMethod("registerMeta");

            // 调用静态方法
            registerMethod.invoke(null);

            // 检查是否已注册
            if (metaMap.containsKey(nodeClass)) {
                return metaMap.get(nodeClass);
            }
        } catch (NoSuchMethodException e) {
            // 没有静态注册方法，正常情况
            return null;
        } catch (Exception e) {
            // 注册方法执行出错
            System.err.println("Failed to call registerMeta for " + nodeClass.getName() + ": " + e.getMessage());
            return null;
        }

        return null;
    }

    /**
     * 创建默认meta
     */
    private NodeMeta createDefaultMeta(Class<? extends Node> nodeClass) {
        String nodeType = camelToCamel(nodeClass.getSimpleName(), true);
        return new NodeMeta()
                   .setNodeType(nodeType)
                   .setDisplayNameKey("name")
                   .setBackgroundColor(Color.valueOf("#2196F3")) // 默认蓝色
                   .setCategory("general");
    }

    /**
     * 驼峰命名转换
     *
     * @param input        输入字符串
     * @param toLowerCamel 是否转换为小驼峰（true）或保持原样
     *
     * @return 转换后的字符串
     */
    private String camelToCamel(String input, boolean toLowerCamel) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        if (toLowerCamel) {
            // 大驼峰转小驼峰：首字母小写
            return Character.toLowerCase(input.charAt(0)) + input.substring(1);
        } else {
            // 小驼峰转大驼峰：首字母大写
            return Character.toUpperCase(input.charAt(0)) + input.substring(1);
        }
    }

    /**
     * 清除所有缓存的metadata（主要用于测试）
     */
    public void clearCache() {
        metaMap.clear();
    }

    /**
     * 获取已缓存的metadata数量
     *
     * @return 缓存数量
     */
    public int getCachedCount() {
        return metaMap.size;
    }

    /**
     * 检查某个类的metadata是否已缓存
     *
     * @param nodeClass 节点类
     *
     * @return 是否已缓存
     */
    public boolean isCached(Class<? extends Node> nodeClass) {
        return metaMap.containsKey(nodeClass);
    }
}
