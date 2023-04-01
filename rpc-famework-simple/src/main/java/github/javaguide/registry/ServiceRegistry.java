package github.javaguide.registry;

import github.javaguide.extension.SPI;

import java.net.InetSocketAddress;
@SPI
public interface ServiceRegistry {
    //用于注册服务 
  
    // 方法名称 + 地址为一个服务
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
