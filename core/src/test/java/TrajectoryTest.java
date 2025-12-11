//import com.badlogic.gdx.*;
//import com.badlogic.gdx.backends.headless.*;
//import com.badlogic.gdx.math.*;
//import io.bdc.painttd.content.trajector.*;
//import io.bdc.painttd.content.trajector.processor.*;
//import io.bdc.painttd.content.trajector.var.*;
//import org.junit.*;
//
////20251205
//public class TrajectoryTest{
//    public static Application app;
//
//    public static Processor line, circle, seq, parallel, scale, trigger, line2;
//
//    public static class LineProcessor extends Processor{
//        public static ParamF directionX = new ParamF("directionX", 0);
//        public static ParamF directionY = new ParamF("directionY", 1);
//
//        public LineProcessor(){
//            super(0, 2, 0, 0);
//        }
//
//        @Override
//        public void restart(Node node){
//            super.restart(node);
//            directionX.setFloat(0, node);
//            directionY.setFloat(1, node);
//        }
//
//        @Override
//        public void update(float deltaTicks, Node node){
//            node.state.shift.set(directionX.asFloat(node), directionY.asFloat(node)).setLength(1f + node.state.ticks * 0.1f);
//        }
//    }
//
//    @Before
//    public void setUp(){
//        line = new LineProcessor();
//
//        line2 = new io.bdc.painttd.content.trajector.processor.LineProcessor();
//
//        circle = new Processor(0, 0, 1, 0){
//            public StateF degree = new StateF("degree", 0);
//
//            @Override
//            public void restart(Node node){
//                super.restart(node);
//                degree.setFloat(0, node);
//            }
//
//            @Override
//            public void update(float deltaTicks, Node node){
//                float rotSpeed = 10f;
//                float radius = 10f;
//                float rotDirection = 1f;
//
//                float lastDegree = degree.asFloat(node);
//                float deltaDegree = deltaTicks * rotSpeed;
//                degree.setFloat(lastDegree + deltaDegree * rotDirection, node);
//                //计算圆周上移动至下一个点的步进向量
//                node.state.shift.set(1, 0).setLength(radius * deltaDegree / 180f * MathUtils.PI).rotateDeg(lastDegree).rotateDeg((90 + deltaDegree / 2f * rotDirection));
//            }
//        };
//
//        scale = new ScaleProcessor();
//
//        seq = new SeqProcessor(100);
//
//        parallel = new ParallelProcessor(100);
//
//        trigger = new Processor(0, 1, 0, 0){
//            @Override
//            public void update(float deltaTicks, Node node){
//                node.tree.fire((int)node.gp(0), node);
//                complete(node);
//            }
//        };
//    }
//
//    @Test
//    public void t1_circleTest(){
//        Gdx.app.log("test1", "测试圆形步进轨迹");
//        Tree tree = new Tree();
//        tree.add(circle, null);
//        for(int i = 0; i < 12; i++){
//            tree.update(1f);
//            Gdx.app.log("test1", i + " shift " + tree.getShift());
//        }
//    }
//
//    @Test
//    public void t2_scaleTest(){
//        Gdx.app.log("test2", "测试缩放直线轨迹");
//        Tree tree = new Tree();
//        var root = tree.add(scale, null);
//        tree.add(line, root);
//        for(int i = 0; i < 12; i++){
//            tree.update(1f);
//            Gdx.app.log("test2", i + " shift " + tree.getShift());
//        }
//    }
//
//    @Test
//    public void t3_seqTest(){
//        Gdx.app.log("test3", "测试串行轨迹");
//        Tree tree = new Tree();
//        var root = tree.add(seq, null);
//        //第一个子轨迹持续10t
//        var next = tree.add(circle, root);
//        next.parameter.maxTicks = 10;
//        //第二个子轨迹持续15t
//        next = tree.add(line, root);
//        next.parameter.maxTicks = 15;
//        //5t的空结果
//
//
//        for(int i = 0; i < 30; i++){
//            tree.update(1f);
//            Gdx.app.log("test3", i + " shift " + tree.getShift());
//        }
//
//        Gdx.app.log("test3", "预期结果: 0~9t为圆形步进, 10~24t为直线步进, 25~29t为零向量");
//    }
//
//    @Test
//    public void t4_parallelTest(){
//        Gdx.app.log("test4", "测试并行轨迹");
//        Tree tree = new Tree();
//        var root = tree.add(parallel, null);
//        //第一个子轨迹持续10t
//        var next = tree.add(circle, root);
//        next.parameter.maxTicks = 10;
//        //第二个子轨迹持续15t
//        next = tree.add(line, root);
//        next.parameter.maxTicks = 15;
//        //5t的空结果
//
//        for(int i = 0; i < 20; i++){
//            tree.update(1f);
//            Gdx.app.log("test4", i + " shift " + tree.getShift());
//        }
//        Gdx.app.log("test4", "预期结果: 0~9t为圆形直线步进相加, 10~14t为直线步进, 15~19t为零向量");
//    }
//
////    @Test
////    @Ignore
////    public void t5_triggerAndContext(){
////        Gdx.app.log("test5", "测试触发器和上下文");
////        Vector2 vec = new Vector2(-1, -2);
////        Vector2 copy = vec.cpy();
////
////        VarInjector testX = new VarInjector(){
////            @Override
////            public float get(){
////                return vec.x;
////            }
////        };
////
////        VarInjector testY = new VarInjector(){
////            @Override
////            public float get(){
////                return vec.y;
////            }
////        };
////
////        Tree tree = new Tree();
////        tree.addContext(testX);
////        tree.addContext(testY);
////        tree.triggers.add((t, n) -> {
////            Gdx.app.log("test5", "树触0号被调用");
////        });
////        tree.triggers.add((t, n) -> {
////            Gdx.app.log("test5", "树触1号被调用, 检查触发唤起节点的param 0号位 值为: " + n.gp(0));
////            copy.x = n.gp(0);
////            Gdx.app.log("test5", "修改外部向量的副本copy.x为该值");
////        });
////
////        var root = tree.add(seq, null);
////        //直线轨迹持续10t
////        var lineNode = tree.add(line, root);
////        lineNode.parameter.maxTicks = 10;
////        //为直线注入指定的方向
////        testX.addInjection(lineNode, LineProcessor.directionX);
////        testY.addInjection(lineNode, LineProcessor.directionY);
////
////        //第二个子轨迹是单次触发
////        var triNode = tree.add(trigger, root);
////        //为触发处理器注入指定的树触发器
////        //触发1号树触发器
////        triNode.parameter.data.set(0, 1);
////
////        for(int i = 0; i < 20; i++){
////            tree.update(1f);
////            Gdx.app.log("test5", i + " shift " + tree.getShift());
////        }
////
////        Gdx.app.log("test5", "预期结果: 0~9t为直线步进, 与外部向量vec = " + vec + "同向; 10t单次触发'树触1号'报告节点参数并将外部向量copy更新为:" + copy + "; 10~19t为零向量");
////    }
//
//    @Test
//    public void t6_copy(){
//        Gdx.app.log("test6", "测试复制");
//        Tree tree = new Tree();
//        var root = tree.add(parallel, null);
//        //第一个子轨迹持续10t
//        var next = tree.add(circle, root);
//        next.parameter.maxTicks = 10;
//        //第二个子轨迹持续15t
//        next = tree.add(line, root);
//        next.parameter.maxTicks = 15;
//        //5t的空结果
//
//        Tree tree2 = new Tree();
//        tree2.copy(tree);
//
//        for(int i = 0; i < 20; i++){
//            tree.update(1f);
//            tree2.update(1f);
//            Gdx.app.log("test6", i + " shift " + tree.getShift() + " " + tree2.getShift());
//        }
//
//        Gdx.app.log("test6", "预期结果: 两棵树输出相同. 0~9t为圆形直线步进相加, 10~14t为直线步进, 15~19t为零向量");
//
//        Gdx.app.log("test6", "测试拷贝是否清空状态");
//        Tree tree3 = new Tree();
//        tree3.copy(tree2);
//        for(int i = 0; i < 20; i++){
//            tree3.update(1f);
//            Gdx.app.log("test6", i + " shift " + tree3.getShift());
//        }
//
//        Gdx.app.log("test6", "预期结果: 与前两棵树输出相同. 0~9t为圆形直线步进相加, 10~14t为直线步进, 15~19t为零向量");
//    }
//
//    @Test
//    public void t7_combo1(){
//        Gdx.app.log("test7", "测试组合1");
//
//        Tree tree = new Tree();
//
//        var root = tree.add(seq, null);
//
//        //第一个串行持续5+5+5t
//        var seq1 = tree.add(seq, root);
//        var r1 = tree.add(line, seq1);
//        r1.parameter.maxTicks = 5;
//        var r2 = tree.add(line, seq1);
//        r2.parameter.maxTicks = 5;
//        var scl1 = tree.add(scale, seq1);
//        var r3 = tree.add(line, scl1);
//        r3.parameter.maxTicks = 5;
//
//        //第二个并行持续5t
//        var para1 = tree.add(parallel, root);
//        var r4 = tree.add(line, para1);
//        r4.parameter.maxTicks = 5;
//        var r5 = tree.add(line, para1);
//        r5.parameter.maxTicks = 5;
//        var r6 = tree.add(line, para1);
//        r6.parameter.maxTicks = 5;
//
//        //第三个轨迹持续8t
//        var f1 = tree.add(circle, root);
//        f1.parameter.maxTicks = 8;
//
//        for(int i = 0; i < 40; i++){
//            tree.update(1f);
//            Gdx.app.log("test7", i + " shift " + tree.getShift());
//        }
//    }
//
//    @Test
//    public void t8_repeat(){
//        Gdx.app.log("test8", "测试seq重复");
//        Tree tree = new Tree();
//        var root = tree.add(seq, null);
//        SeqProcessor.repeat.setFloat(5, root);
//        //第一个子轨迹持续4t
//        var next = tree.add(line, root);
//        next.parameter.maxTicks = 4;
//
//        for(int i = 0; i < 30; i++){
//            tree.update(1f);
//            Gdx.app.log("test8", i + " shift " + tree.getShift());
//        }
//
//        Gdx.app.log("test8", "预期结果: 0~19t为直线步进五次重复, 20~29t为零向量");
//    }
//
//    @BeforeClass
//    public static void setUpClass(){
//        if(app == null){
//            app = new HeadlessApplication(new ApplicationAdapter(){
//            });
//            app.setLogLevel(Application.LOG_DEBUG);
//        }
//    }
//
//    @AfterClass
//    public static void tearDownClass(){
//        if(app != null){
//            app.exit();
//            app = null;
//        }
//    }
//}
