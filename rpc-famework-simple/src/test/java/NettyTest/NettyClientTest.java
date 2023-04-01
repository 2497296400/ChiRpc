package NettyTest;

import TestMethod.Doget;
import TestMethod.Hellow;
import github.javaguide.config.RpcServiceConfig;
import github.javaguide.proxy.RpcClientProxy;
import github.javaguide.remoting.transport.RpcRequestTransport;
import github.javaguide.remoting.transport.netty.client.NettyRpcClient;
import org.junit.Test;

public class NettyClientTest {
    @Test
    public void nettyClinet() throws InterruptedException {
        RpcRequestTransport rpcClient = new NettyRpcClient();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        RpcClientProxy proxy = new RpcClientProxy(rpcClient, rpcServiceConfig);
     //   Hellow hellow = proxy.getProxy(Hellow.class);
        Doget doget = proxy.getProxy(Doget.class);
        while (true) {
            System.out.println(doget.get(10));
            Thread.sleep(10000);
        }
    }
}
