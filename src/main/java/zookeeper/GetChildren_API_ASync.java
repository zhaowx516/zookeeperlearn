package zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GetChildren_API_ASync implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String connectStr = "140.143.238.210:2181";
    private static  ZooKeeper zooKeeper=null;
    public static void main(String[] args) throws Exception{
        zooKeeper = new ZooKeeper(connectStr, 5000, new GetChildren_API_ASync());
        countDownLatch.await();

        String path = "/zk-book";
        zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.create(path+"/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        zooKeeper.getChildren(path, true,new IChildrenCallBack(),null);

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


    private static class IChildrenCallBack implements AsyncCallback.Children2Callback {
        public void processResult(int rc, String path, Object ctx, List<String> list, Stat stat) {
            System.out.println("get Children znode result:[ response code:"+rc
                    +", param path:"+path+",ctx :"+ctx+",children list:"+list
                    +",stat :"+stat);
        }
    }
}
