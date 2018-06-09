package fruitbasket.com.bodyfit.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import fruitbasket.com.bodyfit.R;

public class TitleBar extends LinearLayout{
    private LinearLayout ll_left;
    private ImageView iv_left;
    private TextView tx_left;
    private LinearLayout ll_right;
    private ImageView iv_right;
    private TextView tx_right;
    private TextView title;

    private String TAG="TitleBar";
    /**
     * 构造方法：用于获取对象
     */
    public TitleBar(Context context) {
        this(context,null);
    }
    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    public void initView( Context context,AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_title_bar,this);
        ll_left = (LinearLayout) findViewById(R.id.ll_title_bar_left);
        iv_left = (ImageView) findViewById(R.id.ic_title_bar_left);
        tx_left = (TextView) findViewById(R.id.tx_title_bar_left);

        ll_right = (LinearLayout) findViewById(R.id.ll_title_bar_right);
        iv_right = (ImageView) findViewById(R.id.ic_title_bar_right);
        tx_right = (TextView) findViewById(R.id.tx_title_bar_right);
        title = (TextView) findViewById(R.id.tx_title);
        Log.i(TAG,"title="+title);
    }

    /**
     * 用于设置标题栏文字
     *
     * @param titleText 传入要设置的标题
     * @return
     */
    public TitleBar setTitleText(String titleText) {
        title.setText(titleText);
        return this;
    }

    /**
     * 设置标题栏文字颜色
     *
     * @return
     */
    public TitleBar setTitleTextColor() {
        title.setTextColor(Color.BLACK);
        return this;
    }

    /**
     * 设置标题栏右边的文字
     *
     * @return
     */
    public TitleBar setTitleRight(String rightTitle) {
        if (rightTitle!=null) {
            tx_right.setVisibility(View.VISIBLE);
            iv_right.setVisibility(View.GONE);
            tx_right.setTextColor(Color.BLACK);
            tx_right.setText(rightTitle);
        }
        return this;
    }
    /**
     * 用于设置标题栏左边要显示的图片
     *
     * @param resId 标题栏左边的图标的id，一般为返回图标
     * @return
     */
    public TitleBar setLeftIco(int resId) {
        iv_left.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        iv_left.setImageResource(R.drawable.back_left);
        return this;
    }
    /**
     * 用于设置标题栏右边要显示的图片
     *
     * @param resId 标题栏右边的图标id
     * @return
     */
    public TitleBar setRightIco(int resId) {

        iv_right.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        if(resId>0)
            iv_right.setImageResource(resId);
        return this;
    }

    /**
     * 用户设置 标题栏右侧的图标的背景drawable
     *
     * @param resId drawable的id
     * @return
     */
    public TitleBar setRightIconBgDr(int resId) {
        iv_right.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        iv_right.setImageResource(R.drawable.ic_camera);
        return this;
    }
    /**
     * 用于设置标题栏左边图片的单击事件
     *
     * @param listener 传入的事件对象
     * @return
     */
    public TitleBar setLeftIcoListening(View.OnClickListener listener) {
        if (iv_left.getVisibility() == View.VISIBLE) {
            iv_left.setOnClickListener(listener);
        }
        return this;
    }
    /**
     * 用于设置标题栏右边图片的单击事件
     *
     * @param listener 传入的事件对象
     * @return
     */
    public TitleBar setRightIcoListening(View.OnClickListener listener) {
        if (iv_right.getVisibility() == View.VISIBLE) {
            iv_right.setOnClickListener(listener);
        }
        return this;
    }

    public TitleBar setRightTextListening(View.OnClickListener listener) {
        if (tx_right.getVisibility() == View.VISIBLE) {
            tx_right.setOnClickListener(listener);
        }
        return this;
    }


}
