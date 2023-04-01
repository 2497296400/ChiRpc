package github.javaguide.provider.Impl;

import github.javaguide.config.RpcServiceConfig;
import github.javaguide.enums.RpcErrorMessageEnum;
import github.javaguide.exception.RpcException;
import github.javaguide.extension.ExtensionLoader;
import github.javaguide.provider.ServiceProvider;
import github.javaguide.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ZkServiceProviderImpl  implements ServiceProvider {
    //key 是 serviceName + version + group 
    //valuse是 service实体
    private  final Map<String,Object> serviceMap;
    private  final Set<String> registeredService;
    
    private final ServiceRegistry serviceRegistry;

    public ZkServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("zk");
        
    }

    @Override
    public void addService(RpcServiceConfig serviceConfig) {
        String rpcServiceName = serviceConfig.getRpcServiceName();
        if(registeredService.contains(rpcServiceName)){
            return;
        }
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, serviceConfig.getService());
        log.info("Add service: {} and interfaces:{}", rpcServiceName, serviceConfig.getService().getClass().getInterfaces());
        
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if(service == null){
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            this.addService(rpcServiceConfig);
            serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), 
                    new InetSocketAddress(hostAddress,9998));
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress", e);
        }
    }
}
