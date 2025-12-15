package io.bdc.painttd.content.trajector;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.trajector.NodeMetadata.*;

/**
 * 节点元数据注册中心，使用单例模式和享元模式
 * 负责创建、缓存和管理所有节点的元数据
 *
 * <p>主要功能：
 * <ul>
 *   <li>单例模式：全局唯一实例</li>
 *   <li>享元模式：缓存NodeMetadata避免重复创建</li>
 *   <li>注解解析：从{@link NodeInfo}注解创建元数据</li>
 *   <li>继承支持：智能合并父类和子类的注解</li>
 *   <li>自动生成：为端口自动生成i18n key</li>
 * </ul>
 *
 * <p>使用示例：
 * <pre>{@code
 * NodeMetadata metadata = NodeMetadataRegistry.getInstance()
 *     .getMetadata(ScaleNode.class);
 * }</pre>
 *
 * @see NodeInfo
 * @see NodeMetadata
 */
public class NodeMetadataRegistry{
    private static final NodeMetadataRegistry instance = new NodeMetadataRegistry();
    private final ObjectMap<Class<? extends Node>, NodeMetadata> metadataMap = new ObjectMap<>();

    /**
     * 私有构造函数，防止外部创建实例
     */
    private NodeMetadataRegistry(){
    }

    /**
     * 获取单例实例
     *
     * @return 注册中心单例
     */
    public static NodeMetadataRegistry getInstance(){
        return instance;
    }

    /**
     * 根据Node类获取metadata（享元的核心）
     * 如果已存在则返回缓存的实例，否则创建新的并缓存
     *
     * @param nodeClass Node类
     * @return 节点元数据
     */
    public NodeMetadata getMetadata(Class<? extends Node> nodeClass){
        // 如果已经存在，直接返回（享元）
        if(metadataMap.containsKey(nodeClass)){
            return metadataMap.get(nodeClass);
        }

        // 如果不存在，创建新的metadata并缓存
        NodeMetadata metadata = createMetadataFromAnnotation(nodeClass);
        metadataMap.put(nodeClass, metadata);
        return metadata;
    }

    /**
     * 从注解创建metadata，支持智能继承合并
     *
     * @param nodeClass Node类
     * @return 创建的元数据
     */
    private NodeMetadata createMetadataFromAnnotation(Class<? extends Node> nodeClass){
        // 1. 先获取当前类的注解
        NodeInfo currentAnnotation = nodeClass.getAnnotation(NodeInfo.class);

        // 2. 获取父类的注解（如果支持继承）
        Class<? extends Node> superClass = findNodeSuperClass(nodeClass);
        NodeInfo parentAnnotation = null;
        if(superClass != null){
            parentAnnotation = superClass.getAnnotation(NodeInfo.class);
        }

        // 3. 合并注解信息
        NodeInfo mergedAnnotation = mergeAnnotations(parentAnnotation, currentAnnotation);

        // 4. 从合并后的注解创建metadata
        return createMetadataFromMergedAnnotation(nodeClass, mergedAnnotation);
    }

    /**
     * 查找Node父类
     *
     * @param nodeClass Node类
     * @return 最近的Node父类，如果没有则返回null
     */
    @SuppressWarnings("unchecked")
    private Class<? extends Node> findNodeSuperClass(Class<? extends Node> nodeClass){
        Class<?> superClass = nodeClass.getSuperclass();
        while(superClass != null && superClass != Object.class){
            if(Node.class.isAssignableFrom(superClass)){
                return (Class<? extends Node>)superClass;
            }
            superClass = superClass.getSuperclass();
        }
        return null;
    }

