package fruitbasket.com.bodyfit;

import android.app.Application;
import android.content.Context;

import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.view.CropImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.litepal.LitePal;

/**
 * Created by lkeye on 2018/3/17.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;
    }


}
