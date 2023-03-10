package github.javaguide.remoting.provider.impl;

import gitHub.chi.enums.RpcErrorMessageEnum;
import gitHub.chi.exception.RpcException;
import gitHub.chi.extension.ExtensionLoader;
import github.javaguide.config.RpcServerConfig;
import github.javaguide.remoting.provider.ServiceProvider;
import github.javaguide.remoting.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ZKServiceProvider implements ServiceProvider {
    private final Map<String, Object> serviceMap;

    private final Set<String> registeredService;

    private final ServiceRegistry serviceRegistry;

    public ZKServiceProvider() throws IllegalAccessException {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("zk");
    }

    @Override
    public void addService(RpcServerConfig rpcServerConfig) {
        String rpcServiceName = rpcServerConfig.getRpcServiceName();
        if (registeredService.contains(rpcServiceName)) {
            return;
        }
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, rpcServerConfig.getServer());
        log.info("Add service: {} and interfaces:{}", rpcServiceName, rpcServerConfig.getServer().getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(RpcServerConfig rpcServerConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            this.addService(rpcServerConfig);
            serviceRegistry.registerService(rpcServerConfig.getRpcServiceName(),new InetSocketAddress(host,NettyRpcServer.PORT));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
