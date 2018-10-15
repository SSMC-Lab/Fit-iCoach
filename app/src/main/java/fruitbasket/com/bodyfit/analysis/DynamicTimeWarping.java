package fruitbasket.com.bodyfit.analysis;

/**
 * Created by Administrator on 2016/11/15.
 */
public class DynamicTimeWarping {
    public static final String TAG = "DynamicTimeWarping";

    static {
        System.loadLibrary("jni-dtw");
    }

    public native double getDtwValue(double[] t, int num, double[] r);

    public native double getfastDtwValue(double[] t, int num, double[] r);
}
