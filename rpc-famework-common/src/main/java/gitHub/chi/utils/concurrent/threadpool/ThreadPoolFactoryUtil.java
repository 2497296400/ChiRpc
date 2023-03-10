package gitHub.chi.utils.concurrent.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;
@Slf4j
public class ThreadPoolFactoryUtil {
    /**
     * 通过 threadNamePrefix 来区分不同线程池（我们可以把相同 threadNamePrefix 的线程池看作是为同一业务场景服务）。
     * key: threadNamePrefix
     * value: threadPool
     */

    private static final Map<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();

    private ThreadPoolFactoryUtil() {
    }
    
    public static ExecutorService creatrCustomThreadPoolIfAbsent(String threadName) {
        CustomThreadPoolConfig threadPoolConfig = new CustomThreadPoolConfig();
        return creatrCustomThreadPoolIfAbsent(threadPoolConfig, threadName, false);
    }

    public static ExecutorService creatrCustomThreadPoolIfAbsent(CustomThreadPoolConfig threadPoolConfig, String threadName, boolean daemon) {
        ExecutorService threadPool = THREAD_POOLS.computeIfAbsent(threadName, k ->
                createThreadPool(threadPoolConfig, threadName, daemon));
        if (threadPool.isShutdown() || threadPool.isTerminated()) {
            THREAD_POOLS.remove(threadName);
            threadPool = createThreadPool(threadPoolConfig, threadName, daemon);
            THREAD_POOLS.put(threadName, threadPool);
        }
        return threadPool;
    }

    public static ExecutorService createThreadPool(CustomThreadPoolConfig threadPoolConfig, String threadName, boolean daemon) {
        ThreadFactory threadFactory = createThreadFactory(threadName, daemon);
        return new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(), threadPoolConfig.getMaximumPoolSize(),
                threadPoolConfig.getKeepAliveTime(), threadPoolConfig.getUnit(), threadPoolConfig.getWorkQueue(), threadFactory);
    }

    /**
     * 创建 ThreadFactory 。如果threadNamePrefix不为空则使用自建ThreadFactory，否则使用defaultThreadFactory
     *
     * @param threadName 作为创建的线程名字的前缀
     * @param daemon     指定是否为 Daemon Thread(守护线程)
     * @return ThreadFactory
     */
    public static ThreadFactory createThreadFactory(String threadName, Boolean daemon) {
        if (threadName != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder().setNameFormat(threadName + "-%d").setDaemon(daemon).build();
            } else {
                return new ThreadFactoryBuilder().setNameFormat(threadName + "-%d").build();
            }
        }
        return Executors.defaultThreadFactory();
    }
    /**
     * 打印线程池的状态
     *
     * @param threadPool 线程池对象
     */
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