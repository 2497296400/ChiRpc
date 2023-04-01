package github.javaguide.remoting.transport.netty.server;

import github.javaguide.config.CustomShutdownHook;
import github.javaguide.config.RpcServiceConfig;
import github.javaguide.factory.SingletonFactory;
import github.javaguide.provider.Impl.ZkServiceProviderImpl;
import github.javaguide.provider.ServiceProvider;
import github.javaguide.remoting.transport.netty.codec.RpcMessageDecoder;
import github.javaguide.remoting.transport.netty.codec.RpcMessageEncoder;
import github.javaguide.utis.RuntimeUtil;
import github.javaguide.utis.concurrent.threadpool.ThreadPoolFactoryUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;
@Slf4j
@Component
public class NettyRpcServer {
    public static final int PORT = 9998;

    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    @SneakyThrows
    public void start() {
        CustomShutdownHook.getCustomShutdownHook().clearAll();
        String host = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(
                RuntimeUtil.cpus() * 2,
                ThreadPoolFactoryUtil.createThreadFactory("service-handler-group", false)
        );
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //开启tcp nagle 算法
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //开启tcp心跳检测
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                                      @Override
                                      protected void initChannel(SocketChannel socketChannel) throws Exception {
                                          ChannelPipeline p = socketChannel.pipeline();
                                          p.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                                          p.addLast(new RpcMessageEncoder());
                                          p.addLast( new RpcMessageDecoder());
                                          p.addLast(serviceHandlerGroup, new NettyRpcServerHandler());
                                      }
                                  }
                    );
            ChannelFuture future = b.bind(host, PORT).sync();
            future.channel().closeFuture().sync();
        }catch (InterruptedException e) {
            log.error("occur exception when start server:", e);
        } finally {
            log.error("shutdown bossGroup and workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }
    }
}
