package NettySpringTest;

import github.javaguide.annotation.RpcScan;
import github.javaguide.remoting.transport.netty.server.NettyRpcServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@RpcScan(basePackage = {"github.javaguide","NettySpringTest"})
public class SpringServer {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringServer.class);
      NettyRpcServer nettyRpcServer = (NettyRpcServer) annotationConfigApplicationContext.getBean("nettyRpcServer");
      nettyRpcServer.start();
      
    }
}
