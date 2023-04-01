package Socket;

import TestMethod.Doget;
import TestMethod.Hellow;
import github.javaguide.config.RpcServiceConfig;
import github.javaguide.proxy.RpcClientProxy;
import github.javaguide.remoting.dto.RpcRequest;
import github.javaguide.remoting.transport.RpcRequestTransport;
import github.javaguide.remoting.transport.socket.SocketRpcClient;
import org.junit.Test;

public class SokcetClinet {
    @Test
    public void TestClinet() {
        
        RpcRequestTransport rpcRequestTransport = new SocketRpcClient();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport, rpcServiceConfig);
        Hellow hellow = rpcClientProxy.getProxy(Hellow.class);
        Doget doget = rpcClientProxy.getProxy(Doget.class);
        
        System.out.println(hellow.hellow("辉").length());
        System.out.println(doget.get(10));
    }
}
