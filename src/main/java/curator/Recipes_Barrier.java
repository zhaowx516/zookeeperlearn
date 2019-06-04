package curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.CountDownLatch;

/**
 * 分布式barrier
 */
public class Recipes_Barrier {
    private static String connectStr = "140.143.238.210:2181";
    private static String path="/barrier_path";
    private static DistributedBarrier barrier;
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    CuratorFramework client = CuratorFrameworkFactory.builder()
                            .connectString(connectStr)
                            .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                            .build();
                    client.start();
                    barrier = new DistributedBarrier(client, path);
                    System.out.println(Thread.currentThread().getName()+"号barrier设置");
                    barrier.setBarrier();
                    barrier.waitOnBarrier();
                    System.out.println("启动。。。");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();
            Thread.sleep(60*1000);
            barrier.removeBarrier();
        }

    }
}
