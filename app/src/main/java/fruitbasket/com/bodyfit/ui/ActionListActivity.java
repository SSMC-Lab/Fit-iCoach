package fruitbasket.com.bodyfit.ui;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.service.notification.Condition;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.R;
import fruitbasket.com.bodyfit.utils.SPUtils;

public class ActionListActivity extends Activity {
    private List<ActionListItem> actionList = new ArrayList<>();
    private String grades[] = {"初级", "中级", "高级"};
    private String grade = "初级";
    private String actionName[] = {"", "A01-坐姿哑铃弯举", "A02-锤式弯举", "A03-哑铃侧卧外旋", "A04-坐姿器械推胸",
            "A05-正握下拉", "A06-弹力绳高位面拉", "A07-站姿飞鸟", "A08-坐姿推肩", "A09-阿诺德推举", "A10-单臂哑铃侧曲", "A11-杠铃划船", "A12-杠铃卧推",
            "A13-蝴蝶机夹胸", "A14-上斜哑铃飞鸟", "A15-背后拉力器弯举"};
    private String times[] = {"", "2组/每组10个", "3~4组/每组10个", "5组/每组10个"};
    private int img[] = {0, R.drawable.action1, R.drawable.action2, R.drawable.action3,
            R.drawable.action4, R.drawable.action5, R.drawable.action6,
            R.drawable.action7, R.drawable.action8, R.drawable.action9,
            R.drawable.action10, R.drawable.action11, R.drawable.action12,
            R.drawable.action13, R.drawable.action14, R.drawable.action15};
    private String TAG = "ActionListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_action_list);
        initAction();
        ActionAdapter adapter = new ActionAdapter(ActionListActivity.this, R.layout.layout_action_item, actionList);
        ListView listView = (ListView) findViewById(R.id.action_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActionListItem item = actionList.get(position);
                Intent intent = new Intent(ActionListActivity.this, ActionIntroductionActivity.class);
                switch (position) {
                    default:
                        intent.putExtra("action", Conditions.exerciseName[position]);
                        break;

                }
                startActivity(intent);
            }
        });
    }

    public void initAction() {
        grade = (String) SPUtils.get(Conditions.User_grade, "初级");
        if (grade == "初级") {
            for (int i = 1; i <= 15; i++) {
                if (i == 2 || i == 6 || i >= 11)
                    continue;
                else {
                    ActionListItem item = new ActionListItem(actionName[i], times[1], img[i]);
                    actionList.add(item);
                }
            }
        } else if (grade == "中级") {
            for (int i = 1; i <= 15; i++) {
                if (i == 6 || i == 15 || i == 12)
                    continue;
                else {
                    ActionListItem item = new ActionListItem(actionName[i], times[2], img[i]);
                    actionList.add(item);
                }
            }
        } else if (grade == "高级") {
            for (int i = 1; i <= 15; i++) {
                ActionListItem item = new ActionListItem(actionName[i], times[3], img[i]);
                actionList.add(item);
            }
        } else {
            for (int i = 1; i <= 15; i++) {
                if (i == 2 || i == 6 || i >= 11)
                    continue;
                else {
                    ActionListItem item = new ActionListItem(actionName[i], times[1], img[i]);
                    actionList.add(item);
                }
            }
        }
    }

}
