package io.bdc.painttd.content.trajector;

import com.badlogic.gdx.graphics.Color;
import io.bdc.painttd.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 节点元数据类，使用享元模式
 * 包含节点的UI相关信息，支持国际化
 */
public class NodeMetadata {
    // 基本属性
    public final String nodeType;  // 节点类型，如"ScaleNode"
    public final String displayNameKey;  // 国际化key，如"node.scale.name"
    public final String descriptionKey;  // 描述的国际化key
    public final Color backgroundColor;  // 背景色
    public final String iconName;  // 图标名称
    public final String category;  // 分类
    
    // 变量名映射
    private final Map<String, String> varNameMappings;  // var字段名 -> 显示名映射

    /**
     * 构造函数
     */
    public NodeMetadata(String nodeType, String displayNameKey, 
                       String descriptionKey, Color backgroundColor,
                       String iconName, String category,
                       Map<String, String> varNameMappings) {
        this.nodeType = nodeType;
        this.displayNameKey = displayNameKey;
        this.descriptionKey = descriptionKey;
        this.backgroundColor = backgroundColor;
        this.iconName = iconName;
        this.category = category;
        this.varNameMappings = varNameMappings != null ? varNameMappings : new HashMap<>();
    }

    /**
     * 获取本地化的显示名称
     */
    public String getDisplayName() {
        return Core.i18n.get(displayNameKey);
    }

    /**
     * 获取本地化的描述
     */
    public String getDescription() {
        if (descriptionKey == null || descriptionKey.isEmpty()) return "";
        return Core.i18n.get(descriptionKey);
    }

    /**
     * 获取变量的本地化名称
     */
    public String getVarDisplayName(String varName) {
        String mappingKey = varNameMappings.get(varName);
        if (mappingKey != null) {
            return Core.i18n.get(mappingKey);
        }
        return varName; // 如果没有映射，返回原始变量名
    }

    /**
     * 获取所有变量名映射
     */
    public Map<String, String> getVarNameMappings() {
        return new HashMap<>(varNameMappings);
    }

    @Override
    public String toString() {
        return "NodeMetadata{" +
                "nodeType='" + nodeType + '\'' +
                ", displayNameKey='" + displayNameKey + '\'' +
                ", backgroundColor=" + backgroundColor +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeMetadata that = (NodeMetadata) o;

        return nodeType.equals(that.nodeType);
    }

    @Override
    public int hashCode() {
        return nodeType.hashCode();
    }

}