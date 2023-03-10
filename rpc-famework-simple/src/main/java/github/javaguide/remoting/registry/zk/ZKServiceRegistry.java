package github.javaguide.remoting.registry.zk;

import github.javaguide.remoting.registry.ServiceRegistry;
import github.javaguide.remoting.registry.zk.untils.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.core.CoroutinesUtils;

import java.net.InetSocketAddress;

public class ZKServiceRegistry implements ServiceRegistry {
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient,servicePath);
    }
}
