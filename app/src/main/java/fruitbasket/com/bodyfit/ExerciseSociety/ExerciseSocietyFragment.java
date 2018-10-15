package fruitbasket.com.bodyfit.ExerciseSociety;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.sackcentury.shinebuttonlib.ShineButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.R;
import fruitbasket.com.bodyfit.ui.TitleBar;

/**
 * Created by Administrator on 2016/11/10.
 */

public class ExerciseSocietyFragment extends Fragment {

    private ListView listview;
    private List<ItemContainer> mData;
    private TitleBar titleBar;
    private static List<ItemContainer> list = new LinkedList<ItemContainer>();

    private Integer headID;
    private String name, content;
    private String[] imagePath;

    private String[] nameArray = {"冰可乐", "美年达", "雪碧"};
    private Integer[] headIdArray = {R.drawable.touxiang1, R.drawable.touxiang, R.drawable.touxiang4};
    private int count = 0;

    public static final String TAG = "ExerciseSocietyFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_exercise_society, container, false);
        init(view);
        initTitleBar();
        updateData();
        Collections.sort(list);//反转list使得后面添加的数据显示在前面
        mData = list;
        SocietyAdapter adapter = new SocietyAdapter(getActivity(), (LinkedList) mData);
        listview.setAdapter(adapter);
        return view;
    }

    public void updateData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            content = bundle.getString(Conditions.EXERCISE_SOCIETY_CONTENT);
            name = nameArray[count % 3];
            headID = headIdArray[count % 3];
            count++;
            imagePath = bundle.getStringArray(Conditions.EXERCISE_SOCIETY_IMAGE);
            LinkedList<Map<String, Object>> imagelist = new LinkedList<Map<String, Object>>();
            if (imagePath != null) {
                for (int i = 0; i < imagePath.length; i++) {
                    String absPath = imagePath[i];
                    int index = imagePath[i].lastIndexOf('/');
                    String dir_path = absPath.substring(0, index);
                    File dir = new File(dir_path);
                    if (!dir.exists())
                        dir.mkdirs();
                    File file = new File(dir + File.separator + absPath.substring(index + 1));

                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("image", bitmap);
                    imagelist.add(map);
                    Log.i(TAG, imagePath[i]);
                }
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String time = simpleDateFormat.format(date);
            ItemContainer itemContainer = new ItemContainer(headID, name, content, time, imagelist);
            list.add(itemContainer);
        }
    }


    private void init(View view) {
        listview = (ListView) view.findViewById(R.id.listView);
        titleBar = (TitleBar) view.findViewById(R.id.title_bar_1);
    }

    private void initTitleBar() {
        titleBar.setTitleText("运动圈");
        titleBar.setBackgroundColor(Color.parseColor("#87CEFA"));
        titleBar.setLeftIco(-1);
        titleBar.setRightIcoListening(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PublishActivity.class);
                startActivityForResult(new Intent(getContext(), PublishActivity.class), Conditions.SOCIETY_R_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}

class ItemContainer implements Comparable<ItemContainer> {
    public int headID;
    public String name;
    public String content;
    public String time;
    public LinkedList imageArray;

    public ItemContainer(int headID, String name, String content, String time, LinkedList imageArray) {
        this.headID = headID;
        this.name = name;
        this.content = content;
        this.time = time;
        this.imageArray = imageArray;
    }

    @Override
    public int compareTo(@NonNull ItemContainer itemContainer) {
        return itemContainer.time.compareTo(time);
    }
}

class SocietyAdapter extends BaseAdapter {
    public static final String TAG = "SocietyAdapter";

    private Context context;
    private List<ItemContainer> item;

    public SocietyAdapter(Context con, LinkedList list) {
        context = con;
        item = list;
    }

    @Override
    public int getCount() {
        return item == null ? 0 : item.size();
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

        ItemContainer itemContainer = item.get(position);
        if (convertView == null) {
            viewholder = new ViewHolder();
            convertView = View.inflate(context, R.layout.society_list_item, null);
            viewholder.image = (ImageView) convertView.findViewById(R.id.society_headcul);
            viewholder.name = (TextView) convertView.findViewById(R.id.name);
            viewholder.content = (TextView) convertView.findViewById(R.id.content);
            viewholder.picture = (GridView) convertView.findViewById(R.id.picture);
//            viewholder.comment = (Button) convertView.findViewById(R.id.society_comment);
            viewholder.time = (TextView) convertView.findViewById(R.id.society_time);
            viewholder.like=(ShineButton) convertView.findViewById(R.id.society_like);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        viewholder.image.setImageResource(itemContainer.headID);
        viewholder.name.setText(itemContainer.name);
        viewholder.content.setText(itemContainer.content);
        viewholder.time.setText(itemContainer.time);

        SimpleAdapter simpleAdapter = new SimpleAdapter(context, itemContainer.imageArray, R.layout.cell, new String[]{"image"}, new int[]{R.id.imageView});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView iv = (ImageView) view;
                    iv.setImageBitmap((Bitmap) data);
                    return true;
                } else {
                    return false;
                }
            }

        });
        viewholder.picture.setAdapter(simpleAdapter);

        viewholder.picture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "click on " + position, Toast.LENGTH_SHORT).show();
            }
        });
        /*viewholder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "comment", Toast.LENGTH_SHORT).show();
            }
        });*/

        return convertView;
    }

    class ViewHolder {
        private ImageView image;
        private TextView name;
        private TextView content;
        private GridView picture;
        private TextView time;
        private Button comment;
        private ShineButton like;
    }
}



