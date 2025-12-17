package io.bdc.painttd.game.path.metadata;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.bdc.painttd.game.path.var.*;
import io.bdc.painttd.utils.*;
import io.bdc.painttd.utils.func.*;

/**
 * 端口元数据类, 包含UI配置和国际化支持, 采用链式API设计.
 *
 * <p>设计特性:
 * <ul>
 *   <li>public字段允许modder直接访问修改</li>
 *   <li>链式配置提供流畅API体验</li>
 *   <li>智能i18n键生成支持三种模式</li>
 *   <li>自定义UI构造器支持</li>
 * </ul>
 *
 * <p>i18n键生成规则(继承旧实现):
 * <ul>
 *   <li>完整键: 包含".", 直接使用(如"common.input.port")</li>
 *   <li>相对键: 不含".", 添加前缀(如"shortName" → "{prefix}.shortName")</li>
 *   <li>自动生成: 空字符串, 使用默认字段名</li>
 * </ul>
 *
 * <p>使用示例(参考timeoffset节点实现):
 * <pre>{@code
 * PortMeta inputPort = new PortMeta()
 *     .setFieldName("input")
 *     .setDisplayNameKey("name")
 *     .setColor(Color.valueOf("#FF5722"))
 *     .setUiBuilder(new FloatVTextFieldBuilder().range(0, 100));
 * }</pre>
 */
public class PortMeta {
    // 公共字段, modder可直接访问修改

    public String fieldName;
    public String displayNameKey;
    public String descriptionKey;
    public Color color;
    public String iconName;
    public LinkableVarBuilder<?> uiBuilder;
    public Cons<LinkableVar> onChange;

    // 内部字段(由NodeMeta设置)

    String nodeType;
    boolean isInput;

    // 缓存生成的key

    private String cachedDisplayNameKey;
    private String cachedDescriptionKey;

    public PortMeta setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public PortMeta setDisplayNameKey(String key) {
        this.displayNameKey = key;
        return this;
    }

    public PortMeta setDescriptionKey(String key) {
        this.descriptionKey = key;
        return this;
    }

    public PortMeta setColor(Color color) {
        this.color = color;
        return this;
    }

    public PortMeta setIconName(String iconName) {
        this.iconName = iconName;
        return this;
    }

    public PortMeta setUiBuilder(LinkableVarBuilder<?> builder) {
        this.uiBuilder = builder;
        return this;
    }

    public PortMeta setOnChange(Cons<LinkableVar> onChange) {
        this.onChange = onChange;
        return this;
    }

    // 内部setter方法(由NodeMeta调用)

    void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    void setIsInput(boolean isInput) {
        this.isInput = isInput;
    }

    public String getDisplayName() {
        String key = getDisplayNameKey();
        return Format.getI18NWithFallback(key, fieldName);
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

    public boolean isInput() {
        return isInput;
    }

    public String getNodeType() {
        return nodeType;
    }
    /**
     * 构建端口UI, 如果uiBuilder可用.
     *
     * @param var 要构建UI的变量
     * @param container UI容器
     * @return true表示UI已构建, false表示只能连接link
     */
    public boolean build(LinkableVar var, Table container) {
        if (uiBuilder != null) {
            buildInternal(var, container);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private <T extends LinkableVar> void buildInternal(T var, Table container) {
        LinkableVarBuilder<T> builder = (LinkableVarBuilder<T>)uiBuilder;
        builder.build(container, var);
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

        // 3. 不含点号: 生成带端口类型的相对key
        String portType = isInput ? "input" : "output";
        return String.format("%s.%s.%s.%s", nodeType, portType, fieldName, annotationValue);
    }

    @Override
    public String toString() {
        return "PortMeta{" +
                   "fieldName='" + fieldName + '\'' +
                   ", isInput=" + isInput +
                   ", nodeType='" + nodeType + '\'' +
                   ", hasUiBuilder=" + (uiBuilder != null) +
                   '}';
    }
}