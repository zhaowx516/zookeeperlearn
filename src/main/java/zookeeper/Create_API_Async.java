package zookeeper;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

public class Create_API_Async implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String connectStr = "140.143.238.210:2181";
    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper(connectStr, 5000, new Create_API_Async());
        countDownLatch.await();

        zooKeeper.create("/zk-test-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                new IStringCallback(), "I am context");
        zooKeeper.create("/zk-test-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                new IStringCallback(), "I am context");
        zooKeeper.create("/zk-test-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
                new IStringCallback(), "I am context");

        Thread.sleep(Integer.MAX_VALUE);
    }
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }

    /**
     * 响应码rc
     * 0：接口调用成功
     * -4：客户端与服务端连接断开
     * -110：指定节点已存在
     * -112：会话已过期
     */
    private static class IStringCallback implements AsyncCallback.StringCallback {
        public void processResult(int rc, String path, Object ctx, String name) {
            System.out.println("create path result: ["+rc+" ,"+path+", "+ctx+", real path name:"+name);
        }
    }
}
