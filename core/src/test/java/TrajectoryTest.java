import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.headless.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.content.trajector.*;
import org.junit.*;

public class TrajectoryTest{
    public static Application app;

    public static Processor line, circle, seq, parallel, scale, trigger;

    public static class LineProcessor extends Processor{
        public static ParamVar directionX = new ParamVar("directionX", 0);
        public static ParamVar directionY = new ParamVar("directionY", 1);

        public LineProcessor(){
            super(0, 2, 0, 0);
        }

        @Override
        public void reset(Node node){
            super.reset(node);
            directionX.set(node, 0);
            directionY.set(node, 1);
        }

        @Override
        public void update(float deltaTicks, Node node){
            node.state.shift.set(directionX.get(node), directionY.get(node)).setLength(1f + node.state.ticks * 0.1f);
        }
    }

    @Before
    public void setUp(){
        line = new LineProcessor();

        circle = new Processor(0, 0, 1, 0){
            public StateFVar degree = new StateFVar("degree", 0);

            @Override
            public void reset(Node node){
                super.reset(node);
                degree.set(node, 0);
            }

            @Override
            public void update(float deltaTicks, Node node){
                float rotSpeed = 10f;
                float radius = 10f;
                float rotDirection = 1f;

                float lastDegree = degree.get(node);
                float deltaDegree = deltaTicks * rotSpeed;
                degree.set(node, lastDegree + deltaDegree * rotDirection);
                //计算圆周上移动至下一个点的步进向量
                node.state.shift.set(1, 0).setLength(radius * deltaDegree / 180f * MathUtils.PI).rotateDeg(lastDegree).rotateDeg((90 + deltaDegree / 2f * rotDirection));
            }
        };

        scale = new Processor(1, 0, 0, 0){
            @Override
            public void update(float deltaTicks, Node node){
                float scale = 2f;
                if(node.children.size <= 0) return;
                var child = node.children.get(0);
                if(child != null){
                    child.update(deltaTicks);
                    node.state.shift.set(child.state.shift).scl(scale);
                }
            }
        };

        seq = new Processor(100, 0, 0, 1){
            //stateInts的0号位存储当前子节点索引
            public StateIVar current = new StateIVar("current", 0);

            @Override
            public void initial(Node node){
                super.initial(node);
                node.parameter.maxTicks = Float.MAX_VALUE;
            }

            @Override
            public void update(float deltaTicks, Node node){
                node.state.shift.setZero();

                if(node.children.size <= 0) return;

                int cur = current.get(node);

                if(cur >= node.children.size){
                    complete(node);
                    node.state.shift.setZero();
                }

                var child = node.getChild(cur);

                if(child != null && child.complete == Node.NodeState.process){
                    child.update(deltaTicks);
                    node.state.shift.set(child.state.shift);
                }

                if(child == null || child.complete == Node.NodeState.complete){
                    current.set(node, cur + 1);
                }
            }
        };

        parallel = new Processor(100, 0, 0, 0){
            @Override
            public void initial(Node node){
                super.initial(node);
                node.parameter.maxTicks = Float.MAX_VALUE;
            }

            @Override
            public void update(float deltaTicks, Node node){
                node.state.shift.setZero();
                int count = 0;
                for(var child : node.children){
                    if(child.complete != Node.NodeState.process) continue;
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

        trigger = new Processor(0, 1, 0, 0){
            @Override
            public void update(float deltaTicks, Node node){
                node.tree.fire((int)node.gp(0), node);
                complete(node);
            }
        };
    }

    @Test
    public void t1_circleTest(){
        Gdx.app.log("test1", "测试圆形步进轨迹");
        Tree tree = new Tree();
        tree.add(circle, null);
        for(int i = 0; i < 12; i++){
            tree.update(1f);
            Gdx.app.log("test1", i + " shift " + tree.getShift().x + " " + tree.getShift().y);
        }
    }

    @Test
    public void t2_scaleTest(){
        Gdx.app.log("test2", "测试缩放直线轨迹");
        Tree tree = new Tree();
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
        Tree tree = new Tree();
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
        Tree tree = new Tree();
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

    @Test
    public void t5_triggerAndContext(){
        Gdx.app.log("test5", "测试触发器和上下文");
        Vector2 vec = new Vector2(-1, -2);
        Vector2 copy = vec.cpy();

        Context testX = new Context(){
            @Override
            public float get(){
                return vec.x;
            }
        };

        Context testY = new Context(){
            @Override
            public float get(){
                return vec.y;
            }
        };

        Tree tree = new Tree();
        tree.addContext(testX);
        tree.addContext(testY);
        tree.triggers.add((t, n) -> {
            Gdx.app.log("test5", "树触0号被调用");
        });
        tree.triggers.add((t, n) -> {
            Gdx.app.log("test5", "树触1号被调用, 检查触发唤起节点的param 0号位 值为: " + n.gp(0));
            copy.x = n.gp(0);
            Gdx.app.log("test5", "修改外部向量的副本copy.x为该值");
        });

        var root = tree.add(seq, null);
        //直线轨迹持续10t
        var lineNode = tree.add(line, root);
        lineNode.parameter.maxTicks = 10;
        //为直线注入指定的方向
        testX.addInjection(lineNode, LineProcessor.directionX);
        testY.addInjection(lineNode, LineProcessor.directionY);

        //第二个子轨迹是单次触发
        var triNode = tree.add(trigger, root);
        //为触发处理器注入指定的树触发器
        //触发1号树触发器
        triNode.parameter.data.set(0, 1);

        for(int i = 0; i < 20; i++){
            tree.update(1f);
            Gdx.app.log("test5", i + " shift " + tree.getShift().x + " " + tree.getShift().y);
        }

        Gdx.app.log("test5", "预期结果: 0~9t为直线步进, 与外部向量vec = " + vec + "同向; 10t单次触发'树触1号'报告节点参数并将外部向量copy更新为:" + copy + "; 10~19t为零向量");
    }

    @Test
    public void t6_copy(){
        Gdx.app.log("test6", "测试复制");
        Tree tree = new Tree();
        var root = tree.add(parallel, null);
        //第一个子轨迹持续10t
        var next = tree.add(circle, root);
        next.parameter.maxTicks = 10;
        //第二个子轨迹持续15t
        next = tree.add(line, root);
        next.parameter.maxTicks = 15;
        //5t的空结果

        Tree tree2 = new Tree();
        tree2.copy(tree);

        for(int i = 0; i < 20; i++){
            tree.update(1f);
            tree2.update(1f);
            Gdx.app.log("test6", i + " shift " + tree.getShift().x + " " + tree.getShift().y + " " + tree2.getShift().x + " " + tree2.getShift().y);
        }

        Gdx.app.log("test6", "预期结果: 两棵树输出相同. 0~9t为圆形直线步进相加, 10~14t为直线步进, 15~19t为零向量");

        Gdx.app.log("test6", "测试拷贝是否清空状态");
        Tree tree3 = new Tree();
        tree3.copy(tree2);
        for(int i = 0; i < 20; i++){
            tree3.update(1f);
            Gdx.app.log("test6", i + " shift " + tree3.getShift().x + " " + tree3.getShift().y);
        }

        Gdx.app.log("test6", "预期结果: 与前两棵树输出相同. 0~9t为圆形直线步进相加, 10~14t为直线步进, 15~19t为零向量");
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
