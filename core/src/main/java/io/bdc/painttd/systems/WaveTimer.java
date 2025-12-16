package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.annotations.*;
import io.bdc.painttd.content.components.logic.*;

import static io.bdc.painttd.game.Game.*;

@Wire
@IsLogicProcess
public class WaveTimer extends BaseEntitySystem {
    public ComponentMapper<SpawnGroupComp> spawnGroupMapper;

    public WaveTimer() {
        super(Aspect.all(SpawnGroupComp.class));
    }

    @Override
    protected void processSystem() {
        if (rules.waveTimerRun) {
            rules.waveTimer -= 1;
            if (rules.waveTimer <= 0) {
                rules.waveTimer += rules.waveDelay;
                rules.wave++;

                var ids = subscription.getEntities();
                for (int i = 0; i < ids.size(); i++) {
                    var id = ids.get(i);
                    SpawnGroupComp sg = spawnGroupMapper.get(id);
                    sg.pull(rules.wave);
                }
            }
        }
    }
}
