package fruitbasket.com.bodyfit.DietRecommendations;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.wx.wheelview.widget.WheelViewDialog;

import java.util.ArrayList;

import fruitbasket.com.bodyfit.Conditions;
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

        ageText = (TextView) findViewById(R.id.AgeText);
        ageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewDialog wvd = new WheelViewDialog(DietAdviceSelectActivity.this);
                switch (agePosition) {
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

        genderText = (TextView) findViewById(R.id.genderText);

        genderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewDialog wvd = new WheelViewDialog(DietAdviceSelectActivity.this);
                switch (genderPosition) {
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

        weightText = (TextView) findViewById(R.id.weightText);

        weightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewDialog wvd = new WheelViewDialog(DietAdviceSelectActivity.this);
                switch (weightPosition) {
                    case 0:
                        wvd.setTitle("体重(kg)").setItems(getWeightList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(5).show();
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


        heightText = (TextView) findViewById(R.id.heightText);

        heightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewDialog wvd = new WheelViewDialog(DietAdviceSelectActivity.this);
                switch (heightPosition) {
                    case 0:
                        wvd.setTitle("身高(cm)").setItems(getHeightList()).setButtonText("确定").setDialogStyle(Color.parseColor("#6699ff")).setCount(5).show();
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
                switch (purposePosition) {
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

        sportCoefText = (TextView) findViewById(R.id.sportCoefText);

        sportCoefText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewDialog wvd = new WheelViewDialog(DietAdviceSelectActivity.this);
                switch (sportCoefPosition) {
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

                        sportCoefText.setText(s.substring(0, s.indexOf('(')));
                        sportCoefPosition = position;
                    }
                });
            }
        });

        final com.spark.submitbutton.SubmitButton button = (com.spark.submitbutton.SubmitButton) findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {

                String age, gender, weight, height, purpose, sportsCoef;
                double heatIn=0;
                age = String.valueOf(ageText.getText());
                gender = String.valueOf(genderText.getText());
                weight = String.valueOf(weightText.getText());
                height = String.valueOf(heightText.getText());
                purpose = String.valueOf(purposeText.getText());
                sportsCoef = String.valueOf(sportCoefText.getText());
//                LayoutInflater layoutInflater = getLayoutInflater();
//                View cur_view = layoutInflater.inflate(R.layout.layout_diet_advice_select, null);
//                View view = layoutInflater.inflate(R.layout.society_list_item, null);
//                WindowManager windowManager = getWindowManager();
//                DisplayMetrics displayMetrics = new DisplayMetrics();
//                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
//
//                Log.d(TAG, "onClick: width" + displayMetrics.widthPixels / 5 * 4 + ",height:" + displayMetrics.heightPixels / 5 * 4);
//                PopupWindow popupWindow = new PopupWindow(view, displayMetrics.widthPixels / 5 * 4, displayMetrics.heightPixels / 5 * 4);
//                popupWindow.setFocusable(true);
//                popupWindow.setOutsideTouchable(true);
//                // popupWindow.setBackgroundDrawable(new BitmapDrawable());
//                popupWindow.showAtLocation(cur_view, Gravity.CENTER, 20, 20);
//                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        WindowManager.LayoutParams alpha = getWindow().getAttributes();
//                        alpha.alpha = 1f;
//                        getWindow().setAttributes(alpha);
//                    }
//                });
//                WindowManager.LayoutParams alpha = getWindow().getAttributes();
//                alpha.alpha = 0.3f;
//                getWindow().setAttributes(alpha);

                if (age.isEmpty() || gender.isEmpty() || weight.isEmpty() || height.isEmpty() || purpose.isEmpty() || sportsCoef.isEmpty()){
                    button.setEnabled(false);
                    return;
                }
                else {
                    heatIn=Recommendation(age, gender, weight, height, purpose, sportsCoef);
                }

                Bundle bundle=new Bundle();
                Intent intent=new Intent(DietAdviceSelectActivity.this,FoodScoreActivity.class);
                bundle.putDouble(Conditions.CURRENT_HEATIN,heatIn);
                intent.putExtra("energy",bundle);

                startActivity(intent);

            }
        });
    }

    /**
     * 产生相应的ArrayList
     *
     * @return specified ArrayList
     */

    private ArrayList<String> getGenderList() {
        ArrayList<String> gender = new ArrayList<>();
        gender.add("男");
        gender.add("女");
        return gender;
    }

    private ArrayList<String> getAgeList() {
        ArrayList<String> ageList = new ArrayList<>();
        for (int i = 14; i <= 100; ++i) {
            ageList.add(String.valueOf(i));
        }
        return ageList;
    }

    private ArrayList<String> getHeightList() {
        ArrayList<String> HeightList = new ArrayList<>();
        for (int i = 140; i <= 220; ++i) {
            HeightList.add(String.valueOf(i));
        }
        return HeightList;
    }

    private ArrayList<String> getWeightList() {
        ArrayList<String> weightList = new ArrayList<>();
        for (int i = 30; i <= 150; ++i) {
            weightList.add(String.valueOf(i));
        }
        return weightList;
    }

    private ArrayList<String> getPurposeList() {
        ArrayList<String> purpose = new ArrayList<>();
        purpose.add("减脂");
        purpose.add("增肌");
        return purpose;
    }

    private ArrayList<String> getSportCoefList() {
        ArrayList<String> sportCoef = new ArrayList<>();
        sportCoef.add("1.2(不进行或很少进行运动)");
        sportCoef.add("1.375(一周进行1-3次低强度运动(心率低于120))");
        sportCoef.add("1.55(一周进行3-5次中等强度运动(心率低于150))");
        sportCoef.add("1.725(一周进行6-7次强度较大运动)");
        sportCoef.add("1.9(从事非常高强度活动或运动的人群)");
        return sportCoef;
    }

    /**
     * params : 年龄 性别 身高 体重 健身目的
     */

    private double Recommendation(String Age, String Gender, String Height, String Weight, String Purpose, String SportsCoef) {

        Log.d(TAG, "Recommendation: " + "Age:" + Age + ",Gender:" + Gender + ",Height:" + Height + ",Weight:" + Weight + ",Purpose:" + Purpose);

        Log.d(TAG, "Recommendation: " + "Age:" + Age + ",Gender:" + Gender + ",Height:" + Height + ",Weight:" + Weight + ",Purpose:" + Purpose);

        int age = Integer.parseInt(Age);
        int weight = Integer.parseInt(Weight);
        int height = Integer.parseInt(Height);
        double sportsCoef = Double.parseDouble(SportsCoef);

        // step1: BMR
        double BMR;
        switch (Gender) {
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

        // step 3
        double protein = 0;
        double fat = 0;
        double carbohydrate = 0;
        double heatIn = 0;

        switch (Purpose) {
            case "增肌":
                heatIn = normalHeat + 300;
                carbohydrate = 0.6 * heatIn;
                protein = 0.2 * heatIn;
                fat = 0.2 * heatIn;
                break;
            case "减脂":
                double fatCoef;
                if (sportsCoef == 1.2) {
                    fatCoef = 0.9;
                    heatIn = fatCoef * normalHeat;
                    protein = 0.8 * weight;
                    fat = heatIn * 0.3 / 9;
                    carbohydrate = (heatIn - fat * 9 - protein * 4) / 4;

                } else if (sportsCoef == 1.375) {
                    fatCoef = 0.85;
                    heatIn = fatCoef * normalHeat;
                    protein = 1 * weight;
                    fat = heatIn * 0.3 / 9;
                    carbohydrate = (heatIn - fat * 9 - protein * 4) / 4;

                } else if (sportsCoef == 1.55) {
                    fatCoef = 0.8;
                    heatIn = fatCoef * normalHeat;
                    protein = 1.4 * weight;
                    fat = heatIn * 0.3 / 9;
                    carbohydrate = (heatIn - fat * 9 - protein * 4) / 4;

                } else if (sportsCoef == 1.725) {
                    fatCoef = 0.8;
                    heatIn = fatCoef * normalHeat;
                    protein = 1.8 * weight;
                    fat = heatIn * 0.3 / 9;
                    carbohydrate = (heatIn - fat * 9 - protein * 4) / 4;

                } else if (sportsCoef == 1.9) {
                    fatCoef = 0.8;
                    heatIn = fatCoef * normalHeat;
                    protein = 2.2 * weight;
                    fat = heatIn * 0.3 / 9;
                    carbohydrate = (heatIn - fat * 9 - protein * 4) / 4;
                }
                break;
            default:
                break;
        }


        Toast.makeText(this, "protein:" + protein + ",fat:" + fat + ",heatIn:" + heatIn + ",carbohydrate:" + carbohydrate, Toast.LENGTH_LONG).show();

        return heatIn;
    }

}
