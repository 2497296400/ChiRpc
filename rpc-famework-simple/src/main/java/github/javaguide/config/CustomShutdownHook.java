package github.javaguide.config;

import github.javaguide.registry.zk.util.CuratorUtils;
import github.javaguide.utis.concurrent.threadpool.ThreadPoolFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Slf4j
public class CustomShutdownHook {
    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }

    public void clearAll() {
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                CuratorUtils.clearRegistry(CuratorUtils.getZkClient(), new InetSocketAddress(InetAddress.getLocalHost().getHostName(), 9998));
            } catch (UnknownHostException e) {
               
            }
            ThreadPoolFactoryUtil.shutDownAllThreadPool();
        }));
    }
}
