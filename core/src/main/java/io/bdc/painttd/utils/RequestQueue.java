package io.bdc.painttd.utils;

import com.badlogic.gdx.utils.*;

/**
 * 维护一个请求队列.
 * 池方法需要自行实现
 */
public abstract class RequestQueue<T extends RequestQueue.Request> {
    public final Queue<T> queue = new Queue<>();

    public void add(T t) {
        queue.addLast(t);
    }

    public void clearHandled() {
        for (int i = 0; i < queue.size; i++) {
            var req = queue.removeFirst();
            if (req.handled) {
                free(req);
            } else {
                queue.addLast(req);
            }
        }
    }

    public void clear() {
        while (!queue.isEmpty()) {
            T poolable = queue.removeFirst();
            free(poolable);
        }
    }

    public abstract T obtain();

    public abstract void free(T t);

    public abstract static class Request implements Pool.Poolable {
        public boolean handled;

        public Request() {
        }

        /**
         * 标记为已处理. 在处理系统之间传递处理状态.
         */
        public void handle() {
            handled = true;
        }

        @Override
        public void reset() {
            handled = false;
        }
    }
}
