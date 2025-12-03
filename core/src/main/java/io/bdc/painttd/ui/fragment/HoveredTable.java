package io.bdc.painttd.ui.fragment;

import com.artemis.*;
import com.artemis.utils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.components.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.game.*;
import io.bdc.painttd.ui.*;
import io.bdc.painttd.utils.*;
import io.bdc.painttd.utils.func.*;

import java.util.*;

import static io.bdc.painttd.ui.Styles.*;

/**
 * 显示单个实体的状态信息, 对每个组件构建ui.
 */
//TODO基于Aspect的多组件UI
public class HoveredTable extends Table{
    Bag<Component> tmpBag = new Bag<>();

    public HoveredTable(){
        pad(2);
        background(black3);
        defaults().minHeight(8).minWidth(200f);
    }

    public void build(int e){
        clear();
        if(e == -1) return;
        ActorUtils.wrapper.set(this).update(t -> {
            if(!Game.world.getEntityManager().isActive(e)) build(-1);
        });

        tmpBag.clear();
        Game.world.getComponentManager().getComponentsFor(e, tmpBag).sort(orderComparator);
        for(Component comp : tmpBag){
            if(comp instanceof CopyableComponent cc){
                var cont = new Table();
                boolean built = false;
                if(Game.rules.isEditor){
                    built = buildEditorStrategy.buildObj(cc, cont);
                }
                if(!built) buildStrategy.buildObj(cc, cont);
                add(cont).row();
            }
        }
    }

    public static Array<Class<? extends Component>> componentOrder = new Array<>();

    static Comparator<Component> orderComparator = Comparator.comparingInt(o -> componentOrder.indexOf(o.getClass(), true));

    /** 组件展示控件的构建策略, 注册同时注册组件排序 */
    public final static ObjBuildStrategy<CopyableComponent, Table> buildStrategy = new ObjBuildStrategy<>() {
        {
            register(EntityTypeComp.class, (comp, t) -> {
                t.add(ActorUtils.wrapper.set(new Label(comp.type, sLabel))
                          .actor);
            });

            register(PositionComp.class, (comp, t) -> {
                t.add(ActorUtils.wrapper.set(new Label(null, sLabel))
                          .with(l -> l.setColor(Color.GRAY))
                          .update(a -> ((Label)a).setText(comp.tileX() + ", " + comp.tileY()))
                          .actor);
            });

            register(HealthComp.class, (comp, t) -> {
                t.add(ActorUtils.wrapper.set(new Label(null, sLabel))
                          .update(a -> ((Label)a).setText("HP: " + Format.fixedBuilder(comp.health, 1)))
                          .actor);
            });

            register(HealthRegenComp.class, (comp, t) -> {
                t.add(ActorUtils.wrapper.set(new Label(null, sLabel))
                          .update(a -> ((Label)a).setText("每秒回复: " + Format.fixedBuilder(comp.rate, 1)))
                          .actor);
            });

            register(SpawnGroupCompsComp.class, (comp, t) -> {
                t.defaults().minWidth(80f);
                comp.comps.sort(orderComparator);
                for(CopyableComponent c : comp.comps){
                    var tt = new Table();
                    t.add(tt).row();
                    buildStrategy.buildObj(c, tt);
                }
            });

            register(SpawnGroupComp.class, (comp, t) -> {
                t.defaults().minWidth(80f);

                Cons2<String, Prov<String>> labelp = (txt, prov) -> {
                    t.add(new Label(txt, sLabel));
                    t.add(ActorUtils.updater(new Label("", sLabel), l -> {
                        l.setText(prov.get());
                    }));
                };

                labelp.get("波次", () -> comp.waveStart + " +" + comp.waveDelta);

                labelp.get("生成间隔", () -> comp.spawnDelta + "t");

                labelp.get("数量", () -> comp.amtStart + " +" + comp.amtDelta + "/w (" + comp.amtMax + ")");

                labelp.get("血量", () -> comp.healthStart + " +" + comp.healthDelta + "/w");
            });
        }

        @Override
        public <T extends CopyableComponent> void register(Class<T> clazz, ObjBuilder<T, Table> cons){
            super.register(clazz, cons);
            componentOrder.add(clazz);
        }
    };

