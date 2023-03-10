package github.javaguide.config;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@Slf4j
public class CustomShutdownHook {
    private static  final CustomShutdownHook  CUSTOM_SHUTDOWN_HOOK =  new CustomShutdownHook();
    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }
    /*
    public  void  clearALl(){
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(),NettyRpcServer.)
        }));
    }*/
    
}
