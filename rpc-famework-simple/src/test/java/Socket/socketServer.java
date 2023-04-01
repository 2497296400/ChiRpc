package Socket;

import TestMethod.DoGetImpl;
import TestMethod.Doget;
import TestMethod.Hellow;
import TestMethod.HellowImpl;
import github.javaguide.config.RpcServiceConfig;
import github.javaguide.remoting.transport.socket.SocketRpcServer;
import org.junit.Test;

public class socketServer {
    @Test
    public void SocketTestServer() {
        SocketRpcServer rpcServer = new SocketRpcServer();
        Hellow hellow = new HellowImpl();
        Doget doget = new DoGetImpl();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(hellow);
        rpcServer.registerService(rpcServiceConfig);
        rpcServiceConfig.setService(doget);
        rpcServer.registerService(rpcServiceConfig);
        rpcServer.start();
    }
}
