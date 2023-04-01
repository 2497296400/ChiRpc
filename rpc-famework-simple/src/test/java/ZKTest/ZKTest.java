package ZKTest;

import github.javaguide.config.RpcServiceConfig;
import github.javaguide.factory.SingletonFactory;
import github.javaguide.provider.Impl.ZkServiceProviderImpl;
import github.javaguide.provider.ServiceProvider;
import github.javaguide.registry.ServiceDiscovery;
import github.javaguide.registry.ServiceRegistry;
import github.javaguide.registry.zk.ZkServiceDiscoveryImpl;
import github.javaguide.registry.zk.ZkServiceRegistryImpl;
import github.javaguide.registry.zk.util.CuratorUtils;
import github.javaguide.remoting.dto.RpcRequest;
import github.javaguide.remoting.handler.RpcRequestHandler;
import org.apache.curator.framework.CuratorFramework;
import org.junit.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class ZKTest {
    @Test
    public void reginstryAndDisCovery() throws Exception {
        ServiceDiscovery serviceDiscovery = new ZkServiceDiscoveryImpl();
        ServiceRegistry serviceRegistry = new ZkServiceRegistryImpl();
        DemoRpcService service = new DemoRpcServiceImpl();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .version("version2")
                .group("test2")
                .service(service)
                .build();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 9333);
        serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), inetSocketAddress);
        ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
        serviceProvider.addService(rpcServiceConfig);
        
        
        ///my-rpc/ZKTest.DemoRpcServiceversion2test2Chi/172.26.65.70:9333
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(rpcServiceConfig.getServiceName())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceConfig.getGroup())
                .version(rpcServiceConfig.getVersion())
                .parameters(service.getClass().getTypeParameters())
                .methodName(rpcServiceConfig.getService().getClass().getMethods()[0].getName())
                .build();
        RpcRequestHandler rpcRequestHandler = new RpcRequestHandler();
        
        Object handle = rpcRequestHandler.handle(rpcRequest);
        System.out.println(handle);
    }
    @Test
    public  void  curTest() throws UnknownHostException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        System.out.println(hostAddress);
        InetSocketAddress address = new InetSocketAddress(hostAddress, 9998);
        System.out.println(address.toString());
    }
}
