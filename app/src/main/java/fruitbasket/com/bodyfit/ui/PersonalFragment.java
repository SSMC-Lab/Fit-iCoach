package fruitbasket.com.bodyfit.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.DietRecommendations.DietAdviceSelectActivity;
import fruitbasket.com.bodyfit.Login.LoginActivity;
import fruitbasket.com.bodyfit.R;

/**
 * Created by Administrator on 2016/10/26.
 */
public class PersonalFragment extends Fragment {
    public static final String TAG = "PersonalFragment";
    @Nullable
    private RelativeLayout personalInfo;  //个人信息
    private Button history; //历史记录
    private Button unfamiliarAction; //陌生动作
    private Button fitnessAssessment;   //体能评估
    private Button actionAdvice;    //健身建议
    private Button dietAdvice;  //饮食建议
    private TextView mUserName;
    Bundle bundle;
    private static String userrName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_personal, container, false);

        initView(view);

        Log.e(TAG, "onCreateView: onResume" );
        personalInfo.setOnClickListener(new perInfoOnClickListener());
        history.setOnClickListener(new myOnClickListener());
        unfamiliarAction.setOnClickListener(new myOnClickListener());
        fitnessAssessment.setOnClickListener(new myOnClickListener());
        actionAdvice.setOnClickListener(new myOnClickListener());
        dietAdvice.setOnClickListener(new myOnClickListener());


        Drawable drawable = getResources().getDrawable(R.drawable.history);
        drawable.setBounds(0, 0, 70, 70);
        history.setCompoundDrawables(drawable, null, null, null);
        drawable = getResources().getDrawable(R.drawable.unfamiliar_action);
        drawable.setBounds(0, 0, 50, 50); //设置图标的大小
        unfamiliarAction.setCompoundDrawables(drawable, null, null, null);
        drawable = getResources().getDrawable(R.drawable.fitness_assessment);
        drawable.setBounds(0, 0, 50, 50);
        fitnessAssessment.setCompoundDrawables(drawable, null, null, null);
        drawable = getResources().getDrawable((R.drawable.diet_advice));
        drawable.setBounds(0, 0, 50, 50);
        dietAdvice.setCompoundDrawables(drawable, null, null, null);
        drawable = getResources().getDrawable(R.drawable.action_advice);
        drawable.setBounds(0, 0, 50, 50);
        actionAdvice.setCompoundDrawables(drawable, null, null, null);

        return view;
    }

    private void initView(View view) {
        personalInfo = (RelativeLayout) view.findViewById(R.id.personalInformation);
        history = (Button) view.findViewById(R.id.history);
        unfamiliarAction = (Button) view.findViewById(R.id.unfamiliarExercise);
        fitnessAssessment = (Button) view.findViewById(R.id.fitnessAssessment);
        actionAdvice = (Button) view.findViewById(R.id.fitnessAdvice);
        dietAdvice = (Button) view.findViewById(R.id.dietAdvice);
        mUserName = (TextView) view.findViewById(R.id.personal_name);

        bundle = getArguments();
        Log.i(TAG, "onCreateView: Bundle==" + bundle);
        if (bundle != null) {
            Log.i(TAG, "onCreateView: Bundle== name=" + bundle.getString(Conditions.USER_NAME));
            mUserName.setText(bundle.getString(Conditions.USER_NAME));
            mUserName.setText("Hhahahah");
        }

    }

    private class perInfoOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bundle != null) {
            Log.e(TAG, "onResume: 1");
            userrName = bundle.getString(Conditions.USER_NAME);
            mUserName.setText(bundle.getString(Conditions.USER_NAME));
            Log.e(TAG, "onResume: 3");
        }
        if (userrName != null)
            mUserName.setText(userrName);
//        mUserName.setText("123");


    }

    private class myOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.history:
                    Toast.makeText(getContext(), "history", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getContext(), CompletenessActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.unfamiliarExercise:
                    Toast.makeText(getContext(), "unfamiliarExercise", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.fitnessAssessment:
//                    Toast.makeText(getContext(),"fitnessAssessment",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), DataTableActivity.class);
                    startActivity(intent);
                    break;

                case R.id.fitnessAdvice:
                    Toast.makeText(getContext(), "fitnessAdvice", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.dietAdvice:
                    Toast.makeText(getContext(), "dietAdvice", Toast.LENGTH_SHORT).show();
//                    Intent intent2 = new Intent(getContext(),FoodScoreActivity.class);
                    Intent intent2 = new Intent(getContext(), DietAdviceSelectActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    }
}
