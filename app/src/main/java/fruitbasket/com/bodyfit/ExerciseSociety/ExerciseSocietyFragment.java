package fruitbasket.com.bodyfit.ExerciseSociety;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.MyApplication;
import fruitbasket.com.bodyfit.R;
import fruitbasket.com.bodyfit.ui.TitleBar;

/**
 * Created by Administrator on 2016/11/10.
 *
 */
public class ExerciseSocietyFragment extends Fragment {
    public static final String TAG="ExerciseSocietyFragment";

    private ListView listview;
    private List<ItemContainer> mData;
    private TitleBar titleBar;
    private static ArrayList<ItemContainer> list = new ArrayList<ItemContainer>();

    private Integer headID;
    private String name,content;
    private String[] imagePath;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_exercise_society,container,false);
        init(view);
        initTitleBar();
        updateData();
        Collections.reverse(list);  //反转list使得后面添加的数据显示在前面
        mData=list;
        SocietyAdapter adapter= new SocietyAdapter(getActivity(), (ArrayList) mData);
        listview.setAdapter(adapter);
        return view;
    }

    public void updateData(){
        Bundle bundle=getArguments();
        if(bundle!=null) {
            content = bundle.getString(Conditions.EXERCISE_SOCIETY_CONTENT);
            name="这有冰可乐";
            headID=(Integer)R.drawable.touxiang1;
            imagePath=bundle.getStringArray(Conditions.EXERCISE_SOCIETY_IMAGE);
            ArrayList<Map<String,Object>> imagelist=new ArrayList<Map<String,Object>>();
            if(imagePath!=null){
                for(int i=0;i<imagePath.length;i++){
                    String absPath=imagePath[i];
                    int index=imagePath[i].lastIndexOf('/');
                    String dir_path=absPath.substring(0,index);
                    File dir=new File(dir_path);
                    if(!dir.exists())
                        dir.mkdirs();
                    File file=new File(dir+File.separator+absPath.substring(index+1));

                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("image",bitmap);
                    imagelist.add(map);
                    Log.i(TAG,imagePath[i]);
                }
            }
            ItemContainer itemContainer=new ItemContainer(headID,name,content,imagelist);
            list.add(itemContainer);
        }
    }


    private void init(View view){
        listview= (ListView) view.findViewById(R.id.listView);
        titleBar=(TitleBar)view.findViewById(R.id.title_bar_1);
    }

    private void initTitleBar(){
        titleBar.setTitleText("运动圈");
        titleBar.setBackgroundColor(Color.parseColor("#87CEFA"));
        titleBar.setLeftIco(-1);
        titleBar.setRightIcoListening(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),PublishActivity.class);
                startActivity(intent);
            }
        });
    }
    private ArrayList getData() {
        for(int i=0;i<20;i++) {
            ArrayList<Map<String,Object>> imagelist=new ArrayList<Map<String,Object>>();
            headID=(Integer)R.drawable.society_head;
            name="张三"+i;
            content="";
            for(int j=0;j<3;j++){
                content+="测试 ";
            }
            if(i==0){
                name="这有冰可乐";
                content="滴，健身卡！";
                headID=(Integer)R.drawable.touxiang1;
                ItemContainer itemContainer=new ItemContainer(headID,name,content,imagelist);
                list.add(itemContainer);
                continue;
            }
            if(i==1){
                name="杨玉米";
                content="想给全世界安利这款手套！性价比超高，提供实时且专业的反馈。自从有了智能健身手套手套，再也不用去健身房了呢~";
                headID=(Integer)R.drawable.touxiang4;
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("image",R.drawable.pengyouquan2);
                imagelist.add(map);
                ItemContainer itemContainer=new ItemContainer(headID,name,content,imagelist);
                list.add(itemContainer);
                continue;
            }
            if(i==2){
                name="54成";
                content="王者绝非偶然，实力成就非凡。";
                headID=(Integer)R.drawable.touxiang3;
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("image",R.drawable.pengyouquan1);
                imagelist.add(map);
                Map<String,Object> map1=new HashMap<String,Object>();
                map1.put("image",R.drawable.pengyouquan3);
                imagelist.add(map1);
                ItemContainer itemContainer=new ItemContainer(headID,name,content,imagelist);
                list.add(itemContainer);
                continue;
            }
            for(int j=0;j<i;j++) {
                if(j==9)
                    break;
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("image",R.drawable.society_head);
                if(j==1)
                imagelist.add(map);
            }
            ItemContainer itemContainer=new ItemContainer(headID,name,content,imagelist);

            list.add(itemContainer);
        }
        return list;
    }
}

class ItemContainer{
    public int headID;
    public String name;
    public String content;
    public ArrayList imageArray;
    public ItemContainer(int h,String n,String con,ArrayList list){
        headID=h;
        name=n;
        content=con;
        imageArray=list;
    }
}

class SocietyAdapter extends BaseAdapter{
    public static final String TAG="SocietyAdapter";

    private Context context;
    private List<ItemContainer> item;

    public SocietyAdapter(Context con,ArrayList list){
        context=con;
        item=list;
    }

    @Override
    public int getCount() {
        return item==null?0:item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder;

        ItemContainer itemContainer=item.get(position);
        if(convertView==null){
            viewholder=new ViewHolder();
            convertView=View.inflate(context,R.layout.society_list_item,null);
            viewholder.image= (ImageView) convertView.findViewById(R.id.society_headcul);
            viewholder.name= (TextView) convertView.findViewById(R.id.name);
            viewholder.content= (TextView) convertView.findViewById(R.id.content);
            viewholder.picture= (GridView) convertView.findViewById(R.id.picture);
            viewholder.comment= (Button) convertView.findViewById(R.id.society_comment);

            convertView.setTag(viewholder);
        }else{
            viewholder= (ViewHolder) convertView.getTag();
        }

        viewholder.image.setImageResource(itemContainer.headID);
        viewholder.name.setText(itemContainer.name);
        viewholder.content.setText(itemContainer.content);

        SimpleAdapter simpleAdapter=new SimpleAdapter(context,itemContainer.imageArray,R.layout.cell,new String[]{"image"},new int[]{R.id.imageView});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view instanceof ImageView  && data instanceof Bitmap){
                    ImageView iv = (ImageView)view;
                    iv.setImageBitmap((Bitmap) data);
                    return true;
                }else{
                    return false;                 }
            }

        });
        viewholder.picture.setAdapter(simpleAdapter);

        viewholder.picture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "click on " + position, Toast.LENGTH_SHORT).show();
            }
        });
        viewholder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "comment", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
    class ViewHolder{
        private ImageView image;
        private TextView name;
        private TextView content;
        private GridView picture;
        private TextView time;
        private Button comment;
    }
}



