package curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * Master选举
 */
public class MasterSelect_sample {
    private static String connectStr = "140.143.238.210:2181";
    private static String path="/curator-master";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectStr)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .build();
        client.start();
//       在成功获取master权利时回调监听
        LeaderSelector selector=new LeaderSelector(client, path, new LeaderSelectorListenerAdapter() {
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("成为master角色");
                Thread.sleep(3000);
                System.out.println("完成master操作，释放master权利");
            }
        });
        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
