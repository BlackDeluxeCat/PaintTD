package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.game.*;
import io.bdc.painttd.game.path.*;
import io.bdc.painttd.game.path.context.*;

public class NodeGraphExecute extends IteratingSystem {
    public ComponentMapper<NodeGraphExecutorComp> ngrm;
    public ComponentMapper<TeamComp> tm;

    public Contexts nodeGraphContexts = new Contexts();
    protected EntityContext entityContext = new DefaultEntityContext();

    public NodeGraphExecute() {
        super(Aspect.all(NodeGraphExecutorComp.class, TeamComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void initialize() {
        super.initialize();
        nodeGraphContexts.set(EntityContext.class, entityContext);
    }

    @Override
    protected void process(int entityId) {
        TeamComp team = tm.get(entityId);
        NodeGraph graph = Game.rules.getNodeGraph(team.team);
        if(graph == null) return;

        entityContext.reset();

        entityContext.set(entityId, this.world);
        NodeGraphExecutorComp executor = ngrm.get(entityId);
        executor.executor.update(graph, nodeGraphContexts, 1f);
    }
}
