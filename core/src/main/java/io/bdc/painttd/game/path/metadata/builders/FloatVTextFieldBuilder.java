package io.bdc.painttd.game.path.metadata.builders;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import io.bdc.painttd.game.path.metadata.*;
import io.bdc.painttd.game.path.var.*;
import io.bdc.painttd.ui.*;
import io.bdc.painttd.utils.func.*;

/**
 * FloatV类型文本字段UI构造器, 支持范围限制, 小数位数, 占位符和值改变回调.
 *
 * <p>使用示例:
 * <pre>{@code
 * new FloatVTextFieldBuilder()
 *     .range(0, 100)
 *     .decimalPlaces(2)
 *     .placeholder("Enter value")
 *     .onChange(value -> System.out.println("Value changed: " + value));
 * }</pre>
 */
public class FloatVTextFieldBuilder implements LinkableVarBuilder<FloatV> {
    // 公共配置字段
    public float min = Float.NEGATIVE_INFINITY;
    public float max = Float.POSITIVE_INFINITY;
    public int decimalPlaces = 2;
    public String placeholder = "";
    public Floatc onChange;

    public FloatVTextFieldBuilder range(float min, float max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public FloatVTextFieldBuilder decimalPlaces(int places) {
        this.decimalPlaces = places;
        return this;
    }

    public FloatVTextFieldBuilder placeholder(String text) {
        this.placeholder = text;
        return this;
    }

    public FloatVTextFieldBuilder onChange(Floatc onChange) {
        this.onChange = onChange;
        return this;
    }

    @Override
    public void build(Table cont, FloatV var, PortMeta meta) {
        String initialValue = formatFloat(var.cache);
        TextField field = new TextField(initialValue, Styles.sTextF);

        if (!placeholder.isEmpty()) {
            field.setMessageText(placeholder);
        }

        // 输入验证过滤器
        field.setTextFieldFilter((textField, c) -> {
            String newText = textField.getText() + c;
            if (newText.isEmpty()) {
                return true;
            }

            try {
                float value = Float.parseFloat(newText);
                return value >= min && value <= max;
            } catch (NumberFormatException e) {
                // 允许负号, 小数点和科学计数法
                if (newText.equals("-") || newText.equals(".") ||
                        newText.equals("-.") || newText.matches("^-?\\d*\\.?\\d*[eE]?[-+]?\\d*$")) {
                    return true;
                }
                return false;
            }
        });

        // 值改变监听器
        field.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String text = field.getText();
                if (text.isEmpty()) return;
                try {
                    float value = Float.parseFloat(text);
                    if (value >= min && value <= max) {
                        var.cache = value;
                        if (onChange != null) {
                            onChange.get(value);
                        }
                    }
                } catch (NumberFormatException ignored) {
                    // 无效输入忽略
                }
            }
        });

        // 失去焦点时格式化显示
        field.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!field.hasKeyboardFocus()) {
                    try {
                        float value = Float.parseFloat(field.getText());
                        field.setText(formatFloat(value));
                    } catch (NumberFormatException ignored) {
                        // 保持原文本
                    }
                }
            }
        });

        cont.add(new Label(meta.getDisplayName(), Styles.sLabel));

        cont.add(field).width(100).pad(2);
    }

    /**
     * 格式化浮点数, 保留指定小数位数.
     * 使用简单格式化避免String.format性能开销.
     */
    private String formatFloat(float value) {
        if (decimalPlaces <= 0) {
            return String.valueOf((int)value);
        }

        String str = String.valueOf(value);
        int dotIndex = str.indexOf('.');
        if (dotIndex >= 0 && str.length() - dotIndex - 1 > decimalPlaces) {
            return str.substring(0, dotIndex + decimalPlaces + 1);
        }
        return str;
    }

    @Override
    public String toString() {
        return "FloatVTextFieldBuilder{" +
                   "min=" + min +
                   ", max=" + max +
                   ", decimalPlaces=" + decimalPlaces +
                   ", hasOnChange=" + (onChange != null) +
                   '}';
    }
}