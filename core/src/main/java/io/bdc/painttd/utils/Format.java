package io.bdc.painttd.utils;

import io.bdc.painttd.*;

public class Format {
    private static final StringBuilder tmp1 = new StringBuilder();
    private static final StringBuilder tmp2 = new StringBuilder();

    public static StringBuilder fixedBuilder(float d, int decimalPlaces) {
        if (decimalPlaces < 0 || decimalPlaces > 8) {
            throw new IllegalArgumentException("Unsupported number of " + "decimal places: " + decimalPlaces);
        }
        boolean negative = d < 0;
        d = Math.abs(d);
        StringBuilder dec = tmp2;
        dec.setLength(0);
        dec.append((int)(float)(d * Math.pow(10, decimalPlaces) + 0.0001f));

        int len = dec.length();
        int decimalPosition = len - decimalPlaces;
        StringBuilder result = tmp1;
        result.setLength(0);
        if (negative) result.append('-');
        if (decimalPlaces == 0) {
            if (negative) dec.insert(0, '-');
            return dec;
        } else if (decimalPosition > 0) {
            // Insert a dot in the right place
            result.append(dec, 0, decimalPosition);
            result.append(".");
            result.append(dec, decimalPosition, dec.length());
        } else {
            result.append("0.");
            // Insert leading zeroes into the decimal part
            while (decimalPosition++ < 0) {
                result.append("0");
            }
            result.append(dec);
        }
        return result;
    }

    public static boolean canParseFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 安全获取i18n字符串，key不存在时返回美化后的fallback
     *
     * @param key            i18n key
     * @param fallbackSource 用于生成fallback显示的源字符串
     *
     * @return 翻译值或美化后的fallback
     */
    public static String getI18NWithFallback(String key, String fallbackSource) {
        if (Core.i18n == null) {
            return beautifyKey(fallbackSource);
        }
        try {
            String value = Core.i18n.get(key);
            // 检查是否返回了"???" + key + "???"（表示key不存在）
            String missingMarker = "???" + key + "???";
            if (value.equals(missingMarker)) {
                return beautifyKey(fallbackSource);
            }
            return value;
        } catch (Exception e) {
            return beautifyKey(fallbackSource);
        }
    }

    /**
     * 安全获取i18n字符串（单参数版本）
     * key不存在时返回美化后的key本身
     */
    public static String getI18NWithFallback(String key) {
        return getI18NWithFallback(key, key);
    }

    /**
     * 美化key值
     * 驼峰转空格，首字母大写
     */
    public static String beautifyKey(String source) {
        if (source == null || source.isEmpty()) {
            return source;
        }

        // 驼峰转空格
        String result = source.replaceAll("([a-z])([A-Z])", "$1 $2");

        // 首字母大写
        if (result.length() > 0) {
            result = Character.toUpperCase(result.charAt(0)) + result.substring(1);
        }

        return result;
    }
}
