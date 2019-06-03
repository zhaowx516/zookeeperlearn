package zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * 同步更新数据
 */
public class SetData_API_Sync implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String connectStr = "140.143.238.210:2181";
    private static  ZooKeeper zooKeeper;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws Exception{
        zooKeeper = new ZooKeeper(connectStr, 5000, new SetData_API_Sync());
        countDownLatch.await();

        String path = "/zk-book";
        zooKeeper.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.getData(path, true, null);

        Stat stat1 = zooKeeper.setData(path, "456".getBytes(), -1);
        System.out.println(stat1.getCzxid()+", "+stat1.getMzxid()+", "+stat1.getVersion());

        Stat stat2 = zooKeeper.setData(path, "456".getBytes(), stat1.getVersion());
        System.out.println(stat2.getCzxid()+", "+stat2.getMzxid()+", "+stat2.getVersion());

        try {
            zooKeeper.setData(path, "456".getBytes(), stat1.getVersion());
        } catch (Exception e) {
            System.out.println(e);
        }
        Thread.sleep(Integer.MAX_VALUE);
    }
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                countDownLatch.countDown();
            }

        }
    }

}
