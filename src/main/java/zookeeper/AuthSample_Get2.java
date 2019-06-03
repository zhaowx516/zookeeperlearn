package zookeeper;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * 使用错误信息的zookeeper会话访问含权限信息的数据节点
 */
public class AuthSample_Get2 implements Watcher {
    final static String path = "/zk_book_test";
    private static String connectStr = "140.143.238.210:2181";

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper1 = new ZooKeeper(connectStr, 5000, new AuthSample_Get2());
        countDownLatch.await();
        zooKeeper1.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper1.create(path, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        ZooKeeper zooKeeper2 = new ZooKeeper(connectStr, 5000, null);
        Thread.sleep(10000);
        zooKeeper2.addAuthInfo("digest","foo:true".getBytes());
        System.out.println(zooKeeper2.getData(path,false,null));

        ZooKeeper zooKeeper3 = new ZooKeeper(connectStr, 5000, null);
        Thread.sleep(10000);
        zooKeeper3.addAuthInfo("digest","foo:false".getBytes());
        zooKeeper3.getData(path,false,null);

    }

    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            System.out.println(111);
            countDownLatch.countDown();
        }
    }
}
