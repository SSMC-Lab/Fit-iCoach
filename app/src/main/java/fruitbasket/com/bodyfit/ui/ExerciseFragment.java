package fruitbasket.com.bodyfit.ui;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.R;
import fruitbasket.com.bodyfit.TimeCalculate;
import fruitbasket.com.bodyfit.bluetooth.BluetoothFragment;
import fruitbasket.com.bodyfit.data.DataSet;
import fruitbasket.com.bodyfit.data.StorageData;
import fruitbasket.com.bodyfit.utils.SPUtils;
import win.smartown.android.library.tableLayout.TableAdapter;
import win.smartown.android.library.tableLayout.*;


public class ExerciseFragment extends BluetoothFragment {
    public static final String TAG = "ExerciseFragment";

    private TextView exerciseNumber;    //显示动作次数
    private TextView exerciseType;  //显示运动类型
    private TextView exerciseTypeManual; //显示个人模板运动类型
    private TextView action_grade;
    private TextView exerciseAssess;
    private TextView totalTime_text;
    private LinearLayout infoLayout;
    private TextView time_Text;
    private TextView time_Text_Manual;
    //    private TextView exerciseTotalNumber;   //累计次数
    private ToggleButton toggleButton;  //连接按钮
    private Button selectExercise;
    private Button testButton;
    private TextView timeText;
    private Button selectGrade;
    private ImageView currentActionImg;

    private TextView exerciseTotalTime;
    private TableLayout tableLayout;

    private MaterialDialog.Builder mBuilder;
    private MaterialDialog mMaterialDialog;

    //    private LineChartManager lineChartManager1;
    private LineChartManager lineChartManager2;


    private Button minus_b;
    private Button clear_b;

    private NbButton actionList;
    private RelativeLayout rlContent;
    private Handler handler;
    private Animator animator;
    private Button selectButton;

    //    private LineChart lineCharta;
    private LineChart lineChartg;
    private ToggleButton manualRecordButton; //手动记录按钮

    private TextView textPoint;
    private TextView textPoint_Manual;
    private TextView textView_fileName;
    private boolean hasStorage = false;
    private int count;

    private String type;    //运动类型
    private String type_Manual; //个人模板运动类型
    private double dtw_time; //dtw时间
    private double dtw_time_Manual; //dtw个人模板时间
    private double singleTime = 0;
    private int singleNum, oldNum = 0;    //运动次数
    private static int totalNum = 0;  //累计运动次数
    private static double totalTime = 0;
    private int t1 = 1;
    private int t2 = 1;
    private double assess;
    private int dtw_score;
    private int dtw;
    private float groupMaxTime1, groupMinTime1, groupAveTime1;
    private float groupMaxDtw1, groupMinDtw1, groupAveDtw1;
    private float groupMaxTime2, groupMinTime2, groupAveTime2;
    private float groupMaxDtw2, groupMinDtw2, groupAveDtw2;
    private float groupMaxTime3, groupMinTime3, groupAveTime3;
    private float groupMaxDtw3, groupMinDtw3, groupAveDtw3;
    private int[] num_of_action_array;

    private long startTime, endTime;
    private static List<DataTableActivity.Content> contentList;
    private double[] ax, ay, az, gx, gy, gz;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.layout_exercise, container, false);
        //initialize Views
        initView(view);

        //设置了两个图标的大小
        Drawable drawable = getResources().getDrawable(R.drawable.muscle);
        drawable.setBounds(0, 0, 50, 50);
//        actionList.setCompoundDrawables(drawable,null,null,null);
        drawable = getResources().getDrawable(R.drawable.select);
        drawable.setBounds(0, 0, 50, 50);
//        selectButton.setCompoundDrawables(drawable,null,null,null);

        toggleButton.setOnClickListener(new ToggleClickListener());
        /*manualRecordButton.setOnClickListener(new ToggleClickListener(){
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.manualRecord:
                        if (((ToggleButton) view).isChecked() == true && toggleButton.isChecked()) {
                            Log.e(TAG, "Start Now");
                            SingleExerciseAnalysis.dataBufferManual_isStart = true;
                            hasStorage = false;
                        } else {
                            Log.e(TAG, "stopping Record");
                            count = SingleExerciseAnalysis.count;
                            Log.e(TAG,"count: "+count);
                            SingleExerciseAnalysis.dataBufferManual_isStart = false;
                        }
                        break;
                }
            }
        });
*/
        View.OnClickListener listener = new myOnClickListener();

//        infoLayout.setOnClickListener(listener);
//        selectExercise.setOnClickListener(listener);

