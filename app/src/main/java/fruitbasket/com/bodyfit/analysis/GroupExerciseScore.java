package fruitbasket.com.bodyfit.analysis;

import android.util.Log;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.TimeCalculate;
import fruitbasket.com.bodyfit.data.DataSet;
import fruitbasket.com.bodyfit.data.DataSetBuffer;
import fruitbasket.com.bodyfit.data.GroupDataSetBuffer;
import fruitbasket.com.bodyfit.data.SelectedDataSet;

/**
 * Created by Administrator on 2016/11/21.
 */
public class GroupExerciseScore implements ExerciseAnalysis {

    public static final String TAG = "GroupExerciseScore";

    private GroupDataSetBuffer groupBuffer; //一整组运动，多个运动，运动+静止+运动+静止+...,第一个动作之前的数据不会储存
    private DataSetBuffer dataBuffer;   //储存运动数据
    private SelectedDataSet selectedData;   //选择需要的维度之后储存
    private DynamicTimeWarping dtw;     //模式匹配算法类对象

    private boolean hasFinishFirst = false;   //是否已经完成第一个动作
    private boolean hasFinishGroupAction = false; //是否已经完成一组动作，若为true，则开始进行一组的分析，否则不进行

    private String exerciseAssess;  //一组运动的评价
    private int currentExerciseType;   //运动类型，范围从1-Conditions.EXERCISE_NUM
    private int exerciseNum = Conditions.EXERCISE_NUM;
    private int selectedAxisData[];


    //运动模板
    private static double[][] ax_mol;
    private static double[][] ay_mol;
    private static double[][] az_mol;
    private static double[][] gx_mol;
    private static double[][] gy_mol;
    private static double[][] gz_mol;
    private static double[][] mx_mol;
    private static double[][] my_mol;
    private static double[][] mz_mol;
    private static double[][] p1_mol;
    private static double[][] p2_mol;
    private static double[][] p3_mol;

    public GroupExerciseScore() {
        groupBuffer = new GroupDataSetBuffer();
        dataBuffer = new DataSetBuffer();
        selectedData = new SelectedDataSet();
        dtw = new DynamicTimeWarping();
    }

    public void analysis() {
        if (hasFinishGroupAction) {
            Log.i(TAG, "finish a group of action,begin to analysis");
            hasFinishGroupAction = false;
            GroupDataSetBuffer temp = groupBuffer;
            groupBuffer.reset();
            //调用组运动的分析函数
        }
    }

    /**
     * 添加数据
     *
     * @param data
     * @return
     */
    public boolean addData(DataSet data) {

        if (data == null || data.size() == 0) {
            Log.e(TAG, "addData()->data=null");
            return false;
        }
        dataBuffer.add(data);
        return true;
    }

    /**
     * 做完一个动作时调用
     */
    public void finishAction() {
        groupBuffer.add(dataBuffer);
        hasFinishFirst = true;
        dataBuffer.clear();
    }

    /**
     * 结束静止（开始做动作）时调用
     */
    public void finishStatic() {
        if (hasFinishFirst == true) {
            groupBuffer.add(dataBuffer);
            dataBuffer.clear();
        }
    }

    /**
     * 从SingleExerciseAnalysis调用，设置模板数据
     */
    public void setModelData(double[][] ax, double[][] ay, double[][] az,
                             double[][] gx, double[][] gy, double[][] gz,
                             double[][] mx, double[][] my, double[][] mz,
                             double[][] p1, double[][] p2, double[][] p3) {
        ax_mol = ax;
        ay_mol = ay;
        az_mol = az;
        gx_mol = gx;
        gy_mol = gy;
        gz_mol = gz;
        mx_mol = mx;
        my_mol = my;
        mz_mol = mz;
        p1_mol = p1;
        p2_mol = p2;
        p3_mol = p3;

    }

    public void calGroupAssess() {

    }


    public String getGroupExerciseAssess() {
        return exerciseAssess;
    }

    /**
     * 设置当前所做的是哪一个运动，从而对这个运动每个维度的的数据进行一一匹配
     *
     * @param type
     */
    public void setCurrentExerciseType(int type) {
        if (type < 1 || type > exerciseNum) {
            Log.e(TAG, "error exercise type");
            return;
        }
        currentExerciseType = type;
    }

    /**
     * 改变动作时调用，调用后才会开始进行组运动的分析
     */
    public void changeAction() {
        hasFinishGroupAction = true ? false : true;
    }


}
