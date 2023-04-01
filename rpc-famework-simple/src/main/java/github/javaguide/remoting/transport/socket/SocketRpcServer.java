package github.javaguide.remoting.transport.socket;

import github.javaguide.config.CustomShutdownHook;
import github.javaguide.config.RpcServiceConfig;
import github.javaguide.extension.ExtensionLoader;
import github.javaguide.factory.SingletonFactory;
import github.javaguide.provider.Impl.ZkServiceProviderImpl;
import github.javaguide.provider.ServiceProvider;
import github.javaguide.utis.concurrent.threadpool.ThreadPoolFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
@Slf4j
public class SocketRpcServer {
    private final ExecutorService threadPool;
    private final ServiceProvider serviceProvider;

    public SocketRpcServer() {
        threadPool = ThreadPoolFactoryUtil.createCustomThreadPoolIfAbsent("sock-rpc-pool");
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()){
            String host = InetAddress.getLocalHost().getHostAddress();
            serverSocket.bind(new InetSocketAddress(host,9998));
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            Socket socket;
            while ((socket =serverSocket.accept())!=null) {
                log.info("client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
