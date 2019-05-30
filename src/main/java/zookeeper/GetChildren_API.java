package zookeeper;

import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GetChildren_API implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String connectStr = "140.143.238.210:2181";
    private static  ZooKeeper zooKeeper;
    public static void main(String[] args) throws Exception{
        zooKeeper = new ZooKeeper(connectStr, 5000, new GetChildren_API());
        countDownLatch.await();

        String path = "/zk-book";
        zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.create(path+"/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        List<String> children = zooKeeper.getChildren(path, true);
        System.out.println(children);

        zooKeeper.create(path+"/c2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        Thread.sleep(Integer.MAX_VALUE);
    }
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                countDownLatch.countDown();
            } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                try {
                    System.out.println("reGet children :"+zooKeeper.getChildren(watchedEvent.getPath(),true));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
