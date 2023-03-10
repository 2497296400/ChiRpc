package github.javaguide.remoting.provider;

import github.javaguide.config.RpcServerConfig;

public interface ServiceProvider {
    void  addService(RpcServerConfig rpcServerConfig);
    
    Object getService(String rpcServiceName);
    
    void  publishService(RpcServerConfig rpcServerConfig);
    
}