    /** 组件编辑控件的构建策略 */
    public final static ObjBuildStrategy<CopyableComponent, Table> buildEditorStrategy = new ObjBuildStrategy<>(){
        {
            register(SpawnGroupCompsComp.class, (comp, t) -> {
                t.defaults();
                var addTable = new Table();

                addTable.add(ActorUtils.wrapper.set(new TextButton("生命恢复", sTextB))
                                 .click(b -> {
                                     comp.add(new HealthRegenComp(1f / 45f));
                                 })
                                 .actor);

                var scroll = new ScrollPane(addTable);
                t.add(scroll).growX().colspan(2).row();

                comp.comps.sort(orderComparator);
                for(CopyableComponent c : comp.comps){
                    var tt = new Table();
                    buildEditorStrategy.buildObj(c, tt);
                    t.add(tt);

                    t.add(ActorUtils.wrapper.set(new TextButton("x", sTextB))
                              .click(b -> {
                                  comp.comps.removeValue(c, true);
                                  tt.remove();
                                  b.remove();
                              })
                              .actor);
                    t.row();
                }
            });

            register(SpawnGroupComp.class, (comp, t) -> {
                t.defaults().minWidth(80f);

                Cons4<String, Prov<String>, Cons<String>, TextField.TextFieldFilter> fieldp = (txt, prov, cons, filter1) -> {
                    t.add(new Label(txt, sLabel));
                    TextField field1 = new TextField(prov.get(), sTextF);
                    t.add(field1).row();
                    field1.setTextFieldFilter(filter1);
                    field1.setTextFieldListener((f, c) -> {
                        if(!f.getText().isEmpty()){
                            cons.get(f.getText());
                        }
                    });
                };

                fieldp.get("起始波数", () -> String.valueOf(comp.waveStart), s -> comp.waveStart = Integer.parseInt(s), ActorUtils.digitOnly);

                fieldp.get("波次间隔", () -> String.valueOf(comp.waveDelta), s -> comp.waveDelta = Integer.parseInt(s), ActorUtils.digitOnly);

                fieldp.get("起始数量", () -> String.valueOf(comp.amtStart), s -> comp.amtStart = Float.parseFloat(s), ActorUtils.floatOnly);

                fieldp.get("增量数量", () -> String.valueOf(comp.amtDelta), s -> comp.amtDelta = Float.parseFloat(s), ActorUtils.floatOnly);

                fieldp.get("最大数量", () -> String.valueOf(comp.amtMax), s -> comp.amtMax = Integer.parseInt(s), ActorUtils.digitOnly);

                fieldp.get("起始血量", () -> String.valueOf(comp.healthStart), s -> comp.healthStart = Float.parseFloat(s), ActorUtils.floatOnly);

                fieldp.get("增量血量", () -> String.valueOf(comp.healthDelta), s -> comp.healthDelta = Float.parseFloat(s), ActorUtils.floatOnly);

                fieldp.get("生成间隔(tick)", () -> String.valueOf(comp.spawnDelta), s -> comp.spawnDelta = Integer.parseInt(s), ActorUtils.digitOnly);
            });

            register(HealthRegenComp.class, (comp, t) -> {
                t.defaults().minWidth(80f);
                t.add(new Label("生命恢复/s", sLabel));
                TextField field1 = new TextField(String.valueOf(comp.rate), sTextF);
                t.add(field1).row();
                field1.setTextFieldFilter(ActorUtils.floatOnly);
                field1.setTextFieldListener((f, c) -> {
                    if(!f.getText().isEmpty()){
                        comp.rate = Float.parseFloat(f.getText());
                    }
                });
            });
        }
    };
}