//        minus_b.setOnClickListener(listener);
//        clear_b.setOnClickListener(listener);


        rlContent.getBackground().setAlpha(5);
        handler = new Handler();
        actionList.setOnClickListener(listener);
        selectGrade.setOnClickListener(listener);
        SPUtils.put(Conditions.User_grade, "初级");
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTest();
            }
        });

//        lineChartManager1=new LineChartManager(lineCharta);
        lineChartManager2 = new LineChartManager(lineChartg);
        initContent();
        firstRowAsTitle();
        initLineChartContent();
        return view;
    }

    private void storage(int realLen, double[] axSet, double[] aySet, double[] azSet, double[] gxSet, double[] gySet, double[] gzSet,
                         double[] mxSet, double[] mySet, double[] mzSet, double[] p1Set, double[] p2Set, double[] p3Set) {
        StorageData sd = new StorageData(String.valueOf(textView_fileName.getText()));
        try {
            Log.e(TAG, "realLen:" + realLen);
            double[] ax = new double[realLen * 5];
            double[] ay = new double[realLen * 5];
            double[] az = new double[realLen * 5];
            double[] gx = new double[realLen * 5];
            double[] gy = new double[realLen * 5];
            double[] gz = new double[realLen * 5];
            double[] mx = new double[realLen * 5];
            double[] my = new double[realLen * 5];
            double[] mz = new double[realLen * 5];
            double[] p1 = new double[realLen * 5];
            double[] p2 = new double[realLen * 5];
            double[] p3 = new double[realLen * 5];
            System.arraycopy(axSet, 0, ax, 0, realLen * 5);
            System.arraycopy(aySet, 0, ay, 0, realLen * 5);
            System.arraycopy(azSet, 0, az, 0, realLen * 5);
            System.arraycopy(gxSet, 0, gx, 0, realLen * 5);
            System.arraycopy(gySet, 0, gy, 0, realLen * 5);
            System.arraycopy(gzSet, 0, gz, 0, realLen * 5);
            System.arraycopy(mxSet, 0, mx, 0, realLen * 5);
            System.arraycopy(mySet, 0, my, 0, realLen * 5);
            System.arraycopy(mzSet, 0, mz, 0, realLen * 5);
            System.arraycopy(p1Set, 0, p1, 0, realLen * 5);
            System.arraycopy(p2Set, 0, p2, 0, realLen * 5);
            System.arraycopy(p3Set, 0, p3, 0, realLen * 5);
            Log.e(TAG, "axSet: " + axSet.length);
            Log.e(TAG, "mxSet: " + mxSet.length);
            double[] time = new double[realLen * 5];
            sd.outputData(new DataSet(time, ax, ay, az, gx, gy, gz, mx, my, mz, p1, p2, p3));
            Log.d(TAG, "成功存储");
        } catch (Exception e) {
            Log.e(TAG, "存储失败");
        }
    }

    private void initView(View view) {
        context = this.getContext();
        exerciseAssess = (TextView) view.findViewById(R.id.exercise_assess);
        exerciseType = (TextView) view.findViewById(R.id.exercise_type);

//        time_Text = (TextView) view.findViewById(R.id.dtw_time_text);
//        time_Text_Manual = (TextView) view.findViewById(R.id.dtw_time_text_Manual);
        toggleButton = (ToggleButton) view.findViewById(R.id.start_doing);
//        selectExercise = (Button) view.findViewById(R.id.setExercise);
        exerciseNumber = (TextView) view.findViewById(R.id.exercise_num);
//        exerciseTotalNumber = (TextView) view.findViewById(R.id.total_num);
        infoLayout = (LinearLayout) view.findViewById(R.id.info_layout);
        action_grade = (TextView) view.findViewById(R.id.action_grade);
        testButton = (Button) view.findViewById(R.id.start_test);
//        timeText = (TextView) view.findViewById(R.id.time_textview);
//        minus_b=(Button)view.findViewById(R.id.minus);
//        clear_b=(Button)view.findViewById(R.id.clear);
        actionList = (NbButton) view.findViewById(R.id.button_action_list);
//        selectButton=(Button)view.findViewById(R.id.setExercise);
//        lineCharta = (LineChart)view.findViewById(R.id.LineCharta);
        lineChartg = (LineChart) view.findViewById(R.id.LineChartg);
//        manualRecordButton = (ToggleButton)view.findViewById(R.id.manualRecord);
        textPoint = (TextView) view.findViewById(R.id.text_point);
//        textView_fileName = (TextView)view.findViewById(R.id.fileName);
        totalTime_text = (TextView) view.findViewById(R.id.total_time);
        selectGrade = (Button) view.findViewById(R.id.select_grade);
        exerciseTotalTime = (TextView) view.findViewById(R.id.total_num);
        tableLayout = (TableLayout) view.findViewById(R.id.SingleDataTable);
        rlContent = (RelativeLayout) view.findViewById(R.id.rl_content);
        currentActionImg = (ImageView) view.findViewById(R.id.currentActionImg);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
        // animator.cancel();
        // rlContent.getBackground().setAlpha(0);
        actionList.regainBackground();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void updateUI(int what, Bundle bundle) {
        switch (what) {
            case Conditions.MESSAGE_BLUETOOTH_TEST:
                break;

            case Conditions.MESSAGE_EXERCESE_STATUS:
                type = String.valueOf(bundle.getString(Conditions.JSON_KEY_EXERCISE_TYPE));
//                type_Manual = String.valueOf(bundle.getString(Conditions.JSON_KEY_EXERCISE_TYPE_MANUAL));
                dtw_time = bundle.getDouble(Conditions.DTW_TIME);
//                dtw_time_Manual = bundle.getDouble(Conditions.DTW_TIME_MANUAL);
                singleNum = bundle.getInt(Conditions.ACTION_NUM);
                singleTime = bundle.getDouble(Conditions.DURATION);
                assess = bundle.getDouble(Conditions.SET_SCORE);
                dtw_score = bundle.getInt(Conditions.DTW_SCORE);
                dtw = bundle.getInt(Conditions.DTW);
                num_of_action_array = bundle.getIntArray(Conditions.NUM_OF_ACTION_ARRAY);
                String message = bundle.getString(Conditions.TIME_TEST);
                if (message != null && message != "") {
                    timeText.setText(message);
                }
                setExerciseAssess();
                setExerciseType(type);
//                time_Text.setText(String.valueOf(dtw_time));
//                time_Text_Manual.setText(String.valueOf(dtw_time_Manual));
//                setActionNum(singleNum, dtw_score);

                drawLineChart(false, bundle.getDoubleArray(Conditions.AX_TEST), bundle.getDoubleArray(Conditions.AY_TEST), bundle.getDoubleArray(Conditions.AZ_TEST),
                        bundle.getDoubleArray(Conditions.GX_TEST), bundle.getDoubleArray(Conditions.GY_TEST), bundle.getDoubleArray(Conditions.GZ_TEST));

               /* if(!manualRecordButton.isChecked() && SingleExerciseAnalysis.selectedFlag_Manual)
                {
                    drawLineChart(true,bundle.getDoubleArray(Conditions.AX_MANUAL),bundle.getDoubleArray(Conditions.AY_MANUAL),bundle.getDoubleArray(Conditions.AZ_MANUAL),
                            bundle.getDoubleArray(Conditions.GX_MANUAL),bundle.getDoubleArray(Conditions.GY_MANUAL),bundle.getDoubleArray(Conditions.GZ_MANUAL));
                    Log.e(TAG,"inner count"+count);
                    if(!hasStorage) {
                        Log.e(TAG,"inner length"+bundle.getDoubleArray(Conditions.AX_MANUAL).length);
                        storage(count, bundle.getDoubleArray(Conditions.AX_MANUAL),bundle.getDoubleArray(Conditions.AY_MANUAL),bundle.getDoubleArray(Conditions.AZ_MANUAL),
                                bundle.getDoubleArray(Conditions.GX_MANUAL),bundle.getDoubleArray(Conditions.GY_MANUAL),bundle.getDoubleArray(Conditions.GZ_MANUAL),
                                bundle.getDoubleArray(Conditions.MX_MANUAL),bundle.getDoubleArray(Conditions.MY_MANUAL),bundle.getDoubleArray(Conditions.MZ_MANUAL),
                                bundle.getDoubleArray(Conditions.P1_MANUAL),bundle.getDoubleArray(Conditions.P2_MANUAL),bundle.getDoubleArray(Conditions.P3_MANUAL));
                        hasStorage = true;
                    }
                }
*/
                setTablelayoutContent(bundle);
                sendDataToTable();

                setActionNum(singleNum, dtw_score, singleTime);
                TimeCalculate.stopTiming("做完動作到顯示ui的時間");
                break;

            case Conditions.MESSAGE_ERROR_JSON:
                break;

            default:
        }
    }

    private void drawLineChart(boolean isManual, double[] ax, double[] ay, double[] az, double[] gx, double[] gy, double[] gz) {
        if (ax == null || ay == null || az == null || gx == null || gy == null || gz == null)
            return;
        List<Entry> ax_entries = new ArrayList<>();
        List<Entry> ay_entries = new ArrayList<>();
        List<Entry> az_entries = new ArrayList<>();
        List<Entry> gx_entries = new ArrayList<>();
        List<Entry> gy_entries = new ArrayList<>();
        List<Entry> gz_entries = new ArrayList<>();

        for (int i = 0; i < ax.length; i++) {
            ax_entries.add(new Entry(i + 1, (float) ax[i]));
            ay_entries.add(new Entry(i + 1, (float) ay[i]));
            az_entries.add(new Entry(i + 1, (float) az[i]));
            gx_entries.add(new Entry(i + 1, (float) gx[i]));
            gy_entries.add(new Entry(i + 1, (float) gy[i]));
            gz_entries.add(new Entry(i + 1, (float) gz[i]));
        }

        LineDataSet ax_dataSet = new LineDataSet(ax_entries, "ax");
        ax_dataSet.setColor(Color.rgb(193, 255, 193));
        ax_dataSet.setCircleColor(Color.rgb(193, 255, 193));
        ax_dataSet.setDrawCircles(false);
        LineDataSet ay_dataSet = new LineDataSet(ay_entries, "ay");
        ay_dataSet.setColor(Color.rgb(240, 128, 128));
        ay_dataSet.setCircleColor(Color.rgb(240, 128, 128));
        ay_dataSet.setDrawCircles(false);
        LineDataSet az_dataSet = new LineDataSet(az_entries, "az");
        az_dataSet.setColor(Color.rgb(0, 178, 238));
        az_dataSet.setCircleColor(Color.rgb(0, 178, 238));
        az_dataSet.setDrawCircles(false);
        LineDataSet gx_dataSet = new LineDataSet(gx_entries, "gx");
        gx_dataSet.setColor(Color.rgb(46, 139, 87));
        gx_dataSet.setCircleColor(Color.rgb(46, 139, 87));
        gx_dataSet.setDrawCircles(false);
        LineDataSet gy_dataSet = new LineDataSet(gy_entries, "gy");
        gy_dataSet.setColor(Color.rgb(148, 0, 211));
        gy_dataSet.setCircleColor(Color.rgb(148, 0, 211));
        gy_dataSet.setDrawCircles(false);
        LineDataSet gz_dataSet = new LineDataSet(gz_entries, "gz");
        gz_dataSet.setColor(Color.rgb(205, 92, 92));
        gz_dataSet.setCircleColor(Color.rgb(205, 92, 92));
        gz_dataSet.setDrawCircles(false);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(ax_dataSet);
        dataSets.add(ay_dataSet);
        dataSets.add(az_dataSet);
        LineData data = new LineData(dataSets);
        Description d = new Description();
        d.setText("");
        if (!isManual) {
            /*lineCharta.setData(data);
            lineCharta.setDrawMarkers(false);
            lineCharta.setDescription(d);
            lineCharta.invalidate();*/
            textPoint.setText("数据点个数:" + String.valueOf(ax.length));
        }

        dataSets = new ArrayList<>();
        dataSets.add(gx_dataSet);
        dataSets.add(gy_dataSet);
        dataSets.add(gz_dataSet);
        data = new LineData(dataSets);
        if (!isManual) {
            lineChartg.setData(data);
            lineChartg.setDescription(d);
            lineChartg.setDrawMarkers(false);
            lineChartg.invalidate();

//            for(int i = 0;i<ax.length;i++){
//                Log.d(TAG,i+":  "+String.valueOf(ax[i]));
//            }

        }/*else{
            lineChartg_Manual.setData(data);
            lineChartg_Manual.setDescription(d);
            lineChartg_Manual.setDrawMarkers(false);
            lineChartg_Manual.invalidate();
//            for(int i = 0;i<ax.length;i++){
//                Log.d(TAG,i+":  "+String.valueOf(ax[i]));
//            }
        }*/

    }

    private void setGrade() {
        if (oldNum == singleNum) {

        } else {

        }
    }

    private void setActionNum(int num, double dtw_score, double singleTime) {
        //exerciseNumber.setText(num + "");

        Log.i("TAG", "t1=" + t1 + "  t2=" + t2);
        if (oldNum != num) {
            totalNum++;
            exerciseTotalTime.setText(new DecimalFormat("0.00").format(singleTime));
            exerciseNumber.setText(totalNum + "");
            int sc = 1;
            if (totalNum == t1 || totalNum == t2) {
                sc = (int) Math.floor(Math.random() * 2 + 1);
            }
            totalTime += singleTime;
            if (singleTime <= 3)
                Log.i(TAG, "SINGLETIME=" + singleTime + " " + (100 / ((1.0 + Math.pow(Math.E, (-1) * singleTime / 0.4 + 3.75)) * 1.0)));
            else
                Log.i(TAG, "SINGLETIME=" + singleTime + " " + (100 / ((1.0 + Math.pow(Math.E, (-1) * (6 - singleTime) / 0.4 + 3.75)) * 1.0)));
            DecimalFormat df = new DecimalFormat("0.0");
            totalTime_text.setText(df.format(totalTime));
            Log.i(TAG, "SingleTime=" + singleTime + " " + dtw + " " + dtw_score);
            Log.i("TAG", "sc=" + sc);
//           if (sc == 1)
//               action_grade.setText("A" + "");
//           else if (sc == 2)
//               action_grade.setText("B" + "");
//           else if (sc == 3)
//               action_grade.setText("C" + "");
//            else if(sc==4)
//                action_grade.setText("E" + "");
            action_grade.setText((int) dtw_score + "");

            oldNum = num;

        }
    }

    private void setExerciseAssess() {
        switch ((int) assess) {
            case -1:
                exerciseAssess.setText("动作太慢");
                break;
            case 0:
                exerciseAssess.setText("合格");
                break;
            case 2:
                exerciseAssess.setText("不合格");
                break;
            case 1:
                exerciseAssess.setText("动作太快");
                break;
            default:
                exerciseAssess.setText("空");
                break;
        }
    }

    private void setExerciseType(String type) {

        if (type.equals("Alternate_Dumbbell_Curl_1")) {
            exerciseType.setText(Conditions.exercise_1);
            currentActionImg.setImageResource(R.drawable.action1);
        } else if (type.equals("Cable_Crossovers_2")) {
            exerciseType.setText(Conditions.exercise_2);
            currentActionImg.setImageResource(R.drawable.action2);
        } else if (type.equals("Dumbbells_Alternate_Aammer_Curls_3")) {
            exerciseType.setText(Conditions.exercise_3);
            currentActionImg.setImageResource(R.drawable.action3);
        } else if (type.equals("Bent_over_lateral_raise_4")) {
            exerciseType.setText(Conditions.exercise_4);
            currentActionImg.setImageResource(R.drawable.action4);
        } else if (type.equals("Flat_Bench_Barbell_Press_5")) {
            exerciseType.setText(Conditions.exercise_5);
            currentActionImg.setImageResource(R.drawable.action5);
        } else if (type.equals("Flat_Bench_Dumbbell_Flye_6")) {
            exerciseType.setText(Conditions.exercise_6);
            currentActionImg.setImageResource(R.drawable.action6);
        } else if (type.equals("Bent_Over_Lateral_Raise_7")) {
            exerciseType.setText(Conditions.exercise_7);
            currentActionImg.setImageResource(R.drawable.action7);
        } else if (type.equals("Barbell_Bent_Over_Row_8")) {
            exerciseType.setText(Conditions.exercise_8);
            currentActionImg.setImageResource(R.drawable.action8);
        } else if (type.equals("Barbell_Neck_After_Bending_9")) {
            exerciseType.setText(Conditions.exercise_9);
            currentActionImg.setImageResource(R.drawable.action9);
        } else if (type.equals("Machine_Curls_10")) {
            exerciseType.setText(Conditions.exercise_10);
            currentActionImg.setImageResource(R.drawable.action10);
        } else if (type.equals("Pec_Deck_Flye_11")) {
            exerciseType.setText(Conditions.exercise_11);
            currentActionImg.setImageResource(R.drawable.action11);
        } else if (type.equals("Instruments_Made_Thoracic_Mobility_12")) {
            exerciseType.setText(Conditions.exercise_12);
            currentActionImg.setImageResource(R.drawable.action12);
        } else if (type.equals("Reverse_Grip_Pulldown_13")) {
            exerciseType.setText(Conditions.exercise_13);
            currentActionImg.setImageResource(R.drawable.action13);
        } else if (type.equals("One_Arm_Dumbell_Row_14")) {
            exerciseType.setText(Conditions.exercise_14);
            currentActionImg.setImageResource(R.drawable.action14);
        } else if (type.equals("Dumbbell_Is_The_Shoulder_15")) {
            exerciseType.setText(Conditions.exercise_15);
            currentActionImg.setImageResource(R.drawable.action15);
        }
//        else if (type.equals("Birds_Standing_16")) {
//            exerciseType.setText(Conditions.exercise_16);
//        }
//        else if (type.equals("Sitting_On_Shoulder_17")) {
//            exerciseType.setText(Conditions.exercise_17);
//        }
//        else if (type.equals("motion_num_eighteen_18")) {
//            exerciseType.setText(Conditions.exercise_18);
//        }
//        else if (type.equals("motion_num_nineteen_19")) {
//            exerciseType.setText(Conditions.exercise_19);
//        }
//        else if (type.equals("motion_num_twenty_20")) {
//            exerciseType.setText(Conditions.exercise_20);
//        }
        else if (type.equals("TOO_SLOW")) {
            exerciseType.setText(Conditions.too_slow);
        } else if (type.equals("TOO_FAST")) {
            exerciseType.setText(Conditions.too_fast);
        } else
            exerciseType.setText("无动作");
    }


    private class ToggleClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.start_doing:
                    if (((ToggleButton) view).isChecked() == true) {
                        startWork();
                    } else {
                        stopWork();
                    }
                    break;


            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Conditions.EXERCISE_R_CODE && resultCode == 0) {
            Toast.makeText(context, "return from select exercise type", Toast.LENGTH_SHORT).show();
        }
    }

    private class myOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.info_layout:
                    Toast.makeText(context, "click on info_layout", Toast.LENGTH_SHORT).show();
                    break;
                /*case R.id.setExercise:
                    Intent intent = new Intent(context, SelectExeActivity.class);
                    startActivityForResult(intent, Conditions.EXERCISE_R_CODE);
                    break;*/

                /*case R.id.minus:
                    minus();
                    break;

                case R.id.clear:
                    clear();
                    break;*/

                case R.id.button_action_list:
                    actionList.startAnim();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            actionList.gotoNew();
                            final Intent intent = new Intent(getActivity(), ActionListActivity.class);
                            startActivity(intent);
                        }
                    }, 200);
                    break;
                case R.id.select_grade:
                    singleChiose();
                    break;
            }
        }

        private void minus() {
            Log.i(TAG, "minus()");

            if (totalNum > 0) {
                totalNum--;
            }
//            exerciseTotalNumber.setText(totalNum + "");
            exerciseNumber.setText(totalNum + "");
        }

        private void clear() {
            Log.i(TAG, "clear()");
            totalNum = 0;
            // oldNum=0;
            t1 = (int) Math.floor(Math.random() * 20 + 1);
            t2 = (int) Math.floor(Math.random() * 20 + 1);
//            exerciseTotalNumber.setText(totalNum + "");
            exerciseNumber.setText(totalNum + "");
        }
    }

    /**
     * 以下是用于修改该布局中表格的数据
     *
     * @param bundle
     */

    private void setTablelayoutContent(Bundle bundle) {

        if (singleNum <= 10) {
            groupMaxTime1 = bundle.getFloat(Conditions.GROUP_MAX_TIME1);
            groupMinTime1 = bundle.getFloat(Conditions.GROUP_MIN_TIME1);
            groupAveTime1 = bundle.getFloat(Conditions.GROUP_AVE_TIME1);
            groupMaxDtw1 = bundle.getFloat(Conditions.GROUP_MAX_DTW1);
            groupMinDtw1 = bundle.getFloat(Conditions.GROUP_MIN_DTW1);
            groupAveDtw1 = bundle.getFloat(Conditions.GROUP_AVE_DTW1);
            String maxTime = new DecimalFormat("0.00").format(groupMaxTime1);
            String minTime = new DecimalFormat("0.00").format(groupMinTime1);
            String aveTime = new DecimalFormat("0.00").format(groupAveTime1);
            String maxDtw = (int) groupMaxDtw1 + "";
            String minDtw = (int) groupMinDtw1 + "";
            String aveDtw = (int) groupAveDtw1 + "";
            if (aveTime == null) aveTime = "0.00" + "";
            if (groupMinDtw1 >= 99999) minDtw = "0.00" + "";
            if (groupMinTime1 >= 99999) minTime = "0.00" + "";
            contentList.set(1, new DataTableActivity.Content("第一组", maxTime, minTime, aveTime, maxDtw, minDtw, aveDtw));
            firstRowAsTitle();
        } else if (singleNum >= 11 && singleNum <= 20) {
            groupMaxTime2 = bundle.getFloat(Conditions.GROUP_MAX_TIME2);
            groupMinTime2 = bundle.getFloat(Conditions.GROUP_MIN_TIME2);
            groupAveTime2 = bundle.getFloat(Conditions.GROUP_AVE_TIME2);
            groupMaxDtw2 = bundle.getFloat(Conditions.GROUP_MAX_DTW2);
            groupMinDtw2 = bundle.getFloat(Conditions.GROUP_MIN_DTW2);
            groupAveDtw2 = bundle.getFloat(Conditions.GROUP_AVE_DTW2);
            String maxTime = new DecimalFormat("0.00").format(groupMaxTime2);
            String minTime = new DecimalFormat("0.00").format(groupMinTime2);
            String aveTime = new DecimalFormat("0.00").format(groupAveTime2);
            String maxDtw = (int) groupMaxDtw2 + "";
            String minDtw = (int) groupMinDtw2 + "";
            String aveDtw = (int) groupAveDtw2 + "";
            if (aveTime == null) aveTime = "0.00" + "";
            if (groupMinDtw2 >= 99999) minDtw = "0.00" + "";
            if (groupMinTime2 >= 99999) minTime = "0.00" + "";
            contentList.set(1, new DataTableActivity.Content("第二组", maxTime, minTime, aveTime, maxDtw, minDtw, aveDtw));
            firstRowAsTitle();
        } else {
            groupMaxTime3 = bundle.getFloat(Conditions.GROUP_MAX_TIME3);
            groupMinTime3 = bundle.getFloat(Conditions.GROUP_MIN_TIME3);
            groupAveTime3 = bundle.getFloat(Conditions.GROUP_AVE_TIME3);
            groupMaxDtw3 = bundle.getFloat(Conditions.GROUP_MAX_DTW3);
            groupMinDtw3 = bundle.getFloat(Conditions.GROUP_MIN_DTW3);
            groupAveDtw3 = bundle.getFloat(Conditions.GROUP_AVE_DTW3);
            String maxTime = new DecimalFormat("0.00").format(groupMaxTime3);
            String minTime = new DecimalFormat("0.00").format(groupMinTime3);
            String aveTime = new DecimalFormat("0.00").format(groupAveTime3);
            String maxDtw = (int) groupMaxDtw3 + "";
            String minDtw = (int) groupMinDtw3 + "";
            String aveDtw = (int) groupAveDtw3 + "";
            if (aveTime == null) aveTime = "0.00" + "";
            if (groupMinDtw3 >= 99999) minDtw = "0.00" + "";
            if (groupMinTime3 >= 99999) minTime = "0.00" + "";
            contentList.set(1, new DataTableActivity.Content("第三组", maxTime, minTime, aveTime, maxDtw, minDtw, aveDtw));
            firstRowAsTitle();
        }
    }

    private void sendDataToTable() {
        SPUtils.put(Conditions.GROUP_MAX_TIME1, new Float(groupMaxTime1));
        SPUtils.put(Conditions.GROUP_MIN_TIME1, new Float(groupMinTime1));
        SPUtils.put(Conditions.GROUP_AVE_TIME1, new Float(groupAveTime1));
        SPUtils.put(Conditions.GROUP_MAX_DTW1, new Float(groupMaxDtw1));
        SPUtils.put(Conditions.GROUP_MIN_DTW1, new Float(groupMinDtw1));
        SPUtils.put(Conditions.GROUP_AVE_DTW1, new Float(groupAveDtw1));

        SPUtils.put(Conditions.GROUP_MAX_TIME2, new Float(groupMaxTime2));
        SPUtils.put(Conditions.GROUP_MIN_TIME2, new Float(groupMinTime2));
        SPUtils.put(Conditions.GROUP_AVE_TIME2, new Float(groupAveTime2));
        SPUtils.put(Conditions.GROUP_MAX_DTW2, new Float(groupMaxDtw2));
        SPUtils.put(Conditions.GROUP_MIN_DTW2, new Float(groupMinDtw2));
        SPUtils.put(Conditions.GROUP_AVE_DTW2, new Float(groupAveDtw2));

        SPUtils.put(Conditions.GROUP_MAX_TIME3, new Float(groupMaxTime3));
        SPUtils.put(Conditions.GROUP_MIN_TIME3, new Float(groupMinTime3));
        SPUtils.put(Conditions.GROUP_AVE_TIME3, new Float(groupAveTime3));
        SPUtils.put(Conditions.GROUP_MAX_DTW3, new Float(groupMaxDtw3));
        SPUtils.put(Conditions.GROUP_MIN_DTW3, new Float(groupMinDtw3));
        SPUtils.put(Conditions.GROUP_AVE_DTW3, new Float(groupAveDtw3));

        SPUtils.put(Conditions.ACTION_NUM, new Integer(singleNum));

        SPUtils.put(Conditions.exercise_1, new Integer(num_of_action_array[0]));
        SPUtils.put(Conditions.exercise_2, new Integer(num_of_action_array[1]));
        SPUtils.put(Conditions.exercise_3, new Integer(num_of_action_array[2]));
        SPUtils.put(Conditions.exercise_4, new Integer(num_of_action_array[3]));
        SPUtils.put(Conditions.exercise_5, new Integer(num_of_action_array[4]));
        SPUtils.put(Conditions.exercise_6, new Integer(num_of_action_array[5]));
        SPUtils.put(Conditions.exercise_7, new Integer(num_of_action_array[6]));
        SPUtils.put(Conditions.exercise_8, new Integer(num_of_action_array[7]));
        SPUtils.put(Conditions.exercise_9, new Integer(num_of_action_array[8]));
        SPUtils.put(Conditions.exercise_10, new Integer(num_of_action_array[9]));
        SPUtils.put(Conditions.exercise_11, new Integer(num_of_action_array[10]));
        SPUtils.put(Conditions.exercise_12, new Integer(num_of_action_array[11]));
        SPUtils.put(Conditions.exercise_13, new Integer(num_of_action_array[12]));
        SPUtils.put(Conditions.exercise_14, new Integer(num_of_action_array[13]));
        SPUtils.put(Conditions.exercise_15, new Integer(num_of_action_array[14]));

    }

    private void initContent() {
        contentList = new ArrayList<>();
        contentList.add(new DataTableActivity.Content("   ", "最大", "最小", "均值", "最大", "最小", "均值"));
        contentList.add(new DataTableActivity.Content("本组", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00"));
    }

    private void firstRowAsTitle() {
        //fields是表格中要显示的数据对应到Content类中的成员变量名，其定义顺序要与表格中显示的相同
        final String[] fields = {"空", "最大s", "最小s", "均值s", "最大", "最小", "均值"};
        TableAdapter adapter = new TableAdapter() {
            @Override
            public int getColumnCount() {
                return fields.length;
            }

            @Override
            public String[] getColumnContent(int position) {
                int rowCount = contentList.size();
                String contents[] = new String[rowCount];
                try {
                    Field field = DataTableActivity.Content.class.getDeclaredField(fields[position]);
                    field.setAccessible(true);
                    for (int i = 0; i < rowCount; i++) {
                        contents[i] = (String) field.get(contentList.get(i));
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return contents;
            }
        };

        tableLayout.setAdapter(adapter);
    }


    public void initLineChartContent() {

        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            xValues.add((float) i);
        }

        //设置y轴的数据()
        List<List<Float>> y1Values = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<Float> y1Value = new ArrayList<>();
            for (int j = 0; j < 1; j++) {
                y1Value.add(0.0f);
            }
            y1Values.add(y1Value);
        }

        List<List<Float>> y2Values = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<Float> y2Value = new ArrayList<>();
            for (int j = 0; j < 1; j++) {
                y2Value.add(0.0f);
            }
            y2Values.add(y2Value);
        }


        //颜色集合
        List<Integer> colours = new ArrayList<>();
        colours.add(Color.parseColor("#FFAEB9"));
        colours.add(Color.parseColor("#6495ED"));
        colours.add(Color.parseColor("#00CD66"));

        //线的名字集合
        List<String> names1 = new ArrayList<>();
        names1.add("ax");
        names1.add("ay");
        names1.add("az");
        List<String> names2 = new ArrayList<>();
        names2.add("gx");
        names2.add("gy");
        names2.add("gz");

        //创建多条折线的图表
        /*lineChartManager1.showLineChart(xValues, y1Values, names1, colours);
        lineChartManager1.setDescription("加速度");
        lineChartManager1.setYAxis(15, -15, 10);*/

        lineChartManager2.showLineChart(xValues, y2Values, names2, colours);
        lineChartManager2.setYAxis(200, -200, 10);
        lineChartManager2.setDescription("重力加速度");
    }

    private void singleChiose() {
        final String[] grades = {"初级", "中级", "高级"};
        mBuilder = new MaterialDialog.Builder(getContext());
        mBuilder.title("选择健身等级");
        mBuilder.titleGravity(GravityEnum.CENTER);
        mBuilder.titleColor(Color.parseColor("#000000"));
        mBuilder.items(grades);
        mBuilder.autoDismiss(false);
        mBuilder.widgetColor(Color.RED);
        mBuilder.positiveText("确定");
        mBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            }
        });

        mBuilder.itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
            @Override
            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(getContext(), "请选择健身等级", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    selectGrade.setText(grades[which]);
                    String grade = grades[which];
                    SPUtils.put(Conditions.User_grade, grade);
                }
                return false;
            }
        });
        mMaterialDialog = mBuilder.build();
        mMaterialDialog.show();
    }

}
