package fruitbasket.com.bodyfit;

/**
 * Created by Franklin on 3/23/2018.
 */

public class TimeCalculate {

    private static long start;
    private static long end;
    private static boolean isBegin = false;

    public static void startTiming() {
        if (!isBegin) {
            isBegin = true;
            start = System.currentTimeMillis();
        } else {
            LogUtils.e("already begin");
        }
    }

    public static void stopTiming(String msg) {
        if (isBegin) {
            isBegin = false;
            end = System.currentTimeMillis();
            LogUtils.d(msg + " time cal :" + (end - start));
        } else {
            LogUtils.e("already end");
        }
    }
}
