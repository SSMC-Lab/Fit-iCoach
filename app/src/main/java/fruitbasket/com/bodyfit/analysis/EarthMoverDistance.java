package fruitbasket.com.bodyfit.analysis;
import android.util.Log;

/**
 * Created by Franklin on 4/25/2018.
 */

public class EarthMoverDistance {

    public static final String TAG = "EarthMoverDistance";

    private native static float getEarthMoverDistance(double []s1,double []s2,int n1,int n2);

    public static float EarthMoversDistance(double []s1,double []s2,int n1,int n2){
        return getEarthMoverDistance(s1,s2,n1,n2);
    }
}
