package fruitbasket.com.bodyfit.analysis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import java.util.concurrent.locks.Condition;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.MyApplication;
import fruitbasket.com.bodyfit.ui.DataTableActivity;

public class ExerciseAnalysisTask implements Runnable {

    private SingleExerciseAnalysis singleAnalysis;
    private GroupExerciseScore groupAnalysis;
    private Handler handler;
    private boolean isStart = false;
    private double start = 0, end = 0;

    public ExerciseAnalysisTask(SingleExerciseAnalysis analysis, GroupExerciseScore groupAnalysis) {
        this.singleAnalysis = analysis;
        this.groupAnalysis = groupAnalysis;
    }

    public ExerciseAnalysisTask(SingleExerciseAnalysis analysis, GroupExerciseScore groupAnalysis, Handler handler) {
        this(analysis, groupAnalysis);
        this.handler = handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setContext(Context context) {
        singleAnalysis.setContext(context);
    }

    @Override
    public void run() {
        Log.i(this.toString(), "run()");

        singleAnalysis.analysis();
        groupAnalysis.analysis();

        Message message = new Message();
        message.what = Conditions.MESSAGE_EXERCESE_STATUS;
        Bundle data = new Bundle();
        double time = singleAnalysis.getTime();
        if (time != 0) {
            data.putString(Conditions.TIME_TEST, "耗时：" + (time+300) + "ms");
        }

        if (singleAnalysis.getExerciseType() != null) {//判断出运动类型之后传到UI
            data.putString(Conditions.JSON_KEY_EXERCISE_TYPE, singleAnalysis.getExerciseType().toString());
//            data.putString(Conditions.JSON_KEY_EXERCISE_TYPE_MANUAL,singleAnalysis.getExerciseTypeManual().toString());
            data.putDouble(Conditions.DTW_TIME,singleAnalysis.getdtwtime());
//            data.putDouble(Conditions.DTW_TIME_MANUAL,singleAnalysis.getdtwtime_Manual());
            data.putDoubleArray(Conditions.AX_TEST,singleAnalysis.getSelectedDataSet(0));
            data.putDoubleArray(Conditions.AY_TEST,singleAnalysis.getSelectedDataSet(1));
            data.putDoubleArray(Conditions.AZ_TEST,singleAnalysis.getSelectedDataSet(2));
            data.putDoubleArray(Conditions.GX_TEST,singleAnalysis.getSelectedDataSet(3));
            data.putDoubleArray(Conditions.GY_TEST,singleAnalysis.getSelectedDataSet(4));
            data.putDoubleArray(Conditions.GZ_TEST,singleAnalysis.getSelectedDataSet(5));

        }

        if(singleAnalysis.selectedFlag_Manual)
        {
            data.putDoubleArray(Conditions.AX_MANUAL,singleAnalysis.getSelectedDataSet_Manual(0));
            data.putDoubleArray(Conditions.AY_MANUAL,singleAnalysis.getSelectedDataSet_Manual(1));
            data.putDoubleArray(Conditions.AZ_MANUAL,singleAnalysis.getSelectedDataSet_Manual(2));
            data.putDoubleArray(Conditions.GX_MANUAL,singleAnalysis.getSelectedDataSet_Manual(3));
            data.putDoubleArray(Conditions.GY_MANUAL,singleAnalysis.getSelectedDataSet_Manual(4));
            data.putDoubleArray(Conditions.GZ_MANUAL,singleAnalysis.getSelectedDataSet_Manual(5));
            data.putDoubleArray(Conditions.MX_MANUAL,singleAnalysis.getSelectedDataSet_Manual(6));
            data.putDoubleArray(Conditions.MY_MANUAL,singleAnalysis.getSelectedDataSet_Manual(7));
            data.putDoubleArray(Conditions.MZ_MANUAL,singleAnalysis.getSelectedDataSet_Manual(8));
            data.putDoubleArray(Conditions.P1_MANUAL,singleAnalysis.getSelectedDataSet_Manual(9));
            data.putDoubleArray(Conditions.P2_MANUAL,singleAnalysis.getSelectedDataSet_Manual(10));
            data.putDoubleArray(Conditions.P3_MANUAL,singleAnalysis.getSelectedDataSet_Manual(11));
        }


        if (groupAnalysis.getGroupExerciseAssess() != null) {//将一组动作的评分传到UI
            data.putString(Conditions.GROUP_EXERCISE_ASSESS, groupAnalysis.getGroupExerciseAssess());
        }
        data.putDoubleArray(Conditions.REPETITION_SCORE, singleAnalysis.getRepetitionScore());
        data.putDouble(Conditions.SET_SCORE, singleAnalysis.getSetScore());
        data.putInt(Conditions.ACTION_NUM,singleAnalysis.getActionNum());
        data.putDouble(Conditions.DURATION, singleAnalysis.getActionDuration());
        data.putInt(Conditions.DTW_SCORE, singleAnalysis.getSingleDtwScore());
        data.putInt(Conditions.DTW,singleAnalysis.getSingleDTW());


        data.putFloat(Conditions.GROUP_MAX_TIME1,singleAnalysis.getGroupMaxTime1());
        data.putFloat(Conditions.GROUP_MIN_TIME1,singleAnalysis.getGroupMinTime1());
        data.putFloat(Conditions.GROUP_AVE_TIME1,singleAnalysis.getGroupAveTime1());
        data.putFloat(Conditions.GROUP_MAX_DTW1,singleAnalysis.getGroupMaxDtw1());
        data.putFloat(Conditions.GROUP_MIN_DTW1,singleAnalysis.getGroupMinDtw1());
        data.putFloat(Conditions.GROUP_AVE_DTW1,singleAnalysis.getGroupAveDtw1());

        data.putFloat(Conditions.GROUP_MAX_TIME2,singleAnalysis.getGroupMaxTime2());
        data.putFloat(Conditions.GROUP_MIN_TIME2,singleAnalysis.getGroupMinTime2());
        data.putFloat(Conditions.GROUP_AVE_TIME2,singleAnalysis.getGroupAveTime2());
        data.putFloat(Conditions.GROUP_MAX_DTW2,singleAnalysis.getGroupMaxDtw2());
        data.putFloat(Conditions.GROUP_MIN_DTW2,singleAnalysis.getGroupMinDtw2());
        data.putFloat(Conditions.GROUP_AVE_DTW2,singleAnalysis.getGroupAveDtw2());

        data.putFloat(Conditions.GROUP_MAX_TIME3,singleAnalysis.getGroupMaxTime3());
        data.putFloat(Conditions.GROUP_MIN_TIME3,singleAnalysis.getGroupMinTime3());
        data.putFloat(Conditions.GROUP_AVE_TIME3,singleAnalysis.getGroupAveTime3());
        data.putFloat(Conditions.GROUP_MAX_DTW3,singleAnalysis.getGroupMaxDtw3());
        data.putFloat(Conditions.GROUP_MIN_DTW3,singleAnalysis.getGroupMinDtw3());
        data.putFloat(Conditions.GROUP_AVE_DTW3,singleAnalysis.getGroupAveDtw3());




        data.putDouble(Conditions.TOTAL_TIME,singleAnalysis.getTotalTime());

        message.setData(data);
        if (handler != null) {
            //Log.i(this.toString(),"handler!=null");
            handler.sendMessage(message);
        } else {
            Log.e(this.toString(), "handler==null");
        }
    }

}
