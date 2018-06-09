package fruitbasket.com.bodyfit.ExerciseSociety;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.PhotoPreviewActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;
import com.lqr.imagepicker.ui.ImageGridActivity;
import com.lqr.imagepicker.ui.ImagePreviewActivity;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.util.ArrayList;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.R;
import fruitbasket.com.bodyfit.ui.CompletenessActivity;
import fruitbasket.com.bodyfit.ui.DataTableActivity;
import fruitbasket.com.bodyfit.ui.DietAdviceSelectActivity;
import fruitbasket.com.bodyfit.ui.ExerciseFragment;
import fruitbasket.com.bodyfit.ui.MainActivity;
import fruitbasket.com.bodyfit.ui.TitleBar;
import weka.gui.Main;

public class PublishActivity extends AppCompatActivity {

    private String[] imagePath=null;
    private ArrayList<String> imagePaths = new ArrayList<>();
    public static final int IMAGE_PICKER = 100;
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;

    private TitleBar titleBar;
    private EditText publishContent;
    private String content;
    private GridView choosePhoto;
    private GridAdapter gridAdapter;

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
        choosePhoto=(GridView) findViewById(R.id.select_photo);

        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        choosePhoto.setNumColumns(cols);
        choosePhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String imgs = (String) parent.getItemAtPosition(position);
                if ("paizhao".equals(imgs) ){
                    PhotoPickerIntent intent = new PhotoPickerIntent(PublishActivity.this);
                    intent.setSelectModel(SelectModel.MULTI);
                    intent.setShowCarema(true); // 是否显示拍照
                    intent.setMaxTotal(6); // 最多选择照片数量，默认为6
                    intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                    startActivityForResult(intent, REQUEST_CAMERA_CODE);
                }else{
                    Toast.makeText(PublishActivity.this,"1"+position,Toast.LENGTH_SHORT).show();
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(PublishActivity.this);
                    intent.setCurrentItem(position);
                    intent.setPhotoPaths(imagePaths);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }
            }
        });
        imagePaths.add("paizhao");
        gridAdapter = new GridAdapter(imagePaths);
        choosePhoto.setAdapter(gridAdapter);

    }

    private void initTitleBar(){
        titleBar.setTitleText("发表文字");
        titleBar.setBackgroundColor(Color.WHITE);
        titleBar.setTitleTextColor();
        titleBar.setTitleRight("发表");
        titleBar.setRightTextListening(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePath=new String[imagePaths.size()-1];
                for(int i=0;i<imagePaths.size()-1;i++)
                    imagePath[i]=imagePaths.get(i);
                Bundle bundle=new Bundle();
                content=publishContent.getText().toString();
                bundle.putString(Conditions.EXERCISE_SOCIETY_CONTENT,content);
                bundle.putStringArray(Conditions.EXERCISE_SOCIETY_IMAGE,imagePath);
                Intent intent=new Intent(PublishActivity.this, MainActivity.class);
                intent.putExtra("fragmentId",Conditions.SOCIETY_R_CODE);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        titleBar.setLeftIcoListening(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=null;
                Intent intent=new Intent(PublishActivity.this, MainActivity.class);
                intent.putExtra("fragmentId",Conditions.SOCIETY_R_CODE);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Log.d(TAG, "数量："+list.size());
                    loadAdpater(list);
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    loadAdpater(ListExtra);
                    break;
            }
        }
    }

    private void loadAdpater(ArrayList<String> paths){
        if (imagePaths!=null&& imagePaths.size()>0){
            imagePaths.clear();
        }
        if (paths.contains("paizhao")){
            paths.remove("paizhao");
        }
        paths.add("paizhao");
        imagePaths.addAll(paths);
        gridAdapter  = new GridAdapter(imagePaths);
        choosePhoto.setAdapter(gridAdapter);
        try{
            JSONArray obj = new JSONArray(imagePaths);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class GridAdapter extends BaseAdapter {
        private ArrayList<String> listUrls;
        private LayoutInflater inflater;
        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
            if(listUrls.size() == 7){
                listUrls.remove(listUrls.size()-1);
            }
            inflater = LayoutInflater.from(PublishActivity.this);
        }

        public int getCount(){
            return  listUrls.size();
        }
        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.girdview_item, parent,false);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            final String path=listUrls.get(position);
            if (path.equals("paizhao")){
                holder.image.setImageResource(R.drawable.ic_add_photo);
            }else {
                Glide.with(PublishActivity.this)
                        .load(path)
                        .placeholder(R.drawable.ic_add_photo)
                        .error(R.drawable.ic_add_photo)
                        .centerCrop()
                        .crossFade()
                        .into(holder.image);
            }
            return convertView;
        }
        class ViewHolder {
            ImageView image;
        }
    }
}
