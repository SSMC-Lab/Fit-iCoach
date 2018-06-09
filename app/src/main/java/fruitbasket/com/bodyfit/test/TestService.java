package fruitbasket.com.bodyfit.test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.analysis.ExerciseAnalysisTask;
import fruitbasket.com.bodyfit.analysis.GroupExerciseScore;
import fruitbasket.com.bodyfit.analysis.SingleExerciseAnalysis;
import fruitbasket.com.bodyfit.data.DataSet;
import fruitbasket.com.bodyfit.data.DataUnit;

/**
 * @Author Welkinshadow
 * @Date 2017/10/13
 */

public class TestService extends Service {

    private static final String TAG = "TestService";

    private Context context;
    private Handler handler;

    private long itemsNumber = 0;//记录已经读取的数据条目数量
    private int localItemsNumber = 0;//记录已经读取的数据条目数量。但值过大时，它会被清0；
    private double startTime;//记录接收数据的起始时间
    private double localStartTime;//记录接收数据的起始时间，但会定量清0
    private double currentTime;
    private double itemsPreSecond;


    private Bundle bundle = new Bundle();
    private DataUnit dataUnit;
    private int loadSize = 0;
    private DataUnit[] dataUnits = new DataUnit[Conditions.MAX_SAMPLE_NUMBER];//5组数据

    private SingleExerciseAnalysis analysis = new SingleExerciseAnalysis();
    private GroupExerciseScore groupExerciseScore = new GroupExerciseScore();
    private ExecutorService processExecutor = Executors.newSingleThreadExecutor();//创建线程池
    private TestDataProvider testDataProvider;

    @Override
    public IBinder onBind(Intent intent) {
        testDataProvider = new TestDataProvider(this);
        return new MyBinder();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public class MyBinder extends Binder {
        public TestService getService() {
            return TestService.this;
        }
    }

    private class testTask implements Runnable {
        @Override
        public void run() {
            while ((dataUnit = testDataProvider.getDataUnit()) != null) {
                if (loadSize < dataUnits.length - 1) {
                    dataUnits[loadSize] = dataUnit;
                    ++loadSize;
                } else {
                    dataUnits[loadSize] = dataUnit;
                    loadSize = 0;
                    DataSet dataSet = new DataSet(dataUnits);
                    ///这里analysis可能会产生一个处理延迟或处理缺失的问题
                    if (analysis.addToSet(dataSet) == false) {
                        //这里将数据的处理放到一个新的线程中
                        Log.e(TAG, "将数据的处理放到一个新的线程中");
                        ExerciseAnalysisTask exerciseAnalysisTask = new ExerciseAnalysisTask(analysis, groupExerciseScore, handler);
                        exerciseAnalysisTask.setContext(TestService.this);
                        processExecutor.execute(exerciseAnalysisTask);
                    }
                }
            }
            testDataProvider.close();
        }
    }

    public void start() {
        new Thread(new testTask()).start();
    }
}
