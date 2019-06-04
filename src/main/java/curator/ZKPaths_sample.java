package curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;

/**
 * 工具类ZKPaths能构建ZNode路径、递归创建和删除节点等
 */
public class ZKPaths_sample {
    private static final String path = "/curator_zkpaths";
    private static String connectStr = "140.143.238.210:2181";
    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectStr)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .build();
        client.start();
        ZooKeeper zooKeeper = client.getZookeeperClient().getZooKeeper();
        System.out.println(ZKPaths.fixForNamespace("sub",path));
        System.out.println(ZKPaths.makePath(path,"sub"));
        System.out.println(ZKPaths.getNodeFromPath(path+"/sub1"));
        ZKPaths.PathAndNode pn = ZKPaths.getPathAndNode(path + "/sub1");
        System.out.println(pn.getNode());
        System.out.println(pn.getPath());

        String dir1 = path + "/child1";
        String dir2 = path + "/child2";
        Thread.sleep(10000);
        ZKPaths.mkdirs(zooKeeper,dir1);
        ZKPaths.mkdirs(zooKeeper,dir2);
        System.out.println(ZKPaths.getSortedChildren(zooKeeper,path));

        ZKPaths.deleteChildren(client.getZookeeperClient().getZooKeeper(),path,true);

    }
}
