package github.javaguide.registry.zk;

import github.javaguide.registry.ServiceRegistry;
import github.javaguide.registry.zk.util.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

public class ZkServiceRegistryImpl implements ServiceRegistry {
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient, servicePath);
    }
}
