package github.javaguide.remoting.registry;

import gitHub.chi.extension.SPI;

import java.net.InetSocketAddress;

@SPI
public interface ServiceDIscovery {
    /**
     * register service
     *
     * @param rpcServiceName    rpc service name
     * @param inetSocketAddress service address
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress); 
}
