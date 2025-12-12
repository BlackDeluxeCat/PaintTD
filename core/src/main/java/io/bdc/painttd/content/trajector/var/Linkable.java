package io.bdc.painttd.content.trajector.var;

public interface Linkable{
    /** 对远端port做读取 */
    void syncLink(Linkable port);

    /** 对编辑器中创建link做有效性检查 */
    boolean canLink(Linkable port);
}