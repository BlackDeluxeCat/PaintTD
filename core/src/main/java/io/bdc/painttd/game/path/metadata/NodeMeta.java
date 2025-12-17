package io.bdc.painttd.game.path.metadata;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.utils.*;

/**
 * 节点元数据类, 包含UI配置和国际化支持, 采用链式API设计.
 *
 * <p>设计特性:
 * <ul>
 *   <li>public字段允许modder直接访问修改</li>
 *   <li>链式配置提供流畅API体验</li>
 *   <li>智能i18n键生成支持三种模式</li>
 *   <li>享元模式实现同类节点共享实例</li>
 * </ul>
 *
 * <p>i18n键生成规则(继承旧实现):
 * <ul>
 *   <li>完整键: 包含".", 直接使用(如"common.node.name")</li>
 *   <li>相对键: 不含".", 添加前缀(如"name" → "{nodeType}.name")</li>
 *   <li>自动生成: 空字符串, 使用默认字段名</li>
 * </ul>
 *
 * <p>使用示例(参考timeoffset节点实现):
 * <pre>{@code
 * NodeMeta meta = new NodeMeta()
 *     .setNodeType("timeoffset")
 *     .setDisplayNameKey("name")
 *     .setBackgroundColor(Color.valueOf("#4CAF50"))
 *     .addInputPort(new PortMeta().setFieldName("input"))
 *     .addOutputPort(new PortMeta().setFieldName("output"));
 * }</pre>
 */
public class NodeMeta {
    // 公共字段, modder可直接访问修改
    public String nodeType;
    public String displayNameKey;
    public String descriptionKey;
    public Color backgroundColor;
    public String iconName;
    public String category;
    public Array<PortMeta> inputPorts = new Array<>();
    public Array<PortMeta> outputPorts = new Array<>();

    // 缓存生成的key

    private String cachedDisplayNameKey;
    private String cachedDescriptionKey;

    public NodeMeta setNodeType(String nodeType) {
        this.nodeType = nodeType;
        updatePortsNodeType();
        return this;
    }

    public NodeMeta setDisplayNameKey(String key) {
        this.displayNameKey = key;
        return this;
    }

    public NodeMeta setDescriptionKey(String key) {
        this.descriptionKey = key;
        return this;
    }

    public NodeMeta setBackgroundColor(Color color) {
        this.backgroundColor = color;
        return this;
    }

    public NodeMeta setIconName(String iconName) {
        this.iconName = iconName;
        return this;
    }

    public NodeMeta setCategory(String category) {
        this.category = category;
        return this;
    }

    public NodeMeta addInputPort(PortMeta port) {
        if (port != null) {
            port.setNodeType(nodeType);
            port.setIsInput(true);
            inputPorts.add(port);
        }
        return this;
    }

    public NodeMeta addOutputPort(PortMeta port) {
        if (port != null) {
            port.setNodeType(nodeType);
            port.setIsInput(false);
            outputPorts.add(port);
        }
        return this;
    }

    public String getDisplayName() {
        String key = getDisplayNameKey();
        return Format.getI18NWithFallback(key, nodeType);
    }

    public String getDisplayNameKey() {
        if (cachedDisplayNameKey == null) {
            cachedDisplayNameKey = generateKey("name", displayNameKey);
        }
        return cachedDisplayNameKey;
    }

    public String getDescription() {
        String key = getDescriptionKey();
        if (key == null || key.isEmpty()) return "";
        return Format.getI18NWithFallback(key, "");
    }

    public String getDescriptionKey() {
        if (cachedDescriptionKey == null) {
            cachedDescriptionKey = generateKey("description", descriptionKey);
        }
        return cachedDescriptionKey;
    }

    public PortMeta getInputPort(int index) {
        return inputPorts.get(index);
    }

    public PortMeta getOutputPort(int index) {
        return outputPorts.get(index);
    }

    public int getInputPortCount() {
        return inputPorts.size;
    }

    public int getOutputPortCount() {
        return outputPorts.size;
    }

    // 私有辅助方法

    private void updatePortsNodeType() {
        if (nodeType == null) return;

        for (PortMeta port : inputPorts) {
            port.setNodeType(nodeType);
        }

        for (PortMeta port : outputPorts) {
            port.setNodeType(nodeType);
        }
    }

    /**
     * 生成i18n key, 支持三种配置模式.
     *
     * @param defaultField 默认字段名(name/description)
     * @param annotationValue 注解值
     * @return 生成的key
     */
    private String generateKey(String defaultField, String annotationValue) {
        // 1. 注解值为空: 使用默认字段名
        if (annotationValue == null || annotationValue.isEmpty()) {
            annotationValue = defaultField;
        }

        // 2. 包含点号: 视为完整key
        if (annotationValue.contains(".")) {
            return annotationValue;
        }

        // 3. 不含点号: 生成相对key
        return String.format("%s.%s", nodeType, annotationValue);
    }

    @Override
    public String toString() {
        return "NodeMeta{" +
                   "nodeType='" + nodeType + '\'' +
                   ", inputPorts=" + inputPorts.size +
                   ", outputPorts=" + outputPorts.size +
                   ", category='" + category + '\'' +
                   '}';
    }
}