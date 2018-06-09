package fruitbasket.com.bodyfit.ExerciseSociety;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public class UILImageLoader implements com.lqr.imagepicker.loader.ImageLoader{
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        ImageSize size = new ImageSize(width, height);
        ImageLoader.getInstance().displayImage("file://" + path, imageView, size);
    }

    @Override
    public void clearMemoryCache() {
    }
}
