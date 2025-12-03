package io.bdc.painttd.utils;

public class Format{
    private static final StringBuilder tmp1 = new StringBuilder();
    private static final StringBuilder tmp2 = new StringBuilder();

    public static StringBuilder fixedBuilder(float d, int decimalPlaces){
        if(decimalPlaces < 0 || decimalPlaces > 8){
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
        if(negative) result.append('-');
        if(decimalPlaces == 0){
            if(negative) dec.insert(0, '-');
            return dec;
        }else if(decimalPosition > 0){
            // Insert a dot in the right place
            result.append(dec, 0, decimalPosition);
            result.append(".");
            result.append(dec, decimalPosition, dec.length());
        }else{
            result.append("0.");
            // Insert leading zeroes into the decimal part
            while(decimalPosition++ < 0){
                result.append("0");
            }
            result.append(dec);
        }
        return result;
    }

    public static boolean canParseFloat(String s){
        try{
            Float.parseFloat(s);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
}
