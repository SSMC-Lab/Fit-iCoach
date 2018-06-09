package fruitbasket.com.bodyfit.ExerciseSociety;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;
import com.lqr.imagepicker.ui.ImageGridActivity;
import com.lqr.imagepicker.ui.ImagePreviewActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.R;
import fruitbasket.com.bodyfit.ui.CompletenessActivity;
import fruitbasket.com.bodyfit.ui.DataTableActivity;
import fruitbasket.com.bodyfit.ui.DietAdviceSelectActivity;
import fruitbasket.com.bodyfit.ui.ExerciseFragment;
import fruitbasket.com.bodyfit.ui.TitleBar;

public class PublishActivity extends AppCompatActivity {

    private TitleBar titleBar;
    private EditText publishContent;
    private String content;
    private ImageView choosePhoto;
    private String[] imagePath=null;
    public static final int IMAGE_PICKER = 100;

    private static String TAG="PublishActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_publish_activity);
        initView();
        initTitleBar();
    }


    private void initView(){
        titleBar=(TitleBar)findViewById(R.id.title_bar_publish);
        publishContent=(EditText) findViewById(R.id.publish_input);
        choosePhoto=(ImageView)findViewById(R.id.add_photo);
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublishActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
            }
        });
    }

    private void initTitleBar(){
        titleBar.setTitleText("发表文字");
        titleBar.setBackgroundColor(Color.WHITE);
        titleBar.setRightIco(-1);
        titleBar.setTitleTextColor();
        titleBar.setLeftIcoListening(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResume1();
            }
        });

    }
    protected void onResume1(){
        ExerciseSocietyFragment fragment=new ExerciseSocietyFragment();
        Bundle bundle=new Bundle();
        content=publishContent.getText().toString();
        bundle.putString(Conditions.EXERCISE_SOCIETY_CONTENT,content);
        bundle.putStringArray(Conditions.EXERCISE_SOCIETY_IMAGE,imagePath);
        fragment.setArguments(bundle);
        android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.publish_layout,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {//返回多张照片
            if (data != null) {
                //是否发送原图
                boolean isOrig = data.getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Log.e("CSDN_LQR", isOrig ? "发原图" : "不发原图");//若不发原图的话，需要在自己在项目中做好压缩图片算法
                imagePath=new String[images.size()];
                for (int i=0;i<images.size();i++) {
                    ImageItem imageItem=images.get(i);
                    imagePath[i]=imageItem.path;
                }
            }
        }
    }
}
