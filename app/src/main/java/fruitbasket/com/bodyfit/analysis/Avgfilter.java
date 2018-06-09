package fruitbasket.com.bodyfit.analysis;

/**
 * Created by Wang on 2017/10/19.
 */

public class Avgfilter {
    public static final String TAG = "DynamicTimeWarping";

   /* static {
        System.loadLibrary("jni-avgfilter");
    }*/
    public native double[] avgfilterc(double[] sensorDatas,int gap,int span);
}
