package io.bdc.painttd.content.trajector;

import com.badlogic.gdx.graphics.Color;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 节点元数据注册中心，使用单例模式和享元模式
 * 负责创建、缓存和管理所有节点的元数据
 */
public class NodeMetadataRegistry {
    private static final NodeMetadataRegistry instance = new NodeMetadataRegistry();
    private final Map<Class<? extends Node>, NodeMetadata> metadataMap = new HashMap<>();

    /**
     * 私有构造函数，防止外部创建实例
     */
    private NodeMetadataRegistry() {
    }

    /**
     * 获取单例实例
     */
    public static NodeMetadataRegistry getInstance() {
        return instance;
    }

    /**
     * 根据Node类获取metadata（享元的核心）
     * 如果已存在则返回缓存的实例，否则创建新的并缓存
     */
    public NodeMetadata getMetadata(Class<? extends Node> nodeClass) {
        // 如果已经存在，直接返回（享元）
        if (metadataMap.containsKey(nodeClass)) {
            return metadataMap.get(nodeClass);
        }

        // 如果不存在，创建新的metadata并缓存
        NodeMetadata metadata = createMetadataFromAnnotation(nodeClass);
        metadataMap.put(nodeClass, metadata);
        return metadata;
    }

    /**
     * 从注解创建metadata，支持智能继承合并
     */
    private NodeMetadata createMetadataFromAnnotation(Class<? extends Node> nodeClass) {
        // 1. 先获取当前类的注解
        NodeInfo currentAnnotation = nodeClass.getAnnotation(NodeInfo.class);

        // 2. 获取父类的注解（如果支持继承）
        Class<? extends Node> superClass = findNodeSuperClass(nodeClass);
        NodeInfo parentAnnotation = null;
        if (superClass != null) {
            parentAnnotation = superClass.getAnnotation(NodeInfo.class);
        }

        // 3. 合并注解信息
        NodeInfo mergedAnnotation = mergeAnnotations(parentAnnotation, currentAnnotation);

        // 4. 从合并后的注解创建metadata
        return createMetadataFromMergedAnnotation(nodeClass, mergedAnnotation);
    }

    /**
     * 查找Node父类
     */
    @SuppressWarnings("unchecked")
    private Class<? extends Node> findNodeSuperClass(Class<? extends Node> nodeClass) {
        Class<?> superClass = nodeClass.getSuperclass();
        while (superClass != null && superClass != Object.class) {
            if (Node.class.isAssignableFrom(superClass)) {
                return (Class<? extends Node>) superClass;
            }
            superClass = superClass.getSuperclass();
        }
        return null;
    }

    /**
     * 智能合并注解：子类注解覆盖父类，但只覆盖非空值
     */
    private NodeInfo mergeAnnotations(NodeInfo parent, NodeInfo child) {
        if (child == null && parent == null) return null;
        if (child != null && parent == null) return child;
        if (child == null && parent != null) return parent;

        // 创建合并后的注解实现
        return new NodeInfo() {
            @Override
            public String displayName() {
                return !child.displayName().isEmpty() ? child.displayName() : parent.displayName();
            }

            @Override
            public String description() {
                return !child.description().isEmpty() ? child.description() : parent.description();
            }

            @Override
            public String backgroundColor() {
                return !child.backgroundColor().isEmpty() && !child.backgroundColor().equals("#2196F3") 
                    ? child.backgroundColor() : parent.backgroundColor();
            }

            @Override
            public String icon() {
                return !child.icon().isEmpty() ? child.icon() : parent.icon();
            }

            @Override
            public String category() {
                return !child.category().equals("general") ? child.category() : parent.category();
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return NodeInfo.class;
            }
        };
    }

    /**
     * 从合并后的注解创建NodeMetadata
     */
    private NodeMetadata createMetadataFromMergedAnnotation(Class<? extends Node> nodeClass, NodeInfo annotation) {
        if (annotation == null) {
            // 如果没有注解，使用默认值
            return createDefaultMetadata(nodeClass);
        }

        String nodeType = nodeClass.getSimpleName();
        String displayNameKey = annotation.displayName();
        String descriptionKey = annotation.description();
        Color backgroundColor = Color.valueOf(annotation.backgroundColor());
        String iconName = annotation.icon();
        String category = annotation.category();

        // 解析变量名映射
        Map<String, String> varNameMappings = parseVarNameMappings(nodeClass);

        return new NodeMetadata(
            nodeType, displayNameKey, descriptionKey, backgroundColor,
            iconName, category, varNameMappings
        );
    }

    /**
     * 创建默认metadata（没有注解的类）
     */
    private NodeMetadata createDefaultMetadata(Class<? extends Node> nodeClass) {
        String nodeType = nodeClass.getSimpleName();
        String displayNameKey = "node." + nodeType.toLowerCase() + ".name";
        String descriptionKey = "";
        Color backgroundColor = Color.valueOf("#2196F3"); // 默认蓝色
        String iconName = "";
        String category = "general";

        Map<String, String> varNameMappings = parseVarNameMappings(nodeClass);

        return new NodeMetadata(
            nodeType, displayNameKey, descriptionKey, backgroundColor,
            iconName, category, varNameMappings
        );
    }


    /**
     * 解析变量名映射（通过反射扫描字段）
     */
    private Map<String, String> parseVarNameMappings(Class<? extends Node> nodeClass) {
        Map<String, String> mappings = new HashMap<>();
        String className = nodeClass.getSimpleName();

        // 为常见的节点类型添加预定义映射
        switch (className) {
            case "ScaleNode":
                mappings.put("scaleI", "node.scale.var.scale");
                mappings.put("shiftI", "node.scale.var.shift");
                mappings.put("shiftO", "node.scale.var.output");
                break;
            case "TimeOffsetNode":
                mappings.put("offset", "node.timeOffset.var.offset");
                break;
            default:
                // 为其他节点生成默认映射
                Field[] fields = nodeClass.getDeclaredFields();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (fieldName.endsWith("I")) {
                        // 输入变量
                        mappings.put(fieldName, "node." + className.toLowerCase() + ".var." + fieldName);
                    } else if (fieldName.endsWith("O")) {
                        // 输出变量
                        mappings.put(fieldName, "node." + className.toLowerCase() + ".var." + fieldName);
                    }
                }
                break;
        }

        return mappings;
    }

    /**
     * 清除所有缓存的metadata（主要用于测试）
     */
    public void clearCache() {
        metadataMap.clear();
    }

    /**
     * 获取已缓存的metadata数量
     */
    public int getCachedCount() {
        return metadataMap.size();
    }

    /**
     * 检查某个类的metadata是否已缓存
     */
    public boolean isCached(Class<? extends Node> nodeClass) {
        return metadataMap.containsKey(nodeClass);
    }
}