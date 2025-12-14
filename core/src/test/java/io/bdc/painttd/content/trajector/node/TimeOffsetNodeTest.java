package io.bdc.painttd.content.trajector.node;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.headless.*;
import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.NodeGraph;
import io.bdc.painttd.content.trajector.var.*;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * TimeOffsetNode 单元测试
 * 测试时间偏移节点的时间重映射和转发功能
 */
public class TimeOffsetNodeTest {
    public static HeadlessApplication app;

    private NodeGraph nodeGraph;
    private TimeOffsetNode timeOffsetNode;
    private TestSourceNode sourceNode;
    
    /**
     * 测试用的源节点，提供一个简单的 FloatV 输出
     */
    private static class TestSourceNode extends Node {
        public FloatV output = new FloatV(false);
        public float lastSyncFrame = -1;
        
        @Override
        public void registerVars() {
            outputs.add(output);
        }
        
        @Override
        public boolean calc(float frame) {
            // 简单的测试逻辑：输出帧号
            output.cache = frame;
            lastSyncFrame = frame;
            return true;
        }
        
        @Override
        public LinkableVar getSyncOutput(float frame, int targetOutputPort) {
            if (targetOutputPort == 0) {
                calc(frame);
                return output;
            }
            return null;
        }
        
        @Override
        public Node obtain() {
            TestSourceNode copy = new TestSourceNode();
            copy.output.cache = this.output.cache;
            return copy;
        }
        
        @Override
        public void free() {
            output.reset();
        }
        
        @Override
        public void reset() {
            output.reset();
            lastSyncFrame = -1;
        }
    }
    
    @BeforeClass
    public static void setUpClass() {
        app = new HeadlessApplication(new ApplicationAdapter(){});
    }
    
    @AfterClass
    public static void tearDownClass() {
        app.exit();
    }
    
    @Before
    public void setUp() {
        // 初始化网络
        nodeGraph = new NodeGraph();
        
        // 创建源节点
        sourceNode = new TestSourceNode();
        int sourceIndex = nodeGraph.add(sourceNode);
        
        // 创建时间偏移节点
        timeOffsetNode = new TimeOffsetNode();
        int offsetIndex = nodeGraph.add(timeOffsetNode);

        // 设置连接：源节点 -> 时间偏移节点
        timeOffsetNode.inPort.sourceNode = sourceIndex;
        timeOffsetNode.inPort.sourceOutputPort = 0;
        
        // 设置网络根节点
        nodeGraph.rootIndex = offsetIndex;
        
        // 设置节点的网络引用
        sourceNode.nodeGraph = nodeGraph;
        timeOffsetNode.nodeGraph = nodeGraph;
    }
    
    @After
    public void tearDown() {
        if (nodeGraph != null) {
            nodeGraph.clear();
        }
    }
    
    @Test
    public void testBasicTimeOffset() {
        // 设置偏移量为 5.0
        timeOffsetNode.offset.cache = 5.0f;
        timeOffsetNode.offset.cacheValue = false; // 禁用缓存，强制同步
        
        // 测试时间偏移
        float inputFrame = 10.0f;
        float expectedFrame = inputFrame + 5.0f; // 15.0f
        
        // 获取同步输出
        timeOffsetNode.calc(inputFrame);
        LinkableVar result = timeOffsetNode.getSyncOutput(inputFrame, 0);
        
        assertNotNull("同步输出不应为 null", result);
        assertTrue("结果应该是 FloatV 类型", result instanceof FloatV);
        
        FloatV floatResult = (FloatV) result;
        assertEquals("时间偏移应该正确", expectedFrame, floatResult.cache, 0.001f);
        assertEquals("源节点应该接收到偏移后的帧", expectedFrame, sourceNode.lastSyncFrame, 0.001f);
    }
    
    @Test
    public void testNegativeOffset() {
        // 设置负偏移量
        timeOffsetNode.offset.cache = -3.0f;
        timeOffsetNode.offset.cacheValue = false;
        
        float inputFrame = 10.0f;
        float expectedFrame = inputFrame - 3.0f; // 7.0f
        
        LinkableVar result = timeOffsetNode.getSyncOutput(inputFrame, 0);
        
        assertNotNull("同步输出不应为 null", result);
        FloatV floatResult = (FloatV) result;
        assertEquals("负偏移应该正确", expectedFrame, floatResult.cache, 0.001f);
    }
    
