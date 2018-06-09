package fruitbasket.com.bodyfit.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.util.WheelUtils;
import com.wx.wheelview.widget.WheelView;
import com.wx.wheelview.widget.WheelViewDialog;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import fruitbasket.com.bodyfit.R;

public class DietAdviceSelectActivity extends AppCompatActivity {

    private static final String TAG = "DietAdviceSelctActivity";

    TextView ageText;
    int agePosition = 0;

    TextView genderText;
    int genderPosition = 0;

    TextView weightText;
    int weightPosition = 0;

    TextView heightText;
    int heightPosition = 0;

    TextView purposeText;
    int purposePosition = 0;

    TextView sportCoefText;
    int sportCoefPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_diet_advice_select);

        ageText = (TextView)findViewById(R.id.AgeText);
        ageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewDialog wvd = new WheelViewDialog(DietAdviceSelectActivity.this);
                switch (agePosition){
                    case 0:
                        wvd.setTitle("年龄").setItems(getAgeList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(5).show();
                        break;
                    default:
                        wvd.setSelection(agePosition).setTitle("年龄").setItems(getAgeList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(5).show();
                        break;
                }

                wvd.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int position, String s) {
                        ageText.setText(s);
                        agePosition = position;
                    }
                });
            }
        });

        genderText = (TextView)findViewById(R.id.genderText);

        genderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewDialog wvd = new WheelViewDialog(DietAdviceSelectActivity.this);
                switch (genderPosition){
                    case 0:
                        wvd.setTitle("性别").setItems(getGenderList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(3).show();
                        break;
                    default:
                        wvd.setSelection(genderPosition).setTitle("性别").setItems(getGenderList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(3).show();
                        break;
                }

                wvd.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int position, String s) {
                        genderText.setText(s);
                        genderPosition = position;
                    }
                });
            }
        });

        weightText = (TextView)findViewById(R.id.weightText);

        weightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewDialog wvd = new WheelViewDialog(DietAdviceSelectActivity.this);
                switch (weightPosition){
                    case 0:
                        wvd.setTitle("体重").setItems(getWeightList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(5).show();
                        break;
                    default:
                        wvd.setSelection(weightPosition).setTitle("体重").setItems(getWeightList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(5).show();
                        break;
                }

                wvd.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int position, String s) {
                        weightText.setText(s);
                        weightPosition = position;
                    }
                });
            }
        });



        heightText = (TextView)findViewById(R.id.heightText);

        heightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewDialog wvd = new WheelViewDialog(DietAdviceSelectActivity.this);
                switch (heightPosition){
                    case 0:
                        wvd.setTitle("身高").setItems(getHeightList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(5).show();
                        break;
                    default:
                        wvd.setSelection(heightPosition).setTitle("身高").setItems(getHeightList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(5).show();

                }

                wvd.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int position, String s) {
                        heightPosition = position;
                        heightText.setText(s);
                    }
                });
            }
        });

        purposeText = (TextView) findViewById(R.id.purposeText);

        purposeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewDialog wvd = new WheelViewDialog(DietAdviceSelectActivity.this);
                switch (purposePosition){
                    case 0:
                        wvd.setTitle("目标").setItems(getPurposeList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(5).show();
                        break;
                    default:
                        wvd.setSelection(purposePosition).setTitle("目标").setItems(getPurposeList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(5).show();
                        break;

                }

                wvd.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int position, String s) {
                        purposeText.setText(s);
                        purposePosition = position;
                    }
                });
            }
        });

        sportCoefText = (TextView)findViewById(R.id.sportCoefText);

        sportCoefText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewDialog wvd = new WheelViewDialog(DietAdviceSelectActivity.this);
                switch (sportCoefPosition){
                    case 0:
                        wvd.setTitle("运动系数").setItems(getSportCoefList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(3).show();
                        break;
                    default:
                        wvd.setSelection(sportCoefPosition).setTitle("运动系数").setItems(getPurposeList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(5).show();
                        break;

                }

                wvd.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int position, String s) {

                        sportCoefText.setText(s.substring(0,s.indexOf('(')));
                        sportCoefPosition = position;
                    }
                });
            }
        });

        Button button = (Button)findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String age,gender,weight,height,purpose,sportsCoef;
                age = String.valueOf(ageText.getText());
                gender = String.valueOf(genderText.getText());
                weight = String.valueOf(weightText.getText());
                height = String.valueOf(heightText.getText());
                purpose = String.valueOf(purposeText.getText());
                sportsCoef = String.valueOf(sportCoefText.getText());
                Recommendation(age,gender,weight,height,purpose,sportsCoef);
                Intent intent=new Intent(DietAdviceSelectActivity.this,DietAdviceActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     *  产生相应的ArrayList
     * @return specified ArrayList
     */

    private ArrayList<String> getGenderList(){
        ArrayList<String> gender = new ArrayList<>();
        gender.add("男");
        gender.add("女");
        return gender;
    }

    private ArrayList<String> getAgeList(){
        ArrayList<String> ageList = new ArrayList<>();
        for(int i = 1;i<=100;++i){
            ageList.add(String.valueOf(i));
        }
        return ageList;
    }

    private ArrayList<String> getHeightList(){
        ArrayList<String> HeightList = new ArrayList<>();
        for(int i = 100;i<=250;++i){
            HeightList.add(String.valueOf(i));
        }
        return HeightList;
    }

    private ArrayList<String> getWeightList(){
        ArrayList<String> weightList = new ArrayList<>();
        for(int i = 30;i<=150;++i){
            weightList.add(String.valueOf(i));
        }
        return weightList;
    }

    private ArrayList<String> getPurposeList(){
        ArrayList<String> purpose = new ArrayList<>();
        purpose.add("减脂");
        purpose.add("增肌");
        return purpose;
    }

    private ArrayList<String> getSportCoefList(){
        ArrayList<String> sportCoef = new ArrayList<>();
        sportCoef.add("0.9(不进行或很少进行运动)");
        sportCoef.add("1.375(一周进行1-3次低强度运动(心率低于120))");
        sportCoef.add("1.55(一周进行3-5次中等强度运动(心率低于150))");
        sportCoef.add("1.725(一周进行6-7次强度较大运动)");
        sportCoef.add("1.9(从事非常高强度活动或运动的人群)");
        return sportCoef;
    }

    /**
     * params : 年龄 性别 身高 体重 健身目的
     */

    private void Recommendation(String Age,String Gender,String Height,String Weight,String Purpose,String SportsCoef){

        Log.d(TAG, "Recommendation: "+"Age:"+Age+",Gender:"+Gender+",Height:"+Height+",Weight:"+Weight+",Purpose:"+Purpose);
        int age,weight,height;
        double sportsCoef;
        if(Age.equals(""))
            age=0;
        else
            age = Integer.parseInt(Age);
        if (Weight.equals(""))
            weight=0;
        else
            weight = Integer.parseInt(Weight);
        if(Height.equals(""))
            height=0;
        else
            height = Integer.parseInt(Height);
        if(SportsCoef.equals(""))
            sportsCoef=0;
        else
            sportsCoef = Double.parseDouble(SportsCoef);

        // step1: BMR
        double BMR;
        switch (Gender){
            case "男":
                BMR = weight * 10 + 6.25 * height - 5 * age + 5;
                break;
            case "女":
                BMR = 10 * weight + 6.25 * height - 5 * age - 161;
                break;
            default:
                BMR = 1;
                break;
        }

        // step 2
        double normalHeat = BMR * sportsCoef;

        switch (Purpose){
            case "增肌":
                break;
            case "减脂":

                break;
            default:
                break;
        }
    }

}
