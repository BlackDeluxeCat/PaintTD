package io.blackdeluxecat.painttd.utils.color;

import com.badlogic.gdx.math.Vector3;

import java.util.LinkedList;

public class TileColor {
    LinkedList<HSVColor> colorList = new LinkedList<>();

    /*该向量参数依次表示色相，饱和度，明度在三维坐标系中的位置
     *原点在下底面中心
     * */
    private Vector3 colorVector = new Vector3();
    //总饱和度，最终混色时使用
    float totalSaturation = 0.0f;
    //最终显示颜色
    public HSVColor finalColor;
    //混色接口
    Mixer mixer;


    public TileColor() {
        //提供默认混色接口
        this.mixer = color -> {
            //一些中继变量
            float cosh = (float) Math.cos(Math.toRadians(color.h));
            float sinh = (float) Math.sin(Math.toRadians(color.h));
            //转换为空间直角坐标系
            Vector3 temp = new Vector3(
                color.s * cosh,
                color.s * sinh,
                color.v
            );
            //叠加到colorVector
            colorVector.add(temp);


        };
    }
    //三个属性
    public TileColor(float hue, float saturation, float brightness) {
        this();
        colorVector = new Vector3(hue, saturation, brightness);
    }

    public TileColor(float hue ,float saturation, float brightness, Mixer mixer) {
        colorVector = new Vector3(hue, saturation, brightness);
        this.mixer = mixer;
    }


    void addColor(HSVColor color){
        colorList.add(color);
    }

    void removeColor(int colorIndex){
        //承接一下，减少遍历次数
        HSVColor tempColor = new HSVColor(0.0f,0.0f,0.0f,colorIndex);
        colorList.remove(tempColor);
    }
    void mix(){

        //求得总饱和度
        for (HSVColor color : colorList) {
            //去除饱和度过低的颜色
            if (color.s < 0.0001f) {
                removeColor(color.index);
                continue;
            }
            totalSaturation += color.s;
        }
        mixer.mix(colorList);
        vectorToColor();
    }

    //将colorVector还原为颜色
    private void vectorToColor(){
        finalColor = new HSVColor(
            (float) Math.toDegrees(Math.atan2(colorVector.y,colorVector.x)),
            (float) Math.sqrt(Math.pow(colorVector.x,2)+Math.pow(colorVector.y,2)),
            colorVector.z
        );

        //使finalColor的值合理

        // 确保色相在0-360范围内
        while (finalColor.h < 0.0f) {
            finalColor.h += 360.0f;
        }
        while (finalColor.h >= 360.0f) {
            finalColor.h -= 360.0f;
        }

        // 限制饱和度和亮度在有效范围内
        //finalColor.s = Math.max(0.0f, Math.min(1.0f, finalColor.s));
        //finalColor.v = Math.max(0.0f, Math.min(1.0f, finalColor.v));

        //极短向量归零
        if (finalColor.s < 0.0001f) {
            finalColor.s = 0.0f;
        }
        //放缩
        colorVector.scl(1.0f/totalSaturation);
    }
    public String toString() {
        return "TileColor{" +
            "finalColor=" + finalColor +
            '}';
    }
}
