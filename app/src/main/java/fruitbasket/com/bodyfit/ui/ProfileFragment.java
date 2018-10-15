package fruitbasket.com.bodyfit.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import fruitbasket.com.bodyfit.R;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_profile, container, false);

        final SharedPreferences preferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        ListView list = (ListView) view.findViewById(R.id.profile_list);

        final ArrayList<Map<String, String>> al = refresh();
        final SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), al,
                R.layout.layout_profileandtarget_listext,
                new String[]{"info", "data"},
                new int[]{R.id.profile_items, R.id.profile_info});

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:    //modify the nickname
                    {
                        final EditText text = new EditText(getContext());
                        //获取设置过的nickname，如果找不到则用默认的
                        text.setText(preferences.getString("nickname", getContext().getResources().getString(R.string.default_nickname)));
                        new AlertDialog.Builder(getActivity()).
                                setView(text).
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        editor.putString("nickname", text.getText().toString().trim());
                                        editor.apply();

                                        al.clear();
                                        al.addAll(refresh());
                                        adapter.notifyDataSetChanged();
                                    }
                                }).
                                setNegativeButton("取消", null).
                                create().show();

                        break;
                    }

                    case 1:   //modify sex
                        LinearLayout ll = new LinearLayout(getContext());
                        RadioGroup mRadioGroup = new RadioGroup(getContext());

                        final RadioButton RB1 = new RadioButton(getContext());
                        RB1.setText("男");
                        RB1.setTextColor(Color.rgb(153, 153, 255));
                        RB1.setTextSize(40);

                        final RadioButton RB2 = new RadioButton(getContext());
                        RB2.setText("女");
                        RB2.setTextColor(Color.rgb(153, 153, 255));
                        RB2.setTextSize(40);

                        String sex = preferences.getString("sex", getContext().getResources().getString(R.string.default_sex));


                        mRadioGroup.addView(RB1);
                        mRadioGroup.addView(RB2);
                        mRadioGroup.setPadding(50, 50, 50, 50);
                        mRadioGroup.setOrientation(LinearLayout.HORIZONTAL);

                        if (sex.equals("男"))
                            mRadioGroup.check(RB1.getId());
                        else
                            mRadioGroup.check(RB2.getId());


                        ll.addView(mRadioGroup);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setView(ll).create();
                        final Dialog dialog = builder.show();

                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (checkedId == RB1.getId())
                                    editor.putString("sex", "男");
                                else
                                    editor.putString("sex", "女");

                                editor.apply();
                                dialog.dismiss();
                                al.clear();
                                al.addAll(refresh());
                                adapter.notifyDataSetChanged();
                            }
                        });
                        break;
                    case 2:  //modify height
                        int height = preferences.getInt("height",
                                Integer.parseInt(getContext().getResources().getString(R.string.default_height)));

                        final NumberPicker mPicker = new NumberPicker(getContext());
                        mPicker.setMinValue(50);
                        mPicker.setMaxValue(230);
                        mPicker.setValue(height);


                        new AlertDialog.Builder(getActivity()).
                                setView(mPicker).
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        editor.putInt("height", mPicker.getValue());
                                        editor.apply();

                                        al.clear();
                                        al.addAll(refresh());
                                        adapter.notifyDataSetChanged();
                                    }
                                }).
                                setNegativeButton("取消", null).
                                create().show();
                        break;
                    case 3:  //modify weight

                        int weight = preferences.getInt("weight",
                                Integer.parseInt(getContext().getResources().getString(R.string.default_weight)));

                        final NumberPicker mPicker2 = new NumberPicker(getContext());
                        mPicker2.setMinValue(50);
                        mPicker2.setMaxValue(150);
                        mPicker2.setValue(weight);


                        new AlertDialog.Builder(getActivity()).
                                setView(mPicker2).
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        editor.putInt("weight", mPicker2.getValue());
                                        editor.apply();

                                        al.clear();
                                        al.addAll(refresh());
                                        adapter.notifyDataSetChanged();
                                    }
                                }).
                                setNegativeButton("取消", null).
                                create().show();
                        break;
                }
            }
        });

        return view;
    }

    private ArrayList<Map<String, String>> refresh() {

        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);

        ArrayList<Map<String, String>> listItems = new ArrayList<>();

        String[] profile_array = getResources().getStringArray(R.array.profile_array);
        String[] profile_array_default = getResources().getStringArray(R.array.profile_array_default);


        String getNickname = preferences.getString("nickname", getContext().getResources().getString(R.string.default_nickname));
        String getSex = preferences.getString("sex", "男");
        String getHeight = String.valueOf(preferences.getInt("height", 0));
        String getWeight = String.valueOf(preferences.getInt("weight", 0));
        String[] data = new String[]{getNickname, getSex, getHeight, getWeight};

        for (int i = 0; i < profile_array.length; i++) {
            Map<String, String> mapItems = new HashMap<>();
            mapItems.put("info", profile_array[i]);
            if (data[i].length() > 0)
                mapItems.put("data", data[i]);
            else
                mapItems.put("data", profile_array_default[i]);

            listItems.add(mapItems);
        }
        return listItems;
    }
}
