import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.headless.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.*;
import io.bdc.painttd.content.trajector.*;
import org.junit.*;

//路径树性能测试
public class PathPerformanceTest{
    public static Application app;
    public long[] timer = new long[1000];

    public void timerStart(int index){
        timer[index] = TimeUtils.nanoTime();
    }

    public void timerEnd(int index){
        timer[index] = TimeUtils.nanoTime() - timer[index];
    }

    public long timerGet(int index){
        return timer[index];
    }

    @Test
    public void run(){
        Tree tree = simpleTree();
        Array<Tree> list = duplicateList(tree, 10000);

        timerStart(0);

        int N = 60;

        for(int i = 0; i < N; i++){
            timerStart(1);
            updateList(list);
            timerEnd(1);
//            int j = MathUtils.random(100);
//            Gdx.app.log("#" + i, TimeUtils.nanosToMillis(timerGet(1)) + "ms" + list.get(j).getShift());
        }

        timerEnd(0);
        Gdx.app.log("total", "cost " + TimeUtils.nanosToMillis(timerGet(0)) + "ms in " + N + " frames(" + N * 16f + "ms)");
    }

    //20*2+1个简单节点
    public Tree simpleTree(){
        Tree tree = new Tree();

        var root = tree.add(Paths.par, null);

        for(int i = 0; i < 20; i++){
            var s = tree.add(Paths.scl, root);
            var l = tree.add(Paths.line, s);
        }

        return tree;
    }

    public void updateList(Array<Tree> list){
        for(Tree tree : list) tree.update(1);
    }

    public Array<Tree> duplicateList(Tree origin, int duplicate){
        Array<Tree> list = new Array<>();
        for(int i = 0; i < duplicate; i++){
            Tree newTree = new Tree();
            newTree.copy(origin);
            list.add(newTree);
        }
        return list;
    }

    @BeforeClass
    public static void setUpClass(){
        if(app == null){
            app = new HeadlessApplication(new ApplicationAdapter(){
            });
            app.setLogLevel(Application.LOG_DEBUG);
        }
    }

    @AfterClass
    public static void tearDownClass(){
        if(app != null){
            app.exit();
            app = null;
        }
    }
}