    /**
     * 智能合并注解：子类注解覆盖父类，但只覆盖非空值
     *
     * @param parent 父类注解
     * @param child 子类注解
     * @return 合并后的注解
     */
    private NodeInfo mergeAnnotations(NodeInfo parent, NodeInfo child){
        if(child == null && parent == null) return null;
        if(child != null && parent == null) return child;
        if(child == null && parent != null) return parent;

        // 创建合并后的注解实现
        return new NodeInfo(){
            @Override
            public String displayName(){
                return !child.displayName().isEmpty() ? child.displayName() : parent.displayName();
            }

            @Override
            public String description(){
                return !child.description().isEmpty() ? child.description() : parent.description();
            }

            @Override
            public String backgroundColor(){
                return !child.backgroundColor().isEmpty() && !child.backgroundColor().equals("#2196F3")
                           ? child.backgroundColor() : parent.backgroundColor();
            }

            @Override
            public String icon(){
                return !child.icon().isEmpty() ? child.icon() : parent.icon();
            }

            @Override
            public String category(){
                return !child.category().equals("general") ? child.category() : parent.category();
            }

            @Override
            public Port[] inputPorts(){
                return child.inputPorts().length > 0 ? child.inputPorts() : parent.inputPorts();
            }

            @Override
            public Port[] outputPorts(){
                return child.outputPorts().length > 0 ? child.outputPorts() : parent.outputPorts();
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType(){
                return NodeInfo.class;
            }
        };
    }

    /**
     * 从合并后的注解创建NodeMetadata
     *
     * @param nodeClass Node类
     * @param annotation 合并后的注解
     * @return 创建的元数据
     */
    private NodeMetadata createMetadataFromMergedAnnotation(Class<? extends Node> nodeClass, NodeInfo annotation){
        if(annotation == null){
            // 如果没有注解，使用默认值
            return createDefaultMetadata(nodeClass);
        }

        String nodeType = nodeClass.getSimpleName();
        String displayNameKey = annotation.displayName();
        String descriptionKey = annotation.description();
        Color backgroundColor = Color.valueOf(annotation.backgroundColor());
        String iconName = annotation.icon();
        String category = annotation.category();

        // 解析输入端口（保持注解声明的顺序）
        Array<PortMetadata> inputPorts = new Array<>();
        for(NodeInfo.Port port : annotation.inputPorts()){
            PortMetadata pm = new PortMetadata(
                port.varName(),
                port.displayName(),      // 可能为空
                port.description(),      // 可能为空
                Color.valueOf(port.color()),
                port.icon(),
                true,  // isInput
                nodeType  // 用于自动生成key
            );
            inputPorts.add(pm);
        }

        // 解析输出端口（保持注解声明的顺序）
        Array<PortMetadata> outputPorts = new Array<>();
        for(NodeInfo.Port port : annotation.outputPorts()){
            PortMetadata pm = new PortMetadata(
                port.varName(),
                port.displayName(),      // 可能为空
                port.description(),      // 可能为空
                Color.valueOf(port.color()),
                port.icon(),
                false, // isInput
                nodeType  // 用于自动生成key
            );
            outputPorts.add(pm);
        }

        return new NodeMetadata(
            nodeType, displayNameKey, descriptionKey, backgroundColor,
            iconName, category, inputPorts, outputPorts
        );
    }

    /**
     * 创建默认metadata（没有注解的类）
     *
     * @param nodeClass Node类
     * @return 默认元数据
     */
    private NodeMetadata createDefaultMetadata(Class<? extends Node> nodeClass){
        String nodeType = nodeClass.getSimpleName();
        String displayNameKey = "node." + nodeType.toLowerCase() + ".name";
        String descriptionKey = "";
        Color backgroundColor = Color.valueOf("#2196F3"); // 默认蓝色
        String iconName = "";
        String category = "general";

        Array<PortMetadata> inputPorts = new Array<>();
        Array<PortMetadata> outputPorts = new Array<>();

        return new NodeMetadata(
            nodeType, displayNameKey, descriptionKey, backgroundColor,
            iconName, category, inputPorts, outputPorts
        );
    }

    /**
     * 清除所有缓存的metadata（主要用于测试）
     */
    public void clearCache(){
        metadataMap.clear();
    }

    /**
     * 获取已缓存的metadata数量
     *
     * @return 缓存数量
     */
    public int getCachedCount(){
        return metadataMap.size;
    }

    /**
     * 检查某个类的metadata是否已缓存
     *
     * @param nodeClass Node类
     * @return 是否已缓存
     */
    public boolean isCached(Class<? extends Node> nodeClass){
        return metadataMap.containsKey(nodeClass);
    }
}
