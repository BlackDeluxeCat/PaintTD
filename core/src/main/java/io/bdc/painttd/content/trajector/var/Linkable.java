package io.bdc.painttd.content.trajector.var;

import com.badlogic.gdx.utils.*;

public interface Linkable{
    /** 对远端port做读取 */
    void readLink(@Null Linkable port);

    /** 对编辑器中创建link做有效性检查 */
    boolean canLink(Linkable port);
}