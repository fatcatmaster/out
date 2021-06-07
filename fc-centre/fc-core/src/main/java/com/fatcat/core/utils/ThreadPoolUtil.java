package com.fatcat.core.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fatcat
 * @description 全局线程池配置工具
 * @create 2021/5/10
 **/
public class ThreadPoolUtil {
    /**
     * 线程池执行者
     */
    public final static ThreadPoolExecutor THREAD_POOL_EXECUTOR;
    /**
     * 核心线程大小
     */
    private final static int CORE_POOL_SIZE = 5;
    /**
     * 线程池最大线程数
     */
    private final static int MAX_POOL_SIZE = 10;
    /**
     * 额外线程空状态生存时间
     */
    private final static int KEEP_ALIVE_TIME = 30 * 1000;
    /**
     * 阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
     */
    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(CORE_POOL_SIZE);

    private static ThreadFactory factory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ThreadPool thread: " + integer.getAndIncrement());
        }
    };

    static {
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, factory);
    }
}
