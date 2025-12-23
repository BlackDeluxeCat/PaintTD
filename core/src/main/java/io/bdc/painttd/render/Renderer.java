package io.bdc.painttd.render;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;

import static io.bdc.painttd.Core.*;
import static io.bdc.painttd.ui.Styles.*;

public class Renderer {
    public static Fill fill = new Fill();
    public static Line line = new Line();

    public static float tileSize = 8f;
    public static float zoom = 1f;
    public static float targetZoom;
    public static Vector3 targetPos = new Vector3();
    public static ScreenViewport viewport = new ScreenViewport();
    public static Camera camera;

    public static void load() {
        camera = viewport.getCamera();
        fill.setRegion(whiteRegion);
        line.setRegion(whiteRegion);
    }

    public static void setColor(Color color) {
        batch.setColor(color);
    }

    public static void setColor(Color color, float alpha) {
        batch.setColor(color.r, color.g, color.b, alpha);
    }

    public static void a(float alpha) {
        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, alpha);
    }

    public static void update() {
        //平滑缩放/移动, 并应用到camera
        zoom = MathUtils.lerp(zoom, targetZoom, 0.1f);
        //映射窗口矩形到世界视口矩形
        viewport.setUnitsPerPixel(1f / zoom);
        camera.position.lerp(targetPos, 0.1f);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        batch.setProjectionMatrix(camera.combined);
    }
}
