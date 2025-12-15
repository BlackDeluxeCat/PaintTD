package io.bdc.painttd.content.trajector;

import java.lang.annotation.*;

/**
 * 节点信息注解，用于定义节点的UI元数据
 * 支持继承，子类可以覆盖父类的注解值
 *
 * <p>使用示例：
 * <pre>{@code
 * @NodeInfo(
 *     displayName = "node.scale.name",
 *     description = "node.scale.description",
 *     backgroundColor = "#4CAF50",
 *     icon = "scale_icon",
 *     category = "transform",
 *     inputPorts = {
 *         @NodeInfo.Port(varName = "shiftI", color = "#FF9800")
 *     },
 *     outputPorts = {
 *         @NodeInfo.Port(varName = "shiftO", color = "#2196F3")
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
     * 节点显示名称的国际化key
     * 格式：node.{nodeType}.name
     * 示例：node.scale.name → "缩放节点"
     */
    String displayName();
    
    /**
     * 节点描述的国际化key，可选
     * 格式：node.{nodeType}.description
     * 示例：node.scale.description → "对输入向量进行缩放变换"
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
        String varName();

        /**
         * 显示名国际化key，如果为空则自动生成
         * 自动生成规则：node.{nodeType}.{portType}.{varName}
         * 示例：node.scale.input.shiftI → "位移输入"
         */
        String displayName() default "";

        /**
         * 描述国际化key，如果为空则自动生成
         * 自动生成规则：node.{nodeType}.{portType}.{varName}.desc
         * 示例：node.scale.input.shiftI.desc → "输入位移向量"
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
