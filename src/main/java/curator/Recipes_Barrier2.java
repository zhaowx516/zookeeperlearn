package curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 分布式barrier的另一种使用
 */
public class Recipes_Barrier2 {
    private static String connectStr = "140.143.238.210:2181";
    private static String path="/barrier_path";
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(()-> {
                    try {
                        CuratorFramework client = CuratorFrameworkFactory.builder()
                                .connectString(connectStr)
                                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                                .build();
                        client.start();
                        //barrier最大值为5
                        DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, path,5);
                       Thread.sleep(Math.round(Math.random()*3000));
                        System.out.println(Thread.currentThread().getName()+"号进入barrier");
                        barrier.enter();
                        System.out.println("启动。。。");
                       Thread.sleep(Math.round(Math.random()*3000));
                       barrier.leave();
                        System.out.println("退出...");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }).start();
        }

    }
}
