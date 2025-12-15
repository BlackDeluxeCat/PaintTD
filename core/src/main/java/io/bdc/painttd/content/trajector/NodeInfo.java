package io.bdc.painttd.content.trajector;

import java.lang.annotation.*;

/**
 * 节点信息注解，用于定义节点的UI元数据
 * 支持继承，子类可以覆盖父类的注解值
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NodeInfo {
    /**
     * 节点显示名称的国际化key
     */
    String displayName();
    
    /**
     * 节点描述的国际化key，可选
     */
    String description() default "";
    
    /**
     * 节点背景色，默认蓝色
     */
    String backgroundColor() default "#2196F3";
    
    /**
     * 节点图标名称，可选
     */
    String icon() default "";
    
    /**
     * 节点分类，默认为general
     */
    String category() default "general";
}