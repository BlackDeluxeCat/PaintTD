import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.headless.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.content.*;
import org.junit.*;

public class TrajectoryTest{
    public static Application app;

    public static TrajectoryProcessor line, circle, seq, parallel, scale;

    @Before
    public void setUp(){
        line = new TrajectoryProcessor(){
            @Override
            public void update(float deltaTicks, TrajectoryTree.TrajectoryNode node){
                node.state.shift.set(1f, 0f).setLength(1f + node.state.ticks * 0.1f);
            }
        };

        circle = new TrajectoryProcessor(){
            public static int sDegree = 0;
            {
                stateFloatsSize = 1;
            }

            @Override
            public void reset(TrajectoryTree.TrajectoryNode node){
                super.reset(node);
                node.ssf(sDegree, 0f);
            }

            @Override
            public void update(float deltaTicks, TrajectoryTree.TrajectoryNode node){
                float rotSpeed = 10f;
                float radius = 10f;
                float rotDirection = 1f;

                float lastDegree = node.gsf(sDegree);
                float deltaDegree = deltaTicks * rotSpeed;
                node.state.floats.set(sDegree, lastDegree + deltaDegree * rotDirection);
                //计算圆周上移动至下一个点的步进向量
                node.state.shift.set(1, 0).setLength(radius * deltaDegree / 180f * MathUtils.PI).rotateDeg(lastDegree).rotateDeg((90 + deltaDegree / 2f * rotDirection));
            }
        };

        scale = new TrajectoryProcessor(){
            {
                maxChildrenSize = 1;
            }
            @Override
            public void update(float deltaTicks, TrajectoryTree.TrajectoryNode node){
                float scale = 2f;
                if(node.children.size <= 0) return;
                var child = node.children.get(0);
                if(child != null){
                    child.update(deltaTicks);
                    node.state.shift.set(child.state.shift).scl(scale);
                }
            }
        };

        seq = new TrajectoryProcessor(){
            public static int sChildIndex = 0;//使用stateInts的0号位存储当前子节点索引

            {
                stateIntsSize = 1;
                maxChildrenSize = 100;
            }

            @Override
            public void initial(TrajectoryTree.TrajectoryNode node){
                super.initial(node);
                node.parameter.maxTicks = Float.MAX_VALUE;
            }

            @Override
            public void update(float deltaTicks, TrajectoryTree.TrajectoryNode node){
                node.state.shift.setZero();

                if(node.children.size <= 0) return;

                if(node.gsi(sChildIndex) >= node.children.size){
                    complete(node);
                    node.state.shift.setZero();
                }

                var child = node.getChild(node.gsi(sChildIndex));

                if(child != null && child.complete == TrajectoryTree.NodeState.process){
                    child.update(deltaTicks);
                    node.state.shift.set(child.state.shift);
                }

                if(child == null || child.complete == TrajectoryTree.NodeState.complete){
                    node.ssi(sChildIndex, node.gsi(sChildIndex) + 1);
                }
            }
        };

        parallel = new TrajectoryProcessor(){
            {
                maxChildrenSize = 100;
            }

            @Override
            public void initial(TrajectoryTree.TrajectoryNode node){
                super.initial(node);
                node.parameter.maxTicks = Float.MAX_VALUE;
            }

            @Override
            public void update(float deltaTicks, TrajectoryTree.TrajectoryNode node){
                node.state.shift.setZero();
                int count = 0;
                for(var child : node.children){
                    if(child.complete != TrajectoryTree.NodeState.process) continue;
                    child.update(deltaTicks);
                    node.state.shift.add(child.state.shift);
                    count++;
                }

                if(count <= 0){
                    complete(node);
                    node.state.shift.setZero();
                }
            }
        };
    }

    @Test
    public void t1_circleTest(){
        Gdx.app.log("test1", "测试圆形步进轨迹");
        TrajectoryTree tree = new TrajectoryTree();
        tree.add(circle, null);
        for(int i = 0; i < 12; i++){
            tree.update(1f);
            Gdx.app.log("test1", i + " shift " + tree.getShift().x + " " + tree.getShift().y);
        }
    }

    @Test
    public void t2_scaleTest(){
        Gdx.app.log("test2", "测试缩放直线轨迹");
        TrajectoryTree tree = new TrajectoryTree();
        var root = tree.add(scale, null);
        tree.add(line, root);
        for(int i = 0; i < 12; i++){
            tree.update(1f);
            Gdx.app.log("test2", i + " shift " + tree.getShift().x + " " + tree.getShift().y);
        }
    }

    @Test
    public void t3_seqTest(){
        Gdx.app.log("test3", "测试串行轨迹");
        TrajectoryTree tree = new TrajectoryTree();
        var root = tree.add(seq, null);
        //第一个子轨迹持续10t
        var next = tree.add(circle, root);
        next.parameter.maxTicks = 10;
        //第二个子轨迹持续15t
        next = tree.add(line, root);
        next.parameter.maxTicks = 15;
        //5t的空结果


        for(int i = 0; i < 30; i++){
            tree.update(1f);
            Gdx.app.log("test3", i + " shift " + tree.getShift().x + " " + tree.getShift().y);
        }

        Gdx.app.log("test3", "预期结果: 0~9t为圆形步进, 10~24t为直线步进, 25~29t为零向量");
    }

    @Test
    public void t4_parallelTest(){
        Gdx.app.log("test4", "测试并行轨迹");
        TrajectoryTree tree = new TrajectoryTree();
        var root = tree.add(parallel, null);
        //第一个子轨迹持续10t
        var next = tree.add(circle, root);
        next.parameter.maxTicks = 10;
        //第二个子轨迹持续15t
        next = tree.add(line, root);
        next.parameter.maxTicks = 15;
        //5t的空结果

        for(int i = 0; i < 20; i++){
            tree.update(1f);
            Gdx.app.log("test4", i + " shift " + tree.getShift().x + " " + tree.getShift().y);
        }
        Gdx.app.log("test4", "预期结果: 0~9t为圆形直线步进相加, 10~14t为直线步进, 15~19t为零向量");
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
