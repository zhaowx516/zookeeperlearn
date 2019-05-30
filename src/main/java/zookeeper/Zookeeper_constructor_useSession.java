package zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Zookeeper_constructor_useSession implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String connectStr = "140.143.238.210:2181";
    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper(connectStr, 5000, new Zookeeper_constructor_useSession());
        countDownLatch.await();
        long sessionId = zooKeeper.getSessionId();
        byte[] sessionPasswd = zooKeeper.getSessionPasswd();

        //使用非法的会话
        zooKeeper = new ZooKeeper(connectStr, 5000,
                new Zookeeper_constructor_useSession(), 1l,"test".getBytes());

        //使用已有的会话连接
        zooKeeper = new ZooKeeper(connectStr, 5000, new Zookeeper_constructor_useSession(), sessionId, sessionPasswd);
        Thread.sleep(5000000);
    }
    public void process(WatchedEvent watchedEvent) {
        System.out.println("receive event:"+watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }
}