    @Test
    public void testZeroOffset() {
        // 设置零偏移量
        timeOffsetNode.offset.cache = 0.0f;
        timeOffsetNode.offset.cacheValue = false;
        
        float inputFrame = 10.0f;
        float expectedFrame = inputFrame; // 10.0f
        
        LinkableVar result = timeOffsetNode.getSyncOutput(inputFrame, 0);
        
        assertNotNull("同步输出不应为 null", result);
        FloatV floatResult = (FloatV) result;
        assertEquals("零偏移应该保持原值", expectedFrame, floatResult.cache, 0.001f);
    }
    
    @Test
    public void testOffsetVariableSync() {
        // 创建一个特殊的偏移量源节点，总是返回固定值
        TestSourceNode offsetSource = new TestSourceNode() {
            @Override
            public boolean calc(float frame) {
                // 重写 calc 方法，总是返回固定的偏移量
                output.cache = 7.5f;
                return true;
            }
        };
        int offsetSourceIndex = nodeGraph.add(offsetSource);
        offsetSource.nodeGraph = nodeGraph;
        
        // 连接偏移量源
        timeOffsetNode.offset.sourceNode = offsetSourceIndex;
        timeOffsetNode.offset.sourceOutputPort = 0;
        timeOffsetNode.offset.cacheValue = false; // 禁用缓存，强制同步
        
        float inputFrame = 10.0f;
        float expectedFrame = inputFrame + 7.5f; // 17.5f
        
        LinkableVar result = timeOffsetNode.getSyncOutput(inputFrame, 0);
        
        assertNotNull("同步输出不应为 null", result);
        FloatV floatResult = (FloatV) result;
        assertEquals("动态偏移应该正确", expectedFrame, floatResult.cache, 0.001f);
    }
    
    @Test
    public void testInvalidOutputPort() {
        // 测试无效的输出端口
        LinkableVar result = timeOffsetNode.getSyncOutput(10.0f, 1);
        
        assertNull("无效输出端口应返回 null", result);
    }
    
    @Test
    public void testDisconnectedInput() {
        // 测试断开连接的输入
        timeOffsetNode.inPort.sourceNode = -1;
        
        LinkableVar result = timeOffsetNode.getSyncOutput(10.0f, 0);
        
        assertNull("断开连接的输入应返回 null", result);
    }
    
    @Test
    public void testMultipleFrames() {
        // 设置偏移量
        timeOffsetNode.offset.cache = 2.0f;
        timeOffsetNode.offset.cacheValue = false;
        
        // 测试多个帧值
        float[] inputFrames = {0.0f, 5.0f, 10.0f, 15.0f};
        float[] expectedFrames = {2.0f, 7.0f, 12.0f, 17.0f};
        
        for (int i = 0; i < inputFrames.length; i++) {
            LinkableVar result = timeOffsetNode.getSyncOutput(inputFrames[i], 0);
            
            assertNotNull("帧 " + inputFrames[i] + " 的输出不应为 null", result);
            assertTrue("结果应该是 FloatV 类型", result instanceof FloatV);
            
            FloatV floatResult = (FloatV) result;
            assertEquals("帧 " + inputFrames[i] + " 的偏移应该正确", 
                        expectedFrames[i], floatResult.cache, 0.001f);
        }
    }
    
    @Test
    public void testRemappedFrameField() {
        // 测试 remappedFrame 字段的设置
        timeOffsetNode.offset.cache = 3.5f;
        timeOffsetNode.offset.cacheValue = false;
        
        float inputFrame = 8.0f;
        
        // 调用 calc 方法直接测试
        timeOffsetNode.calc(inputFrame);
        
        assertEquals("remappedFrame 应该被正确设置", 
                    inputFrame + 3.5f, timeOffsetNode.remappedFrame, 0.001f);
    }
}