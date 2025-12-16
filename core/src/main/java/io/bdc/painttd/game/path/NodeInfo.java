package io.bdc.painttd.game.path;

import java.lang.annotation.*;

/**
 * 节点信息注解，用于定义节点的UI元数据
 * 支持继承，子类可以覆盖父类的注解值
 *
 * <p>i18n key生成规则：
 * <ul>
 *   <li>节点级别：{nodeType}.{fieldName}</li>
 *   <li>端口级别：{nodeType}.{portType}.{fieldName}.{fieldName}</li>
 *   <li>如果字段值包含"."：视为完整key，直接使用</li>
 *   <li>如果字段值为空：使用默认字段名生成完整key</li>
 *   <li>如果字段值不为空且不含"."：生成相对key</li>
 * </ul>
 *
 * <p>使用示例：
 * <pre>{@code
 * @NodeInfo(
 *     nodeType = "scale",
 *     displayName = "name",           // → "scale.name"
 *     description = "description",    // → "scale.description"
 *     inputPorts = {
 *         @Port(fieldName = "shiftI", displayName = "")
 *         // → "scale.input.shiftI.name"
 *     }
 * )
 * }</pre>
 *
 * @see NodeMetadata
 * @see NodeMetadataRegistry
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NodeInfo {
    /**
     * 节点类型标识，用于i18n key生成
     * 默认值：空字符串，表示使用类名（大驼峰转小驼峰）
     * 示例：TimeOffsetNode → "timeOffset"
     */
    String nodeType() default "";
    /**
     * 节点显示名称的国际化key
     * 格式：{nodeType}.name 或 完整key
     * 示例："name" → "scale.name"
     */
    String displayName();
    /**
     * 节点描述的国际化key，可选
     * 格式：{nodeType}.description 或 完整key
     * 示例："description" → "scale.description"
     */
    String description() default "";

    /**
     * 节点背景色，默认蓝色
         * 格式：十六进制颜色码
     * 示例："#4CAF50" → 绿色
         */
    String backgroundColor() default "#2196F3";

        /**
     * 节点图标名称，可选
     * 用于在UI中显示节点图标
         */
        String icon() default "";
    /**
     * 节点分类，默认为general
     * 用于在节点库中分组显示
     */
    String category() default "general";
    /**
     * 端口配置注解
     * 定义端口的UI元数据
     */
    @interface Port {
        /**
         * 变量字段名（必需）
         * 必须与Node类中的public字段名一致
         * 示例："shiftI" 对应 {@code public Vector2V shiftI}
     */
        String fieldName();

        /**
         * 显示名国际化key
         * 如果为空：生成 {nodeType}.{portType}.{fieldName}.name
         * 如果不含点：生成 {nodeType}.{portType}.{fieldName}.{displayName}
         * 如果包含点：视为完整key直接使用
         */
        String displayName() default "";

        /**
         * 描述国际化key
         * 如果为空：生成 {nodeType}.{portType}.{fieldName}.description
         * 如果不含点：生成 {nodeType}.{portType}.{fieldName}.{description}
         * 如果包含点：视为完整key直接使用
         */
        String description() default "";

        /**
         * 端口颜色
         * 格式：十六进制颜色码
         * 用于在UI中区分不同类型的端口
         */
        String color() default "#FFFFFF";

        /**
         * 端口图标名称，可选
         * 用于在端口旁显示小图标
         */
        String icon() default "";
}

    /**
     * 输入端口配置
     */
    Port[] inputPorts() default {};

    /**
     * 输出端口配置
     */
    Port[] outputPorts() default {};
}
