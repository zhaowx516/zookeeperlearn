package zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * 异步更新数据
 */
public class SetData_API_ASync implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String connectStr = "140.143.238.210:2181";
    private static  ZooKeeper zooKeeper;

    public static void main(String[] args) throws Exception{
        zooKeeper = new ZooKeeper(connectStr, 5000, new SetData_API_ASync());
        countDownLatch.await();

        String path = "/zk-book";
        zooKeeper.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.setData(path, "456".getBytes(),-1, new IStatCallback(), null);

        Thread.sleep(Integer.MAX_VALUE);
    }
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                countDownLatch.countDown();
            }

        }
    }


    private static class IStatCallback implements AsyncCallback.StatCallback {
        public void processResult(int rc, String s, Object o, Stat stat) {
            if (rc == 0) {
                System.out.println("success");
            }
        }
    }
}
