package zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * 同步更新数据
 */
public class Exist_API_Sync implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String connectStr = "140.143.238.210:2181";
    private static  ZooKeeper zooKeeper;

    public static void main(String[] args) throws Exception{
        zooKeeper = new ZooKeeper(connectStr, 5000, new Exist_API_Sync());
        countDownLatch.await();

        String path = "/zk-book";

        zooKeeper.exists(path, true);

        zooKeeper.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        zooKeeper.setData(path, "456".getBytes(), -1);
        zooKeeper.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.delete(path + "/c1", -1);

        zooKeeper.delete(path,-1);

        Thread.sleep(Integer.MAX_VALUE);
    }
    public void process(WatchedEvent watchedEvent) {
        try {
            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                    countDownLatch.countDown();
                } else if (Event.EventType.NodeCreated == watchedEvent.getType()) {
                    System.out.println("Node(" + watchedEvent.getPath() + ")Create");
                    zooKeeper.exists(watchedEvent.getPath(), true);
                } else if (Event.EventType.NodeDeleted == watchedEvent.getType()) {
                    System.out.println("Node(" + watchedEvent.getPath() + ")Deleted ");
                    zooKeeper.exists(watchedEvent.getPath(), true);
                } else if (Event.EventType.NodeDataChanged == watchedEvent.getType()) {
                    System.out.println("Node(" + watchedEvent.getPath() + ")DataChanged");
                    zooKeeper.exists(watchedEvent.getPath(), true);
                }

            }

        } catch (Exception e) {

        }
    }

}
