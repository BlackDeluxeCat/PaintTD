package io.bdc.painttd.render;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

public class Line {
    public float stroke = 1f;

    protected Batch batch;
    protected TextureRegion region;
    protected Texture texture;
    protected float[] vertices = new float[128];
    protected FloatArray points = new FloatArray();
    protected Vector2 dv = new Vector2();

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
        this.texture = region.getTexture();
    }

    public void setStroke(float stk) {
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
        if (cap) {
            float cx = dv.x - dv.y, cy = dv.y + dv.x;
            px1 = x1 - cy * halfStroke;
            py1 = y1 + cx * halfStroke;
            px2 = x1 - cx * halfStroke;
            py2 = y1 - cy * halfStroke;
            px3 = x2 + cy * halfStroke;
            py3 = y2 - cx * halfStroke;
            px4 = x2 + cx * halfStroke;
            py4 = y2 + cy * halfStroke;
        } else {
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

    public void circle(float x, float y, float radius, int segments) {
        polygon(x, y, radius, 0, segments);
    }

    public void polygon(float x, float y, float size, float rotation, int sides) {
        // 确保至少有3条边
        sides = Math.max(3, sides);

        // 计算每个顶点的角度
        float angleStep = 2 * MathUtils.PI / sides;

        // 将旋转角度转换为弧度
        float rotationRad = rotation * MathUtils.degreesToRadians;

        // 计算多边形的顶点坐标
        points.clear();
        for (int i = 0; i < sides; i++) {
            float angle = i * angleStep + rotationRad;
            points.add(x + size * MathUtils.cos(angle));
            points.add(y + size * MathUtils.sin(angle));
        }

        float x1 = points.items[sides * 2 - 2], y1 = points.items[sides * 2 - 1], x2, y2;
        for (int i = 0; i < sides; i++) {
            x2 = points.items[i * 2];
            y2 = points.items[i * 2 + 1];
            line(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }
    }

    public void polylineStart() {
        points.clear();
    }

    public void polylineAdd(float x, float y) {
        points.add(x, y);
    }

    public void polylineEnd() {
        if (points.size == 0) return;
        line(points.items, points.size);
    }

    public void line(float[] vertices, int size) {
        if (size < 2 * 2) return;
        float x1 = vertices[0], y1 = vertices[1];
        for (int i = 2; i <= size - 2; i += 2) {
            float x2 = vertices[i], y2 = vertices[i + 1];
            line(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }
    }
}
