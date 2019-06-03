package zookeeper;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * 删除节点接口的权限控制
 */
public class AuthSample_Delete implements Watcher {
    final static String path = "/zk_book_test";
    final static String path2 = "/zk_book_test/child";
    private static String connectStr = "140.143.238.210:2181";

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper1 = new ZooKeeper(connectStr, 5000, new AuthSample_Delete());
        countDownLatch.await();
        zooKeeper1.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper1.create(path, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        zooKeeper1.create(path2, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        try {
            ZooKeeper zooKeeper2 = new ZooKeeper(connectStr, 5000, null);
            Thread.sleep(10000);
            zooKeeper2.delete(path2,-1);
        } catch (Exception e) {
            System.out.println("删除节点失败："+e);
        }

        ZooKeeper zooKeeper3 = new ZooKeeper(connectStr, 5000, null);
        Thread.sleep(10000);
        zooKeeper3.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper3.delete(path2,-1);
        System.out.println("成功删除节点："+path2);

        ZooKeeper zooKeeper4 = new ZooKeeper(connectStr, 5000, null);
        Thread.sleep(10000);
        zooKeeper4.delete(path,-1);
        System.out.println("成功删除节点："+path);
    }

    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            System.out.println(111);
            countDownLatch.countDown();
        }
    }
}
