package io.bdc.painttd.game.path;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.*;

/**
 * 节点元数据类，使用享元模式
 * 包含节点的UI相关信息，支持国际化
 *
 * <p>每个Node类对应一个NodeMetadata实例，通过{@link NodeMetadataRegistry}缓存和管理
 * 元数据从{@link NodeInfo}注解解析而来，支持继承和覆盖
 *
 * <p>设计特点：
 * <ul>
 *   <li>享元模式：相同Node类共享同一个metadata实例</li>
 *   <li>不可变：所有字段都是final，线程安全</li>
 *   <li>输入输出端口分开存储：保持注解声明的顺序</li>
 *   <li>自动生成i18n key：减少重复配置</li>
 * </ul>
 *
 * @see NodeInfo
 * @see NodeMetadataRegistry
 */
public class NodeMetadata {
    /** 节点类型，如"ScaleNode" */
    public final String nodeType;
    /** 国际化key，如"node.scale.name" */
    public final String displayNameKey;
    /** 描述的国际化key */
    public final String descriptionKey;
    /** 背景色 */
    public final Color backgroundColor;
    /** 图标名称 */
    public final String iconName;
    /** 分类 */
    public final String category;

    /** 输入端口元数据 */
    private final Array<PortMetadata> inputPorts;
    /** 输出端口元数据 */
    private final Array<PortMetadata> outputPorts;

    /**
     * 构造函数
     *
     * @param nodeType        节点类型
     * @param displayNameKey  显示名key
     * @param descriptionKey  描述key
     * @param backgroundColor 背景色
     * @param iconName        图标名
     * @param category        分类
     * @param inputPorts      输入端口
     * @param outputPorts     输出端口
     */
    public NodeMetadata(String nodeType, String displayNameKey,
                        String descriptionKey, Color backgroundColor,
                        String iconName, String category,
                        Array<PortMetadata> inputPorts,
                        Array<PortMetadata> outputPorts) {
        this.nodeType = nodeType;
        this.displayNameKey = displayNameKey;
        this.descriptionKey = descriptionKey;
        this.backgroundColor = backgroundColor;
        this.iconName = iconName;
        this.category = category;
        this.inputPorts = inputPorts != null ? inputPorts : new Array<>();
        this.outputPorts = outputPorts != null ? outputPorts : new Array<>();
    }

    /**
     * 获取本地化的显示名称
     *
     * @return 本地化后的显示名称
     */
    public String getDisplayName() {
        return Core.i18n.get(displayNameKey);
    }

    /**
     * 获取本地化的描述
     *
     * @return 本地化后的描述，如果为空则返回空字符串
     */
    public String getDescription() {
        if (descriptionKey == null || descriptionKey.isEmpty()) return "";
        return Core.i18n.get(descriptionKey);
    }

    /**
     * 根据变量名获取输入端口元数据
     *
     * @param varName 变量字段名
     *
     * @return 端口元数据，如果不存在则返回null
     */
    public PortMetadata getInputPortMetadata(String varName) {
        for (PortMetadata port : inputPorts) {
            if (port.varName.equals(varName)) {
                return port;
            }
        }
        return null;
    }

    /**
     * 根据变量名获取输出端口元数据
     *
     * @param varName 变量字段名
     *
     * @return 端口元数据，如果不存在则返回null
     */
    public PortMetadata getOutputPortMetadata(String varName) {
        for (PortMetadata port : outputPorts) {
            if (port.varName.equals(varName)) {
                return port;
            }
        }
        return null;
    }

    /**
     * 获取所有输入端口（保持注解声明的顺序）
     *
     * @return 输入端口数组
     */
    public Array<PortMetadata> getInputPorts() {
        return inputPorts;
    }

    /**
     * 获取所有输出端口（保持注解声明的顺序）
     *
     * @return 输出端口数组
     */
    public Array<PortMetadata> getOutputPorts() {
        return outputPorts;
    }

    @Override
    public String toString() {
        return "NodeMetadata{" +
                   "nodeType='" + nodeType + '\'' +
                   ", displayNameKey='" + displayNameKey + '\'' +
                   ", backgroundColor=" + backgroundColor +
                   ", category='" + category + '\'' +
                   ", inputPorts=" + inputPorts.size +
                   ", outputPorts=" + outputPorts.size +
                   '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeMetadata that = (NodeMetadata)o;

        return nodeType.equals(that.nodeType);
    }

    @Override
    public int hashCode() {
        return nodeType.hashCode();
    }

    /**
     * 端口元数据
     * 包含端口的UI信息和国际化配置
     */
    public static class PortMetadata {
        /** 变量字段名 */
        public final String varName;
        /** 显示名国际化key（可能为空，表示自动生成） */
        public final String displayNameKey;
        /** 描述国际化key（可能为空，表示自动生成） */
        public final String descriptionKey;
        /** 端口颜色 */
        public final Color portColor;
        /** 端口图标 */
        public final String iconName;
        /** 是否为输入端口 */
        public final boolean isInput;
        /** 所属节点类型，用于自动生成key */
        private final String nodeType;

        /**
         * 构造函数
         *
         * @param varName        变量字段名
         * @param displayNameKey 显示名key
         * @param descriptionKey 描述key
         * @param portColor      端口颜色
         * @param iconName       端口图标
         * @param isInput        是否为输入端口
         * @param nodeType       节点类型
         */
        public PortMetadata(String varName, String displayNameKey,
                            String descriptionKey, Color portColor,
                            String iconName, boolean isInput, String nodeType) {
            this.varName = varName;
            this.displayNameKey = displayNameKey;
            this.descriptionKey = descriptionKey;
            this.portColor = portColor;
            this.iconName = iconName;
            this.isInput = isInput;
            this.nodeType = nodeType;
        }

        /**
         * 获取本地化的显示名称
         *
         * @return 本地化后的显示名称
         */
        public String getDisplayName() {
            String key = getDisplayNameKey();
            return Core.i18n.get(key);
        }

        /**
         * 获取显示名key（自动生成或使用指定的）
         *
         * @return 显示名国际化key
         */
        public String getDisplayNameKey() {
            if (displayNameKey != null && !displayNameKey.isEmpty()) {
                return displayNameKey;
            }
            // 自动生成：node.{nodeType}.{portType}.{varName}
            return String.format("node.%s.%s.%s",
                nodeType.toLowerCase(),
                isInput ? "input" : "output",
                varName);
        }

        /**
         * 获取本地化的描述
         *
         * @return 本地化后的描述，如果为空则返回空字符串
         */
        public String getDescription() {
            String key = getDescriptionKey();
            if (key == null || key.isEmpty()) return "";
            return Core.i18n.get(key);
        }

        /**
         * 获取描述key（自动生成或使用指定的）
         *
         * @return 描述国际化key
         */
        public String getDescriptionKey() {
            if (descriptionKey != null && !descriptionKey.isEmpty()) {
                return descriptionKey;
            }
            // 自动生成：node.{nodeType}.{portType}.{varName}.desc
            return String.format("node.%s.%s.%s.desc",
                nodeType.toLowerCase(),
                isInput ? "input" : "output",
                varName);
        }
    }

}
