package zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 同步创建节点
 */
public class Create_API_Sync_Usage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String connectStr = "140.143.238.210:2181";
    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(connectStr, 5000, new Create_API_Sync_Usage());
        countDownLatch.await();
        //临时节点
        String path1 = zooKeeper.create("/zk_test", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("success create znode:"+path1);
        //临时顺序节点
        String path2 = zooKeeper.create("/zk_test", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("success create znode : "+path2);
    }
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }
}
