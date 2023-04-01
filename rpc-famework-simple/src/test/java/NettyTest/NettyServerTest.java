package NettyTest;

import TestMethod.DoGetImpl;
import TestMethod.Doget;
import TestMethod.Hellow;
import TestMethod.HellowImpl;
import github.javaguide.config.RpcServiceConfig;
import github.javaguide.remoting.transport.netty.server.NettyRpcServer;
import org.junit.Test;

public class NettyServerTest {
    @Test
    public void nettyServer() {
        Hellow hellow = new HellowImpl();
        Doget doget = new DoGetImpl();
        NettyRpcServer nettyRpcServer = new NettyRpcServer();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(hellow);
        nettyRpcServer.registerService(rpcServiceConfig);
        rpcServiceConfig.setService(doget);
        nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
        
    }
}
