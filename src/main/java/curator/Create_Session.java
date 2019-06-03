package curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 使用curator创建一个zookeeper客户端
 */
public class Create_Session {
    private static String connectStr = "140.143.238.210:2181";

    public static void main(String[] args) throws InterruptedException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //zookeeper服务器列表，例：192.168.1.1:2181,192.168.1.2:2181
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectStr,
                5000,3000,retryPolicy);
        client.start();

        //使用fluent风格的api接口来创建zookeeper客户端
        CuratorFramework client2 = CuratorFrameworkFactory.builder()
                .connectString(connectStr)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client2.start();

        Thread.sleep(Integer.MAX_VALUE);
    }
}
