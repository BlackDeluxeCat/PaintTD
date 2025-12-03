package io.bdc.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.*;
import io.bdc.painttd.io.*;
import io.bdc.painttd.systems.render.*;

public class PreferenceDialog extends BaseDialog{
    public Array<PrefCategory> categories = new Array<>();
    public int cateIndex = 0;

    Table cateTab = new Table(), prefTab = new Table();

    public PreferenceDialog(){
        super("选项");
        setBackground(Styles.black8);
        addCloseButton();

        cateTab.setBackground(Styles.ninePatch);
        prefTab.setBackground(Styles.black5);

        var render = new PrefCategory("pref.cate.render", new TextureRegionDrawable(Core.atlas.findRegion("u-eraser")));
        addCategory(render);

        var game = new PrefCategory("pref.cate.gameplay", new TextureRegionDrawable(Core.atlas.findRegion("b-pencil")));
        addCategory(game);

        var debug = new PrefCategory("pref.cate.debug", new TextureRegionDrawable(Core.atlas.findRegion("u-eraser")), "");
        addCategory(debug);

        render.boolPref("drawRange", "pref.drawRange", true, b -> DrawTarget.drawRange = b);
        render.boolPref("drawTargetLine", "pref.drawTargetLine", true, b -> DrawTarget.drawTargetLine = b);

        debug.boolPref("drawUnitHitbox", "pref.drawUnitHitbox", true, b -> DrawUnitHitbox.pref = b);
        debug.boolPref("drawFlowField", "pref.drawFlowField", false, b -> DrawFlowFieldDebug.pref = b);

        rebuild();
    }

    public PrefCategory getCategory(String name){
        for(PrefCategory category : categories){
            if(category.name.equals(name)){
                return category;
            }
        }
        return null;
    }

    public void addCategory(PrefCategory category){
        categories.add(category);
    }

    public void rebuild(){
        cont.clear();

        cateTab.clear();
        cateTab.top();

        for(int i = 0; i < categories.size; i++){
            int fi = i;
            PrefCategory category = categories.get(i);
            cateTab.add(new ActorUtils<>(new Button(Styles.sTextB))
                            .with(t -> {
                                if(category.icon != null) t.add(new Image(category.icon)).size(Styles.buttonSize);
                                t.add(new Label(Core.i18n.get(category.name), Styles.sLabel)).growX();
                            })
                            .click(b -> {
                                cateIndex = fi;
                                rebuildPrefTab();
                            })
                            .actor).growX().pad(8f);
            cateTab.row();
        }

        rebuildPrefTab();

        cont.pad(100f, 100f, 20f, 100f);
        var cateScroller = cont.add(new ScrollPane(cateTab)).growY().getActor();

        ActorUtils.updater(cateScroller, t -> {
            var stageHit = t.getStage().hit(Gdx.input.getX(), Gdx.input.getY(), false);
            if(stageHit != null && stageHit.isDescendantOf(cateScroller)){
                cateScroller.setWidth(400f);
                cateScroller.toFront();
            }else{
                cateScroller.setWidth(200f);
                cateScroller.toBack();
            }
        });
        cont.add(new ScrollPane(prefTab)).grow();
    }

    public void rebuildPrefTab(){
        prefTab.clear();
        prefTab.top();
        PrefCategory category = categories.get(cateIndex);
        for(PrefCategory.PrefElem elem : category.elems){
            var t = new Table();
            buildStrategy.buildObj(elem, t);
            prefTab.add(t).growX().minHeight(Styles.buttonSize * 2).pad(8f).row();
        }
    }

    public final static ObjBuildStrategy<PrefCategory.PrefElem, Table> buildStrategy = new ObjBuildStrategy<>() {
        {
            register(PrefCategory.BoolElem.class, (pe, t) -> {
                PrefCategory.BoolElem be = (PrefCategory.BoolElem)pe;
                be.updateValue();
                t.add(new ActorUtils<>(new Table())
                          .with(tt -> tt.add(ActorUtils.wrapper
                                                 .set(new Label(Core.i18n.get("pref." + be.name), Styles.sLabel))
                                                 .update(l -> l.setColor(be.value ? Color.GREEN : Color.RED))
                                                 .actor).growX()
                          )
                          .click(tt -> {
                              Core.prefs.putBoolean(be.name, be.value = !be.value);
                              if(be.changed != null) be.changed.get(be.value);
                          })
                          .actor);
            });
        }
    };
}
