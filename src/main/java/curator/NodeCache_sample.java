package curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * NodeCache使用，用于监听指定zookeeper节点本身变化情况
 * 还可以用于监听指定节点是否存在，如果原来节点不存在，那么会在节点创建后触发NodeCacheListener
 */
public class NodeCache_sample {
    private static String connectStr = "140.143.238.210:2181";
    private static String path="/zk-book/nodecache";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectStr)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .build();
        client.start();
//        创建节点
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,"init".getBytes());
//        创建NodeCache实例
        final NodeCache nodeCache = new NodeCache(client, path, false);
//        传入参数为true，会在启动时会立刻到zookeeper节点上读取数据内容，保存到cache中
        nodeCache.start(true);
        nodeCache.getListenable().addListener(() -> System.out.println("Node data update, new data: "+new String(nodeCache.getCurrentData().getData())));
        client.setData().forPath(path, "456".getBytes());
        Thread.sleep(1000);
        client.delete().deletingChildrenIfNeeded().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);

    }
}
