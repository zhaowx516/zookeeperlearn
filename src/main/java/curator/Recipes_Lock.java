package curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 分布式锁
 */
public class Recipes_Lock {
    private static String connectStr = "140.143.238.210:2181";
    private static String path="/lock_path";

    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectStr)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .build();
        client.start();
        final InterProcessMutex lock = new InterProcessMutex(client, path);
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                    lock.acquire();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                String orderNo = sdf.format(new Date());
                System.out.println("订单号："+orderNo);
                try {
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        countDownLatch.countDown();
    }
}
