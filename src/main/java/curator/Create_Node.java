package curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * 创建节点，删除节点，获取节点数据
 */
public class Create_Node {
    private static String connectStr = "140.143.238.210:2181";
    private static String path="/zk-book";

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectStr,
                5000,3000,retryPolicy);
        client.start();
        //创建节点
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path+"/c1","init".getBytes());
        System.out.println("创建节点成功，path:"+path+"/c1");
        Stat stat = new Stat();
        //获取节点数据
        System.out.println(new String(client.getData().storingStatIn(stat).forPath(path+"/c1")));
        //成功更新节点数据
        System.out.println("success set node for :"+path+"/c1"+", new version:"
         +client.setData().withVersion(stat.getVersion()).forPath(path+"/c1").getVersion());
        try {
            //版本不对更新失败
            client.setData().withVersion(stat.getVersion()).forPath(path+"/c1");
        } catch (Exception e) {
            System.out.println("fail to set node :"+e);
        }

        //删除节点，并递归删除子节点
        client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);
        System.out.println("成功删除根节点");
    }
}
