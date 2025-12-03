package io.bdc.painttd.game;

import com.artemis.*;
import com.artemis.utils.Sort;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.systems.*;
import io.bdc.painttd.utils.*;

import static io.bdc.painttd.game.Game.*;

/**
 * 使用层管理系统优先级的系统执行策略.
 * 注册的层和系统均不可删除.
 * 发生注册和修改后, 必须调用 {@link #sort()} 进行排序.
 */
public class LayerInvocationStrategy extends InvocationStrategy{
    public LayerManager<BaseSystem> lm = new LayerManager<>();

    public boolean frameLogicProcess;
    public long time, lastTime, stackTime = 0;

    public LayerInvocationStrategy(){
    }

    @Override
    protected void process(){


        time = TimeUtils.millis();
        stackTime += time - lastTime;
        lastTime = time;
        if(stackTime > 1000 / lfps){
            stackTime = 0;
            frameLogicProcess = true;
        }

        BaseSystem[] systemsData = systems.getData();
        for(int i = 0, s = systems.size(); s > i; i++){
            if(disabled.get(i)) continue;
            if((frameLogicProcess && !rules.isPause) || systemsData[i].getClass().getAnnotation(IsLogicProcess.class) == null){
                updateEntityStates();
                systemsData[i].process();
            }

        }

        updateEntityStates();

        frameLogicProcess = false;
    }

    @Override
    protected void initialize(){
        super.initialize();
        sort();
    }

    /**
     * 排序 {@link #systems}. 当修改了某个Layer之后, 进行排序. 三个Manager不参与排序.
     */
    public void sort(){
        lm.sort();
        Sort.instance().sort(systems.getData(), (s1, s2) -> {
            float z1 = lm.getZ(s1);
            float z2 = lm.getZ(s2);
            return Float.compare(z1, z2);
        }, 2, systems.size());
    }
}
