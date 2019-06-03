package zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class GetData_API_ASync implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String connectStr = "140.143.238.210:2181";
    private static  ZooKeeper zooKeeper;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws Exception{
        zooKeeper = new ZooKeeper(connectStr, 5000, new GetData_API_ASync());
        countDownLatch.await();

        String path = "/zk-book";
        zooKeeper.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.getData(path, true, new IDataCallback(), null);

        zooKeeper.setData(path, "123".getBytes(), -1);
        Thread.sleep(Integer.MAX_VALUE);
    }
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                countDownLatch.countDown();
            } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                    zooKeeper.getData(watchedEvent.getPath(), true, new IDataCallback(), null);

            }

        }
    }

    private static class IDataCallback implements AsyncCallback.DataCallback {
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            System.out.println(rc+", "+path+", "+new String(data));
            System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());
        }
    }
}
