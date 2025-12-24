package io.bdc.painttd.game.path.metadata.builders;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import io.bdc.painttd.game.path.metadata.*;
import io.bdc.painttd.game.path.var.*;
import io.bdc.painttd.ui.*;
import io.bdc.painttd.utils.func.*;

/**
 * Vector2V类型UI构造器, 创建两个文本字段分别编辑x和y分量.
 *
 * <p>使用示例:
 * <pre>{@code
 * new Vector2PortBuilder()
 *     .setXRange(-100, 100)
 *     .setYRange(-50, 50)
 *     .setDecimalPlaces(2)
 *     .setOnChange((x, y) -> System.out.println("Vector changed: " + x + ", " + y));
 * }</pre>
 */
public class Vector2PortBuilder implements LinkableVarBuilder<Vector2V> {
    // 公共配置字段
    public float xMin = Float.NEGATIVE_INFINITY;
    public float xMax = Float.POSITIVE_INFINITY;
    public float yMin = Float.NEGATIVE_INFINITY;
    public float yMax = Float.POSITIVE_INFINITY;
    public int decimalPlaces = 2;
    public String xPlaceholder = "X";
    public String yPlaceholder = "Y";
    public Cons<Vector2> onChange;

    public Vector2PortBuilder setXRange(float min, float max) {
        this.xMin = min;
        this.xMax = max;
        return this;
    }

    public Vector2PortBuilder setYRange(float min, float max) {
        this.yMin = min;
        this.yMax = max;
        return this;
    }

    public Vector2PortBuilder setDecimalPlaces(int places) {
        this.decimalPlaces = places;
        return this;
    }

    public Vector2PortBuilder setXPlaceholder(String placeholder) {
        this.xPlaceholder = placeholder;
        return this;
    }

    public Vector2PortBuilder setYPlaceholder(String placeholder) {
        this.yPlaceholder = placeholder;
        return this;
    }

    public Vector2PortBuilder setOnChange(Cons<Vector2> onChange) {
        this.onChange = onChange;
        return this;
    }

    @Override
    public void build(Table cont, Vector2V var, PortMeta meta) {
        Table vectorTable = new Table();

        TextField xField = createTextField(var.cache.x, xMin, xMax, xPlaceholder);
        xField.addListener(createChangeListener(xField, var, true));

        TextField yField = createTextField(var.cache.y, yMin, yMax, yPlaceholder);
        yField.addListener(createChangeListener(yField, var, false));

        vectorTable.add(xField).width(60).padRight(2);
        vectorTable.add(yField).width(60);

        cont.add(vectorTable);
    }

    /**
     * 创建文本字段组件.
     */
    private TextField createTextField(float initialValue, float min, float max, String placeholder) {
        String text = formatFloat(initialValue);
        TextField field = new TextField(text, Styles.sTextF);

        if (placeholder != null && !placeholder.isEmpty()) {
            field.setMessageText(placeholder);
        }

        field.setTextFieldFilter((textField, c) -> {
            String newText = textField.getText() + c;
            if (newText.isEmpty()) {
                return true;
            }

            try {
                float value = Float.parseFloat(newText);
                return value >= min && value <= max;
            } catch (NumberFormatException e) {
                if (newText.equals("-") || newText.equals(".") || newText.matches("^-?\\d*\\.?\\d*[eE]?[-+]?\\d*$")) {
                    return true;
                }
                return false;
            }
        });

        return field;
    }

    /**
     * 创建值改变监听器.
     */
    private ChangeListener createChangeListener(TextField field, Vector2V var, boolean isX) {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String text = field.getText();
                if (text.isEmpty()) return;
                try {
                    float value = Float.parseFloat(text);
                    float min = isX ? xMin : yMin;
                    float max = isX ? xMax : yMax;

                    if (value >= min && value <= max) {
                        if (isX) {
                            var.cache.x = value;
                        } else {
                            var.cache.y = value;
                        }

                        if (onChange != null) {
                            onChange.get(var.cache);
                        }
                    }
                } catch (NumberFormatException ignored) {
                    // 无效输入忽略
                }
            }
        };
    }

    /**
     * 格式化浮点数, 保留指定小数位数.
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
        return "Vector2PortBuilder{" +
                   "xRange=[" + xMin + ", " + xMax + "]" +
                   ", yRange=[" + yMin + ", " + yMax + "]" +
                   ", decimalPlaces=" + decimalPlaces +
                   ", hasOnChange=" + (onChange != null) +
                   '}';
    }
}