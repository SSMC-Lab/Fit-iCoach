package fruitbasket.com.bodyfit.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.R;
import fruitbasket.com.bodyfit.utils.SPUtils;

public class CompletenessActivity extends AppCompatActivity {
    private CircleProgressView[] circleBar = new CircleProgressView[15];
    private int id[] = new int[]{
            R.id.circle_1, R.id.circle_2, R.id.circle_3, R.id.circle_4,
            R.id.circle_5, R.id.circle_6, R.id.circle_7, R.id.circle_8, R.id.circle_9,
            R.id.circle_10, R.id.circle_11, R.id.circle_12, R.id.circle_13,
            R.id.circle_14, R.id.circle_15};
    private int[] num_of_action = new int[15];
    public static final String TAG = "CompletenessActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoyt_completeness);
        getArray();
        initViews();
    }

    private void getArray() {
        num_of_action[0] = (int) SPUtils.get(Conditions.exercise_1, 0);
        num_of_action[1] = (int) SPUtils.get(Conditions.exercise_2, 0);
        num_of_action[2] = (int) SPUtils.get(Conditions.exercise_3, 0);
        num_of_action[3] = (int) SPUtils.get(Conditions.exercise_4, 0);
        num_of_action[4] = (int) SPUtils.get(Conditions.exercise_5, 0);
        num_of_action[5] = (int) SPUtils.get(Conditions.exercise_6, 0);
        num_of_action[6] = (int) SPUtils.get(Conditions.exercise_7, 0);
        num_of_action[7] = (int) SPUtils.get(Conditions.exercise_8, 0);
        num_of_action[8] = (int) SPUtils.get(Conditions.exercise_9, 0);
        num_of_action[9] = (int) SPUtils.get(Conditions.exercise_10, 0);
        num_of_action[10] = (int) SPUtils.get(Conditions.exercise_11, 0);
        num_of_action[11] = (int) SPUtils.get(Conditions.exercise_12, 0);
        num_of_action[12] = (int) SPUtils.get(Conditions.exercise_13, 0);
        num_of_action[13] = (int) SPUtils.get(Conditions.exercise_14, 0);
        num_of_action[14] = (int) SPUtils.get(Conditions.exercise_15, 0);

    }

    private void initViews() {
        int progress = 0;
        for (int i = 0; i < 15; i++) {
            Log.i(TAG, "initViews: " + (num_of_action[i] * 1.0) / 30);
            circleBar[i] = (CircleProgressView) findViewById(id[i]);
            circleBar[i].setTitle("A" + (i + 1));
            if (num_of_action[i] >= 30)
                progress = 100;
            else
                progress = (int) ((num_of_action[i] * 1.0 / 30) * 100);
            circleBar[i].setProgress(progress);
        }
    }
}

class CircleProgressView extends View {

    private static final String TAG = "CircleProgressBar";

    private String title;

    private int mMaxProgress = 100;

    private int mProgress = 30;

    private final int mCircleLineStrokeWidth = 13;

    private final int mTxtStrokeWidth = 2;

    // 画圆所在的距形区域
    private final RectF mRectF;

    private final Paint mPaint;

    private final Context mContext;

    private String mTxtHint1;

    private String mTxtHint2;

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mRectF = new RectF();
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        // 设置画笔相关属性
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.rgb(0xe9, 0xe9, 0xe9));
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(mCircleLineStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        // 位置
        mRectF.left = mCircleLineStrokeWidth / 2; // 左上角x
        mRectF.top = mCircleLineStrokeWidth / 2; // 左上角y
        mRectF.right = width - mCircleLineStrokeWidth / 2; // 左下角x
        mRectF.bottom = height - mCircleLineStrokeWidth / 2; // 右下角y

        // 绘制圆圈，进度条背景
        canvas.drawArc(mRectF, -90, 360, false, mPaint);
        //mPaint.setColor(Color.rgb(0xf8, 0x60, 0x30));
        mPaint.setColor(Color.parseColor("#5CACEE"));
        canvas.drawArc(mRectF, -90, ((float) mProgress / mMaxProgress) * 360, false, mPaint);

        // 绘制进度文案显示
        mPaint.setStrokeWidth(mTxtStrokeWidth);
        String text = mProgress + "%";
        int textHeight = height / 4;
        mPaint.setTextSize(textHeight);
        int textWidth = (int) mPaint.measureText(text, 0, text.length());
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, width / 2 - textWidth / 2, height / 2 + textHeight / 2, mPaint);

        //绘制文本
        textHeight /= 2;
        textWidth /= 2;
        canvas.drawText(title, width / 3 - textHeight / 6, height / 4 + textHeight / 2, mPaint);

        if (!TextUtils.isEmpty(mTxtHint1)) {
            mPaint.setStrokeWidth(mTxtStrokeWidth);
            text = mTxtHint1;
            textHeight = height / 8;
            mPaint.setTextSize(textHeight);
            mPaint.setColor(Color.rgb(0x99, 0x99, 0x99));
            textWidth = (int) mPaint.measureText(text, 0, text.length());
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, width / 2 - textWidth / 2, height / 4 + textHeight / 2, mPaint);
        }

        if (!TextUtils.isEmpty(mTxtHint2)) {
            mPaint.setStrokeWidth(mTxtStrokeWidth);
            text = mTxtHint2;
            textHeight = height / 8;
            mPaint.setTextSize(textHeight);
            textWidth = (int) mPaint.measureText(text, 0, text.length());
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, width / 2 - textWidth / 2, 3 * height / 4 + textHeight / 2, mPaint);
        }
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        this.invalidate();
    }

    public void setProgressNotInUiThread(int progress) {
        this.mProgress = progress;
        this.postInvalidate();
    }

    public String getmTxtHint1() {
        return mTxtHint1;
    }

    public void setmTxtHint1(String mTxtHint1) {
        this.mTxtHint1 = mTxtHint1;
    }

    public String getmTxtHint2() {
        return mTxtHint2;
    }

    public void setmTxtHint2(String mTxtHint2) {
        this.mTxtHint2 = mTxtHint2;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
