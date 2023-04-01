package github.javaguide.registry.zk.util;

import github.javaguide.enums.RpcConfigEnum;
import github.javaguide.utis.PropertiesFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

//zk工具类 单机zk
@Slf4j
public class CuratorUtils {
    //基础随眠时间
    private static final int BASE_SLEEP_TIME = 1000;
    //最大重连次数
    private static final int MAX_RETRIES = 3;
    //根节点名称
    public static final String ZK_REGISTER_ROOT_PATH = "/my-rpc";
    //地址信息
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    //注册的路径
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    //zk客户端
    private static CuratorFramework zkClient;
    //默认的zk地址和端口
    private static final String DEFAULT_ZOOKEEPER_ADDRESS = "192.168.101.5:2181";

    private CuratorUtils() {
    }

    //创建持久化父节点
    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("The node already exists. The node is:[{}]", path);
            } else {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("The node was created successfully. The node is:[{}]", path);
            }
            REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            log.error("create persistent node for path [{}] fail", path);
        }
    }

    //获得所以子节点的node
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }
        List<String> result = null;
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        try {
            result = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, result);
            reginsterWatcher(rpcServiceName, zkClient);
        } catch (Exception e) {
            log.error("get children nodes for path [{}] fail", servicePath);
        }
        return result;
    }

    private static void reginsterWatcher(String rpcServiceName, CuratorFramework zkClient) throws Exception {
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                List<String> serviceAddress = curatorFramework.getChildren().forPath(servicePath);
                SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceAddress);
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }

    public static void clearRegistry(CuratorFramework zkClient, InetSocketAddress inetSocketAddress) {
        REGISTERED_PATH_SET.stream().parallel().forEach(itme -> {
            try {
                if (itme.endsWith(inetSocketAddress.toString())) {
                    zkClient.delete().forPath(itme);
                }
            } catch (Exception e) {
                log.error("clear registry for path [{}] fail", itme);
            }
        });
        log.info("All registered services on the server are cleared:[{}]", REGISTERED_PATH_SET.toString());
    }

    public static CuratorFramework getZkClient() {
        Properties properties = PropertiesFileUtil.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
        String zookeeperAddress = properties != null &&
                properties.getProperty(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue()) != null ?
                properties.getProperty(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue()) : DEFAULT_ZOOKEEPER_ADDRESS;
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        //设置重启次数和重启时间
        RetryPolicy retryPolicy  = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                .retryPolicy(retryPolicy)
                .connectString(zookeeperAddress)
                .build();
        zkClient.start();
        try {
            if(!zkClient.blockUntilConnected(30, TimeUnit.SECONDS)){
                throw new RuntimeException("Time out waiting to connect to ZK!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zkClient;
    }
}
