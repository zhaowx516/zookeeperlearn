package curator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 时间戳并发问题
 */
public class Recipes_NoLock {
    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                String orderNo = sdf.format(new Date());
                System.out.println("生成的订单号是:"+orderNo);
            }).start();
        }
        countDownLatch.countDown();
    }
}
