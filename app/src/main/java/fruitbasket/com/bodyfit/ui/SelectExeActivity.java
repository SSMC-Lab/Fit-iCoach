package fruitbasket.com.bodyfit.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import fruitbasket.com.bodyfit.R;

public class SelectExeActivity extends Activity {
    public static final String TAG = "SelectExeActivity";
    public static final int maxClickTimes = 2;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String[] target_array;
    private String[] target_array_default;
    private int clickTimes;
    private ListView list1;
    private ListView list2;
    private ListView list3;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_type);

        preferences = context.getSharedPreferences("user_target", Context.MODE_PRIVATE);
        editor = preferences.edit();
        target_array = getResources().getStringArray(R.array.target_array);
        target_array_default = getResources().getStringArray(R.array.target_array_default);
        clickTimes = preferences.getInt("count", Integer.parseInt(getResources().getString(R.string.default_target_clicktimes)));  //初始化点击增加/删除 标志的次数

        CreateTargetItem();

        //-------增删信息-----
        final ImageButton addButton = (ImageButton) findViewById(R.id.target_add);
        final ImageButton delButton = (ImageButton) findViewById(R.id.target_del);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickTimes == maxClickTimes) {
                    Toast.makeText(context, "最多添加" + (maxClickTimes + 1) + "项哦~", Toast.LENGTH_SHORT).show();
                } else {
                    //show animation
                    RotateAnimation ra = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    ra.setFillAfter(true);
                    ra.setDuration(500);
                    addButton.startAnimation(ra);

                    //set visibility
                    if (clickTimes == 0)
                        list2.setVisibility(View.VISIBLE);
                    else if (clickTimes == 1)
                        list3.setVisibility(View.VISIBLE);

                    // deal with logic code
                    clickTimes++;
                    editor.putInt("count", clickTimes);
                    editor.apply();


                }
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickTimes == 0) {
                    Toast.makeText(context, "最少要有一项哦~", Toast.LENGTH_SHORT).show();
                } else {
                    //show animation
                    RotateAnimation ra = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    ra.setFillAfter(true);
                    ra.setDuration(500);
                    delButton.startAnimation(ra);

                    // deal with logic code

                    int tmpInt = clickTimes + 1;
                    String tmpStr = String.valueOf(tmpInt);
                    editor.putInt("count", clickTimes);
                    editor.putInt("action" + tmpStr, 0);
                    editor.putInt("target_sets" + tmpStr, Integer.parseInt(target_array_default[1]));
                    editor.putInt("target_times" + tmpStr, Integer.parseInt(target_array_default[2]));
                    editor.apply();
                    //set visibility
                    if (clickTimes == 1)
                        list2.setVisibility(View.INVISIBLE);
                    else if (clickTimes == 2)
                        list3.setVisibility(View.INVISIBLE);

                    clickTimes--;
                    CreateTargetItem();
                }
            }
        });
    }

    //初始化和点击选项会调用该函数,自动根据count来调整
    private ArrayList<Map<String, String>> refresh(int ii) {
        final String preferences_action = "action" + ii;
        final String preferences_target_sets = "target_sets" + ii;
        final String preferences_target_times = "target_times" + ii;

        ArrayList<Map<String, String>> listItems = new ArrayList<>();

        int getTargetType = preferences.getInt(preferences_action, 0);
        String getTargetSets;
        String getTargetTimes;
        if (getTargetType == 0) {
            editor.putInt(preferences_target_sets, 0);
            editor.putInt(preferences_target_times, 0);
            editor.apply();

            getTargetSets = "0";
            getTargetTimes = "0";
        } else {
            getTargetSets = String.valueOf(preferences.getInt(preferences_target_sets, 0));
            getTargetTimes = String.valueOf(preferences.getInt(preferences_target_times, 0));
        }
        String[] data = new String[]{getResources().getStringArray(R.array.action_type)[getTargetType], getTargetSets, getTargetTimes};

        //写入listItems
        for (int i = 0; i < target_array.length; i++) {
            Map<String, String> mapItems = new HashMap<>();
            mapItems.put("target", target_array[i]);
            if (getTargetType != 0)
                mapItems.put("data", data[i]);
            else
                mapItems.put("data", target_array_default[i]);

            listItems.add(mapItems);
        }
        return listItems;
    }

    private void CreateTargetItem() {
        for (int ii = 1; ii <= maxClickTimes + 1; ++ii) {
            final ArrayList<Map<String, String>> al = refresh(ii);
            final SimpleAdapter adapter = new SimpleAdapter(context, al,
                    R.layout.layout_profileandtarget_listext,
                    new String[]{"target", "data"},
                    new int[]{R.id.profile_items, R.id.profile_info});

            ListView list; //为了下面代码都是用list，不用来个if else判断是list1还是list2等等
            if (ii == 1) {
                list1 = (ListView) findViewById(R.id.target_list1);
                list = list1;
            } else if (ii == 2) {
                list2 = (ListView) findViewById(R.id.target_list2);
                list = list2;
                if (clickTimes == 0)
                    list2.setVisibility(View.INVISIBLE);
            } else {
                list3 = (ListView) findViewById(R.id.target_list3);
                list = list3;
                if (clickTimes == 1 || clickTimes == 0)
                    list3.setVisibility(View.INVISIBLE);
            }

            list.setAdapter(adapter);


            final int finalIi = ii;//为了让内部类能使用外部类变量，才这么做，当然，可以以让ii变成ii[0]
            final String preferences_action = "action" + finalIi;
            final String preferences_target_sets = "target_sets" + finalIi;
            final String preferences_target_times = "target_times" + finalIi;
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:    //modify the nickname
                            int action = preferences.getInt(preferences_action, 0);
                            final String[] actions = getResources().getStringArray(R.array.action_type);
                            final NumberPicker actionPicker = new NumberPicker(context);
                            actionPicker.setMinValue(0);
                            actionPicker.setMaxValue(actions.length - 1);
                            actionPicker.setDisplayedValues(actions);
                            actionPicker.setValue(action);

                            new AlertDialog.Builder(context).
                                    setView(actionPicker).
                                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editor.putInt(preferences_action, actionPicker.getValue());
                                            editor.apply();

                                            al.clear();
                                            al.addAll(refresh(finalIi));
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(SelectExeActivity.this, "type=" + actionPicker.getValue(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).
                                    setNegativeButton("取消", null).
                                    create().show();
                            break;
                        case 1:
                            int sets = preferences.getInt(preferences_target_sets,
                                    Integer.parseInt(context.getResources().getString(R.string.default_target_sets)));

                            final NumberPicker mSetPicker = new NumberPicker(context);
                            mSetPicker.setMinValue(1);
                            mSetPicker.setMaxValue(5);
                            mSetPicker.setValue(sets);


                            new AlertDialog.Builder(context).
                                    setView(mSetPicker).
                                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editor.putInt(preferences_target_sets, mSetPicker.getValue());
                                            editor.apply();

                                            al.clear();
                                            al.addAll(refresh(finalIi));
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(SelectExeActivity.this, "group=" + mSetPicker.getValue(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).
                                    setNegativeButton("取消", null).
                                    create().show();
                            break;
                        case 2:
                            int times = preferences.getInt(preferences_target_times,
                                    Integer.parseInt(context.getResources().getString(R.string.default_target_sets)));

                            final NumberPicker mTimePicker = new NumberPicker(context);
                            mTimePicker.setMinValue(8);
                            mTimePicker.setMaxValue(15);
                            mTimePicker.setValue(times);

                            new AlertDialog.Builder(context).
                                    setView(mTimePicker).
                                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editor.putInt(preferences_target_times, mTimePicker.getValue());
                                            editor.apply();

                                            al.clear();
                                            al.addAll(refresh(finalIi));
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(SelectExeActivity.this, "number=" + mTimePicker.getValue(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).
                                    setNegativeButton("取消", null).
                                    create().show();
                            break;
                    }
                }
            });
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            this.setResult(0, intent);
            this.finish();
            return true;
        }
        return true;
    }
}
