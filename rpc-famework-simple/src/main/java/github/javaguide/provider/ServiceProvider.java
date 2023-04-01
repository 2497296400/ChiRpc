package github.javaguide.provider;

import github.javaguide.config.RpcServiceConfig;
import github.javaguide.extension.SPI;

@SPI
public interface ServiceProvider {

    //添加服务
    void addService(RpcServiceConfig serviceConfig);

    //获取服务
    Object getService(String rpcServiceName);
    
    //推送服务
    void  publishService(RpcServiceConfig rpcServiceConfig);
    
}
