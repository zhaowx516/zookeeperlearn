package curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * PathChildrenCache的使用，用于监听指定zookeeper数据节点的子节点变化情况
 */
public class PathChildrenCache_sample {
    private static String connectStr = "140.143.238.210:2181";
    private static String path="/zk-book";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectStr)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .build();
        client.start();
        PathChildrenCache cache = new PathChildrenCache(client, path, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
//        注册监听
        cache.getListenable().addListener((client1, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println("CHILD_ADDED,"+event.getData().getPath());
                    break;
                case CHILD_UPDATED:
                    System.out.println("CHILD_UPDATED,"+event.getData().getPath());
                    break;
                case CHILD_REMOVED:
                    System.out.println("CHILD_REMOVED,"+event.getData().getPath());
                    break;
                default:
                    break;
            }
        });
//        创建节点
        client.create().withMode(CreateMode.PERSISTENT).forPath(path);
        Thread.sleep(1000);
//        创建子节点，回调PathChildrenCacheListener，事件类型为CHILD_ADDED
        client.create().withMode(CreateMode.PERSISTENT).forPath(path + "/c1");
        Thread.sleep(1000);
//        删除子节点，回调PathChildrenCacheListener，事件类型为CHILD_REMOVED
        client.delete().forPath(path + "/c1");
        Thread.sleep(1000);
//        删除本节点，无回调
        client.delete().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
