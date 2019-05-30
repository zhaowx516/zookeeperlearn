package zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 使用普通的构造方法
 */
public class ZookeeperTest implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static String connectStr = "140.143.238.210:2181";
    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper(connectStr, 5000, new ZookeeperTest());
        System.out.println(zooKeeper.getState());
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("zookeeper session established");
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println("receive watched event:"+watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }
}
