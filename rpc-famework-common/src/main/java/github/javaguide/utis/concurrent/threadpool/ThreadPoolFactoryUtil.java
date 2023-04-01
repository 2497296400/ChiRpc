package github.javaguide.utis.concurrent.threadpool;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class ThreadPoolFactoryUtil {
    private static final Map<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();

    public ThreadPoolFactoryUtil() {
    }

    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix) {
        CustomThreadPoolConfig customThreadPoolConfig = new CustomThreadPoolConfig();
        return createCustomThreadPoolIfAbsent(threadNamePrefix, customThreadPoolConfig, false);
    }

    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix, CustomThreadPoolConfig customThreadPoolConfig, Boolean daemon) {
        ExecutorService threadPool = THREAD_POOLS.computeIfAbsent(threadNamePrefix, k -> {
            return createThreadPool(threadNamePrefix, customThreadPoolConfig, daemon);
        });
        if (threadPool.isShutdown() || threadPool.isTerminated()) {
            THREAD_POOLS.remove(threadNamePrefix);
            threadPool = createThreadPool(threadNamePrefix, customThreadPoolConfig, daemon);
            THREAD_POOLS.put(threadNamePrefix, threadPool);
        }
        return threadPool;
    }

    public static ExecutorService createThreadPool(String threadNamePrefix, CustomThreadPoolConfig customThreadPoolConfig, boolean daemon) {
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        return new ThreadPoolExecutor(customThreadPoolConfig.getCorePoolSize(), 
                customThreadPoolConfig.getMaximumPoolSize(), customThreadPoolConfig.getKeepAliveTime(),
                customThreadPoolConfig.getUnit(),  customThreadPoolConfig.getWorkQueue(),threadFactory);
    }

    public static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        if (threadNamePrefix != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder()
                        .setDaemon(daemon)
                        .setNameFormat(threadNamePrefix + "-%d")
                        .build();
            } else {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
            }
        }
        return Executors.defaultThreadFactory();
    }

    public static void shutDownAllThreadPool() {
        log.info("call shutDownAllThreadPool method");
        THREAD_POOLS.entrySet().parallelStream().forEach(item -> {
            ExecutorService executorService = item.getValue();
            executorService.shutdown();
            log.info("shut down thread pool [{}] [{}]", item.getKey(), executorService.isTerminated());
            try {
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error("Thread pool never terminated");
                executorService.shutdown();
            }
        });
    }

    public static void printThreadPoolStatus(ThreadPoolExecutor threadPool) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, createThreadFactory("print-thread-pool-status", false));
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.info("============ThreadPool Status=============");
            log.info("ThreadPool Size: [{}]", threadPool.getPoolSize());
            log.info("Active Threads: [{}]", threadPool.getActiveCount());
            log.info("Number of Tasks : [{}]", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());
            log.info("===========================================");
        }, 0, 1, TimeUnit.SECONDS);
    }
}