package github.javaguide.registry;


import github.javaguide.extension.SPI;
import github.javaguide.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

//用于五福发现
@SPI
public interface ServiceDiscovery {
    InetSocketAddress lookupService(RpcRequest rpcRequest);
    
}
