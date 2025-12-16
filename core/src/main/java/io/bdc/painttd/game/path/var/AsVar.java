package io.bdc.painttd.game.path.var;

/**
 * 节点变量接口
 * <p>set为节点变量指定值, 可使用于input变量在节点UI编辑器场景.</p>
 * <p>as以T类型返回节点内部缓存, 使用于output变量在linkable同步场景, 方便类型安全的策略模式类型转换介入处理.</p>
 */
//暂时放弃在接口层内部完成数据类型转换. 让我们在节点层实现, 给玩家自由组合吧
public interface AsVar<T> {
    /** 为节点变量指定值, 存入缓存. */
    default void set(T value) {
    }

    /**
     * 输出节点内部缓存. linkable同步 output端用途.
     * <p>out的缓存被所属节点的重新计算直接赋值, 因此as应该返回节点内部缓存. in的sync行为根据缓存帧检查有2种情况, 第一是重新计算+as读取out, 第二是不读取.
     * 使用as而不直接赋值, 是为了在as方法中可以介入处理类型转换, 避免类型转换异常.</p>
     */
    default T as() {
        return null;
    }
}
