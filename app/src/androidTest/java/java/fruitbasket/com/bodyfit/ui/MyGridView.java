package fruitbasket.com.bodyfit.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridView;

/**
 * Created by Administrator on 2017/3/1.
 */
public class MyGridView extends GridView {
    public static final String TAG="MyGridView";
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context con,AttributeSet attrs){
        super(con, attrs);
    }

    public MyGridView(Context con,AttributeSet attrs,int defStyle){
        super(con,attrs,defStyle);
    }

    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        Log.i(TAG,"onMeasure");
        int expandSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
