package curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用curator的异步接口
 */
public class Create_Node_Async {
    private static String connectStr = "140.143.238.210:2181";

    private final static String path = "/zk-book";

    private static CountDownLatch countDownLatch = new CountDownLatch(2);

    private static ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectStr,
                5000,3000,retryPolicy);
        client.start();
        System.out.println("main thread: " + Thread.currentThread().getName());
        //传入自定义executor
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(
                new BackgroundCallback() {
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("event[code: " + curatorEvent.getResultCode() + ", type:" + curatorEvent.getType() + "]");
                        System.out.println("Thread of procesResult: " + Thread.currentThread().getName());
                        countDownLatch.countDown();
                    }
                }, executor
        ).forPath(path, "init".getBytes());

        //不使用自定义线程池
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(
                new BackgroundCallback() {
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("event[code: " + curatorEvent.getResultCode() + ", type:" + curatorEvent.getType() + "]");
                        System.out.println("Thread of procesResult: " + Thread.currentThread().getName());
                        countDownLatch.countDown();
                    }
                }
        ).forPath(path, "init".getBytes());

        countDownLatch.await();
        executor.shutdown();
    }
}
