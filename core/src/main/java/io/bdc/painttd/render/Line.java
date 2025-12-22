package io.bdc.painttd.render;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import static io.bdc.painttd.Core.batch;

public class Line {
    public float stroke = 1f;

    protected TextureRegion region;
    protected Texture texture;
    protected float[] vertices = new float[128];
    protected FloatArray points = new FloatArray();
    protected Vector2 dv = new Vector2();

    public void setRegion(TextureRegion region) {
        this.region = region;
        this.texture = region.getTexture();
    }

    public void setStroke(float stk){
        this.stroke = stk;
    }

    public void line(float x1, float y1, float x2, float y2) {
        line(x1, y1, x2, y2, true);
    }

    public void line(float x1, float y1, float x2, float y2, boolean cap) {
        float len = dv.set(x2, y2).sub(x1, y1).len();
        if (len == 0) return;
        dv.nor();
        float halfStroke = stroke / 2;
        
        // 计算四边形的四个顶点, lt, lb, rb, rt
        float px1, py1, px2, py2, px3, py3, px4, py4;
        if(cap){
            float cx = dv.x - dv.y, cy = dv.y + dv.x;
            px1 = x1 - cy * halfStroke;
            py1 = y1 + cx * halfStroke;
            px2 = x1 - cx * halfStroke;
            py2 = y1 - cy * halfStroke;
            px3 = x2 + cy * halfStroke;
            py3 = y2 - cx * halfStroke;
            px4 = x2 + cx * halfStroke;
            py4 = y2 + cy * halfStroke;
        }else{
            float nx = -dv.y, ny = dv.x;
            px1 = x1 + nx * halfStroke;
            py1 = y1 + ny * halfStroke;
            px2 = x1 - nx * halfStroke;
            py2 = y1 - ny * halfStroke;
            px3 = x2 - nx * halfStroke;
            py3 = y2 - ny * halfStroke;
            px4 = x2 + nx * halfStroke;
            py4 = y2 + ny * halfStroke;
        }

        float u = region.getU();
        float v = region.getV();
        float u2 = region.getU2();
        float v2 = region.getV2();
        float c = batch.getPackedColor();

        int idx = 0;
        vertices[idx++] = px1;
        vertices[idx++] = py1;
        vertices[idx++] = c;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = px2;
        vertices[idx++] = py2;
        vertices[idx++] = c;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = px3;
        vertices[idx++] = py3;
        vertices[idx++] = c;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = px4;
        vertices[idx++] = py4;
        vertices[idx++] = c;
        vertices[idx++] = u2;
        vertices[idx] = v;

        batch.draw(texture, vertices, 0, 20);
    }

    public void tri(float x1, float y1, float x2, float y2, float x3, float y3) {
        line(x1, y1, x2, y2);
        line(x2, y2, x3, y3);
        line(x3, y3, x1, y1);
    }

    public void rect(float x, float y, float w, float h) {
        line(x, y, x + w, y);
        line(x + w, y, x + w, y + h);
        line(x + w, y + h, x, y + h);
        line(x, y + h, x, y);
    }

    public void rectCenter(float cx, float cy, float w, float h) {
        rect(cx - w / 2, cy - h / 2, w, h);
    }

    public void rect(Rectangle rect) {
        rect(rect.x, rect.y, rect.width, rect.height);
    }

    public void begin() {
        points.clear();
    }

    public void point(float x, float y) {
        points.add(x, y);
    }

    public void end() {
        if (points.size == 0) return;
        float x1 = points.get(points.size - 2), y1 = points.get(points.size - 1);
        for (int i = 0; i < points.size; i += 2) {
            float x2 = points.get(i), y2 = points.get(i + 1);
            line(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }
    }
}
