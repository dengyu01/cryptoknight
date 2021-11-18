package com.hscovo.cryptoknight.util;

public class TimeUtil {
    public static void wait(Long startTime, String msg, Integer waitBeforeStartTime, Integer interval)
            throws InterruptedException {
        int count = 0;
        while (true) {
            long currentTimeMillis = System.currentTimeMillis();
            if (count % 30 == 1) {
                System.out.println(msg);
            }
            if (startTime < currentTimeMillis + waitBeforeStartTime * 1000) {
                break;
            }
            Thread.sleep(interval);
            count ++;
        }
    }
}
