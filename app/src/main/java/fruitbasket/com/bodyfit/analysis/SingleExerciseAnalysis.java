package fruitbasket.com.bodyfit.analysis;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.LogUtils;
import fruitbasket.com.bodyfit.TimeCalculate;
import fruitbasket.com.bodyfit.data.DataSet;
import fruitbasket.com.bodyfit.data.DataSetBuffer;
import fruitbasket.com.bodyfit.data.DataUnitForSQL;
import fruitbasket.com.bodyfit.data.ModelData;
import fruitbasket.com.bodyfit.data.ModelData_Left;
import fruitbasket.com.bodyfit.data.ModelData_Manual;
import fruitbasket.com.bodyfit.data.ModelData_Right;
import fruitbasket.com.bodyfit.data.SelectedDataSet;
import fruitbasket.com.bodyfit.data.StorageData;


public class SingleExerciseAnalysis implements ExerciseAnalysis {
    static {
        System.loadLibrary("jni-dtw");
    }

    private static final String TAG = "SingleExerciseAnalysis";
    private Context context;
    private SelectedDataSet selectedDataSet;
    private boolean selectedFlag = false;
    private DataSetBuffer dataBuffer;  //用于收集存储单个完整动作的数据

    private SelectedDataSet selectedDataSet_Manual;
    public static boolean selectedFlag_Manual = false;
    private DataSetBuffer dataBufferManual; // 用于收集存储用户开始结束动作的数据
    public static boolean dataBufferManual_isStart = false;
    public static int count = 0;

    private SingleExerciseScore score;
    private long itemsNumber = 0;//记录从蓝牙设备中已经读取的数据条目数量

    private StorageData storageData = new StorageData(); // 存放数据到文件里的对象

    private int currentTimes = -1;   //指示用户当前做了多少个健身动作
    private int Nsamples = 0;   //传进来的数组的长度
    private final int span = Conditions.MID_SPAN;     //滑动均值滤波的区间
    private static double Avrthreshold1 = 0;    //平均值阈值1
    private static double Avrthreshold2 = 0;    //平均值阈值2
    private static double Avrthreshold3 = 0;    //平均值阈值3
    private static double Varthreshold1 = 0; //gx陀螺仪方差阈值
    private static double Varthreshold2 = 0; //gy
    private static double Varthreshold3 = 0; //gz
    private static final int MaxSamples = Conditions.MAX_SAMPLES_OF_ACTIONS;     //单个动作中允许的最大samples
    private static final int MinSamples = Conditions.MIN_SAMPLES_OF_ACTIONS;     //单个动作中允许的最小samples
    private int firstrun = 0;     ///
    private double dataBuf[] = new double[span];  ///会随着firstrun的没设置好而发错误
    private ExerciseType exerciseType, exerciseTypeManual, exerciseTypeForBack, exerciseType_group2, exerciseType_group3, exerciseType_group4, exerciseType_fastDTW;
    private double[][] repetitionScore = new double[Conditions.NUM_PRE_EXERCISE][];
    private double singleScore;


    private int tempLength = 60;
    private int tempIndex = 0;

    private int myTempIndex = 0;  //记录已收集window大小
    private int myTempLength = 15; //记录目标window大小
    private double window_gx[] = new double[myTempLength];
    private double window_gy[] = new double[myTempLength];
    private double window_gz[] = new double[myTempLength];
    private double window_ax[] = new double[myTempLength];
    private double window_ay[] = new double[myTempLength];
    private double window_az[] = new double[myTempLength];

    private double gx[] = new double[tempLength];
    private double gy[] = new double[tempLength];
    private double gz[] = new double[tempLength];
    private double ax[] = new double[tempLength];
    private double ay[] = new double[tempLength];
    private double az[] = new double[tempLength];

    private double temp1[] = new double[5];
    private double temp2[] = new double[5];
    private double temp3[] = new double[5];
    private double temp4[] = new double[5];
    private double temp5[] = new double[5];
    private double temp6[] = new double[5];

    private boolean isRecordSegmentStart = false;
    private double start, end, time;
    private double dtw_start, dtw_end, dtw_time;
    private double dtw_start_Manual, dtw_end_Manual, dtw_time_Manual;
    private double dtw_start_forBack, dtw_end_forBack, dtw_time_forBack;
    private double dtw_start_group2, dtw_end_group2, dtw_time_group2;
    private double dtw_start_group3, dtw_end_group3, dtw_time_group3;
    private double dtw_start_group4, dtw_end_group4, dtw_time_group4;
    private double dtw_start_fastdtw, dtw_end_fastdtw, dtw_time_fastdtw;
    private double calStart, calEnd, calTime = 0;
    private boolean isBeginTiming = false;
    private final static double INTERVAL_OF_ONE_ACTION = 500; //单位ms

    private double SingleStartTime = 0, SingleEndTime = 0, SingleDuration = 0;
    private float GroupMaxTime1 = 0, GroupMinTime1 = 99999, GroupSumTime1 = 0;
    private float GroupMaxDtw1 = 0.0f, GroupMinDtw1 = 99999, GroupSumDtw1 = 0.0f;
    private float GroupMaxTime2 = 0, GroupMinTime2 = 99999, GroupSumTime2 = 0;
    private float GroupMaxDtw2 = 0.0f, GroupMinDtw2 = 99999, GroupSumDtw2 = 0.0f;
    private float GroupMaxTime3 = 0, GroupMinTime3 = 99999, GroupSumTime3 = 0;
    private float GroupMaxDtw3 = 0.0f, GroupMinDtw3 = 99999, GroupSumDtw3 = 0.0f;

    private float SingleAveDtw = 0;
    private double totalTime = 0;

    private int EIGHT_OR_FOURTEEN = 0;    //区分8 14
    private int ONE_OR_FIVE = 0;  //区分1 10
    private int NUM_OF_ACTION = 0;
    private int NUM1_OF_ACTION = 0;
    private int NUM2_OF_ACTION = 0;
    private int NUM3_OF_ACTION = 0;
    private int[] NUM_OF_ACTION_ARRAY = new int[15];


    private int SINGLE_DTW_SCORE = 0;
    private int SINGLE_DTW = 0;
    private DynamicTimeWarping dtw;
    private int exerciseTypeNum = -1;
    private int exerciseTypeNum_Manual = -1;
    private int exerciseTypeNum_forBack = -1;
    private int exerciseTypeNum_group2 = -1;
    private int exerciseTypeNum_group3 = -1;
    private int exerciseTypeNum_group4 = -1;
    private int exerciseTypeNum_fastDTW = -1;

    private double[] Every_dtw = {31.23790222, 41.87647333, 15.84228667, 22.27601778, 43.09326889};
    private double b = 1.5; // 阈值参数
    private double[] c = {2, 2}; // 同上
    private double w = 0.1; // EWMA滤波参数
    private double r0 = 0; // MLA方差阈值
    private int once = 0; // 切割二合一的机会
    private double[] dataBefore = new double[6]; // 滤波的第一个数据（每个元素为一轴第一个数据）
    private boolean hasCollectedWindow = false;
    private double startTime = 0;
    private double endTime = 0;
    private double mean_max[] = new double[3]; // max value(mean) of window(gyroscope);
    private double mean_min[] = new double[3];
    private int axisMax_Min_Index[] = new int[6]; // record the idx of window(gyroscope)... eg: [0] for gx_maxIndex,[1] for gx_minIndex and so on;
    private int segmentCount = 0;


    private ExecutorService exec = Executors.newCachedThreadPool();
    private ArrayList<Future<Double>> dtwResults = new ArrayList();
    /*
    1.用户未开始做健身动作：hasBegin=false,hasCollected=false；
    2.用户开始做第一个健身动作，但未做完：hasBegin=true,hasCollected=false;
    3.用户完成了第一个健身动作，但未开始处理刚收集的数据且未开始二个动作：hasBegin=false,hasCollected=true；
    4.用户完成了第一个健身动作，但已开始处理刚收集的数据，而未开始第二个动作：hasBegin=false,hasCollected=false；
     */

    private boolean hasBegin = false; //指示目前是否已经收集完一个完整动作的数据
    private boolean hasCollected = false; //指示目前是否已经收集完一个完整动作的数据
    private boolean isDoing = false;  //指示是否正处于做动作期间
    private boolean hasCollectedStaticData = false;   //指示是否已经在一开始收集了静止时的数据
    //存储模板数据
    private static final int exercise_num = Conditions.EXERCISE_NUM;
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

    private static double[][] ax_mol_Manual;
    private static double[][] ay_mol_Manual;
    private static double[][] az_mol_Manual;
    private static double[][] gx_mol_Manual;
    private static double[][] gy_mol_Manual;
    private static double[][] gz_mol_Manual;
    private static double[][] mx_mol_Manual;
    private static double[][] my_mol_Manual;
    private static double[][] mz_mol_Manual;
    private static double[][] p1_mol_Manual;
    private static double[][] p2_mol_Manual;
    private static double[][] p3_mol_Manual;

    private static double[][] ax_mol_left;
    private static double[][] ay_mol_left;
    private static double[][] az_mol_left;
    private static double[][] gx_mol_left;
    private static double[][] gy_mol_left;
    private static double[][] gz_mol_left;
    private static double[][] mx_mol_left;
    private static double[][] my_mol_left;
    private static double[][] mz_mol_left;
    private static double[][] p1_mol_left;
    private static double[][] p2_mol_left;
    private static double[][] p3_mol_left;

    private static double[][] ax_mol_right;
    private static double[][] ay_mol_right;
    private static double[][] az_mol_right;
    private static double[][] gx_mol_right;
    private static double[][] gy_mol_right;
    private static double[][] gz_mol_right;
    private static double[][] mx_mol_right;
    private static double[][] my_mol_right;
    private static double[][] mz_mol_right;
    private static double[][] p1_mol_right;
    private static double[][] p2_mol_right;
    private static double[][] p3_mol_right;

    private static double[][][] ax_mol_dir = new double[4][][];
    private static double[][][] ay_mol_dir = new double[4][][];
    private static double[][][] az_mol_dir = new double[4][][];
    private static double[][][] gx_mol_dir = new double[4][][];
    private static double[][][] gy_mol_dir = new double[4][][];
    private static double[][][] gz_mol_dir = new double[4][][];
    private static double[][][] mx_mol_dir = new double[4][][];
    private static double[][][] my_mol_dir = new double[4][][];
    private static double[][][] mz_mol_dir = new double[4][][];
    private static double[][][] p1_mol_dir = new double[4][][];
    private static double[][][] p2_mol_dir = new double[4][][];
    private static double[][][] p3_mol_dir = new double[4][][];

    private static int[] num_mol;
    private static int[] num_mol_Manual;
    private static int[] num_mol_Left;
    private static int[] num_mol_Right;
    private static int[][] num_mol_dir = new int[4][];
    //数据维度
    private static final int dimension = 6;
    //存储测试数据
    private double ax_test[] = new double[MaxSamples];
    private double ay_test[] = new double[MaxSamples];
    private double az_test[] = new double[MaxSamples];
    private double gx_test[] = new double[MaxSamples];
    private double gy_test[] = new double[MaxSamples];
    private double gz_test[] = new double[MaxSamples];

    //    private double mx_test[]=new double[MaxSamples];
//    private double my_test[]=new double[MaxSamples];
    //   private double mz_test[] = new double[MaxSamples];
    //    private double p1_test[]=new double[MaxSamples];
//    private double p2_test[]=new double[MaxSamples];
//    private double p3_test[]=new double[MaxSamples];
    private double Dist[] = new double[exercise_num];

    public double getTime() {
        return calTime;
    }

    private void recordStart() {
        if (!isBeginTiming) {
            calStart = System.currentTimeMillis();
            isBeginTiming = true;
        } else {
            Log.w(TAG, "not end");
        }
    }

    private void recordEnd() {
        if (isBeginTiming) {
            calEnd = System.currentTimeMillis();
            isBeginTiming = false;
            calTime = calEnd - calStart;
            Log.d(TAG, " TIME TEST : " + calTime);
        } else {
            Log.w(TAG, "not start");
        }
    }

    //此标志使得程序只读取一次模板数据
    private boolean hasReadModelData = false;
    private boolean hasReadManualModelData = false;
    private boolean hasReadManualModelData_Left = false;
    private boolean hasReadManualModelData_Right = false;
    private boolean hasReadManualModelData_Dir = false;

    public SingleExerciseAnalysis() {
        dataBuffer = new DataSetBuffer();
        dtw = new DynamicTimeWarping();
        score = new SingleExerciseScore();
        dataBufferManual = new DataSetBuffer();
    }

    private void notBeginExercise() {
        Log.i(TAG, "notBeginExercise");
        hasBegin = false;
        hasCollected = false;
        isDoing = false;
    }

    private void doingExercise() {
        Log.i(TAG, "doingExercise");
        hasBegin = true;
        hasCollected = false;
        isDoing = true;
    }

    private void notBeginProcess() {
        Log.i(TAG, "notBeginProcess");
        hasBegin = true;
        hasCollected = true;
        isDoing = false;
    }

    /**
     * 对数据记录集合进行预处理
     */
    private DataSet filter(DataSet dataSet, double[] dataBefore) {
        ///span
        dataSet.setAxSet(ewmaFilter(dataSet.getAxSet(), w, firstrun, dataBefore, 0));
        dataSet.setAySet(ewmaFilter(dataSet.getAySet(), w, firstrun, dataBefore, 1));
        dataSet.setAzSet(ewmaFilter(dataSet.getAzSet(), w, firstrun, dataBefore, 2));
        dataSet.setGxSet(ewmaFilter(dataSet.getGxSet(), w, firstrun, dataBefore, 3));
        dataSet.setGySet(ewmaFilter(dataSet.getGySet(), w, firstrun, dataBefore, 4));
        dataSet.setGzSet(ewmaFilter(dataSet.getGzSet(), w, firstrun, dataBefore, 5));

        dataBefore[0] = dataSet.getLastElement(1);
        dataBefore[1] = dataSet.getLastElement(2);
        dataBefore[2] = dataSet.getLastElement(3);
        dataBefore[3] = dataSet.getLastElement(4);
        dataBefore[4] = dataSet.getLastElement(5);
        dataBefore[5] = dataSet.getLastElement(6);

 /*    原滤噪
        dataSet.setAxSet(filter(dataSet.getAxSet(), span));
        dataSet.setAySet(filter(dataSet.getAySet(), span));
        dataSet.setAzSet(filter(dataSet.getAzSet(), span));
        dataSet.setGxSet(filter(dataSet.getGxSet(), span));
        dataSet.setGySet(filter(dataSet.getGySet(), span));
        dataSet.setGzSet(filter(dataSet.getGzSet(), span));
 */

/*        dataSet.setMxSet(filter(dataSet.getMxSet(), span));
        dataSet.setMySet(filter(dataSet.getMySet(), span));
        dataSet.setMzSet(filter(dataSet.getMzSet(), span));
        dataSet.setP1Set(filter(dataSet.getP1Set(), span));
        dataSet.setP2Set(filter(dataSet.getP2Set(), span));
        dataSet.setP3Set(filter(dataSet.getP3Set(), span));*/
        return dataSet;
    }


    /**
     * 虑噪函数，滑动均值滤波算法
     */
    private double[] filter(double[] sensorDatas, int span) {
        return filter(sensorDatas, span, firstrun);
    }

    private native double[] filter(double[] sensorDatas, int span, int firstrun);

    /**
     * 判断是否符合切割条件；true，开始存数据；false，暂停存数据；
     * 则表明已经收集到一组完整动作的数据
     * index为0，操作不变
     * index非0，overLap window操作
     */
    private boolean isBelongSegments(DataSet dataSet, int index) {
        double avg1, avg2, avg3, result;
        if (index == 0) {
            avg1 = absAvg(dataSet.getGxSet());
            avg2 = absAvg(dataSet.getGySet());
            avg3 = absAvg(dataSet.getGzSet());

            result = Var(MLA(dataSet.getAxSet(), dataSet.getAySet(), dataSet.getAzSet()));

        } else {

            TimeCalculate.startTiming();
            double[] axTemp = Arrays.copyOf(window_ax, window_ax.length + dataSet.getAxSet().length);
            System.arraycopy(dataSet.getAxSet(), 0, axTemp, window_ax.length, dataSet.getAxSet().length);

            for (int i = 0; i < axTemp.length; i++) {
                Log.e(TAG, axTemp[i] + "");
            }
            LogUtils.d(" ");
            for (int i = 0; i < window_ax.length; ++i) {
                LogUtils.e(window_ax[i] + "");
            }
            double[] ayTemp = Arrays.copyOf(window_ay, window_ay.length + dataSet.getAySet().length);
            System.arraycopy(dataSet.getAySet(), 0, ayTemp, window_ay.length, dataSet.getAySet().length);

            double[] azTemp = Arrays.copyOf(window_az, window_az.length + dataSet.getAzSet().length);
            System.arraycopy(dataSet.getAzSet(), 0, azTemp, window_az.length, dataSet.getAzSet().length);

            double[] gxTemp = Arrays.copyOf(window_gx, window_gx.length + dataSet.getGxSet().length);
            System.arraycopy(dataSet.getGxSet(), 0, gxTemp, window_gx.length, dataSet.getGxSet().length);

            double[] gyTemp = Arrays.copyOf(window_gy, window_gy.length + dataSet.getGySet().length);
            System.arraycopy(dataSet.getGySet(), 0, gyTemp, window_gy.length, dataSet.getGySet().length);

            double[] gzTemp = Arrays.copyOf(window_gz, window_gz.length + dataSet.getGzSet().length);
            System.arraycopy(dataSet.getGzSet(), 0, gzTemp, window_gz.length, dataSet.getGzSet().length);

            TimeCalculate.stopTiming("The copy time:");

            avg1 = absAvg(gxTemp);
            avg2 = absAvg(gyTemp);
            avg3 = absAvg(gzTemp);

            result = Var(MLA(axTemp, ayTemp, azTemp));

            //TimeCalculate.startTiming();
            System.arraycopy(axTemp, 5, window_ax, 0, window_ax.length);
            System.arraycopy(ayTemp, 5, window_ay, 0, window_ay.length);
            System.arraycopy(azTemp, 5, window_az, 0, window_az.length);
            System.arraycopy(gxTemp, 5, window_gx, 0, window_gx.length);
            System.arraycopy(gyTemp, 5, window_gy, 0, window_gy.length);
            System.arraycopy(gzTemp, 5, window_gz, 0, window_gz.length);
            //TimeCalculate.stopTiming("Another copy time:");
        }
        Log.i(TAG, "length=" + dataSet.size() + "Avrthreshold1=" + avg1 + "/Avrthreshold2=" + avg2 + "/Avrthreshold3=" + avg3);
        if (avg1 > Avrthreshold1 && avg2 > Avrthreshold2
                || avg1 > Avrthreshold1 && avg3 > Avrthreshold3
                || avg2 > Avrthreshold2 && avg3 > Avrthreshold3 || result > r0) {
            end = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    /**
     * 从exerciseDataSet中筛选有效的数据,选择需要用到的维度的数据
     */
    private boolean dataSelect() {
        Log.i(TAG, "beforeSelect,dataBuffer.capacity=" + dataBuffer.getCapacity());

        //若收到的一个动作的数据小于MinSamples，则丢弃，即清空dataBuffer
        if (dataBuffer.getCapacity() < MinSamples)
            dataBuffer.clear();

        if (dataBuffer.isEmpty() == false) {
            ///筛选指定维度的数据，然后存放在selectedDataSet,
            selectedDataSet = new SelectedDataSet(dataBuffer.toDataSet(), 0, 1, 2, 3, 4, 5);
            dataBuffer.clear();
            Log.i(TAG, "afterSelect,dataBuffer.capacity=" + dataBuffer.getCapacity());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 识别健身动作的类型
     *
     * @return
     */
    private void ExerciseRecognition() {//参数为测试数据
        Log.i(TAG, "ExerciseRecognition()");
        if (selectedDataSet == null) {
            if (dataBuffer.isEmpty() == true) {
                return;
            } else {
                dataSelect();
            }
        }

        selectedFlag = true;

        ax_test = selectedDataSet.getDataByIndex(0);
        ay_test = selectedDataSet.getDataByIndex(1);
        az_test = selectedDataSet.getDataByIndex(2);
        gx_test = selectedDataSet.getDataByIndex(3);
        gy_test = selectedDataSet.getDataByIndex(4);
        gz_test = selectedDataSet.getDataByIndex(5);

        /*for(int i=0;i<ax_test.length;i++)
        {
            Log.e(TAG, "ax_test"+ax_test[i]+"/ay_test"+ay_test[i]+"/az_test"+az_test[i]+"/gx_test"+gx_test[i]+"/gy_test"+gy_test[i]+"/gz_test"+gz_test[i]);
        }*/
        if (hasReadModelData == false) {
            if (loadModelData() == true) {
                hasReadModelData = true;
            } else {
                Log.e(TAG, "load modelData failure");
                return;
            }
        }

        if (hasReadManualModelData == false) {
            if (loadModelData_Manual() == true) {
                hasReadManualModelData = true;
            } else {
                Log.e(TAG, "load modelData_Manual failure");
                return;
            }
        }

        if (hasReadManualModelData_Left == false) {
            if (loadModelData_Left() == true) {
                hasReadManualModelData_Left = true;
            } else {
                Log.e(TAG, "load modelData_Manual failure");
                return;
            }
        }

        if (hasReadManualModelData_Right == false) {
            if (loadModelData_Right() == true) {
                hasReadManualModelData_Right = true;
            } else {
                Log.e(TAG, "load modelData_Manual failure");
                return;
            }
        }


        Log.d("testjni", ax_mol[0][0] + "");
        /*for(int i=0;i<ax_test.length && i<ax_mol[0].length;i++){
            Log.i(TAG,"ax_test[i]="+ax_test[i]+" ay_test[i]="+ay_test[i]+" az_test[i]="+az_test[i]);
            Log.e(TAG,"ax_mol[i]="+ax_mol[0][i]+" ay_mol[i]="+ay_mol[0][i]+" az_mol[i]="+az_mol[0][i]);
        }*/
        int p[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        double minDis1 = 10000000.0, minDis2 = 10000000.0, minDis3 = 10000000.0, minDis4 = 10000000.0, minDis5 = 10000000.0, minDis6 = 10000000.0, minDis7 = 10000000.0;//记录Dist最小的动作标号
        int minIndex1 = 1, minIndex2 = 1, minIndex3 = 1, minIndex4 = 1, minIndex5 = 1, minIndex6 = 1, minIndex7 = 1;
        dtw_start = System.currentTimeMillis();
        //recordStart();

        int axis = chooseAxis();
        Log.i(TAG, "ExerciseRecognition: axis" + axis);
        int idx = rms_MaxToMin(axis);
        Log.i(TAG, "ExerciseRecognition: idx" + idx);
        if (idx == 0) {
            dtw_time_forBack = 0;
            dtw_time = 0;
            dtw_time_group3 = 0;
            dtw_time_group4 = 0;
            dtw_time_group2 = 0;
            dtw_time_Manual = 0;
            return;
        }

        //教练模板

       /* dtw_start = System.currentTimeMillis();
        //recordStart();
        for (int i = 0; i < exercise_num; i++) {
            int num = num_mol[i];
            Dist[i] = 0;
            dtwResults.clear();
            Log.i(TAG, "num=" + num);

            dtwResults.add(exec.submit(new DTWTask(dtw, ax_mol[i], num, ax_test)));
            dtwResults.add(exec.submit(new DTWTask(dtw, ay_mol[i], num, ay_test)));
            dtwResults.add(exec.submit(new DTWTask(dtw, az_mol[i], num, az_test)));
            dtwResults.add(exec.submit(new DTWTask(dtw, gx_mol[i], num, gx_test)));
            dtwResults.add(exec.submit(new DTWTask(dtw, gy_mol[i], num, gy_test)));
            dtwResults.add(exec.submit(new DTWTask(dtw, gz_mol[i], num, gz_test)));

            for (int j = 0; j < dimension; j++) {
                try {
                    Dist[i] += dtwResults.get(j).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            Log.i(TAG, "Coach Dist[" + (i + 1) + "]=" + Dist[i]);
            //同时找出两个最小的dtw值，并且记录下对应的动作
            if (Dist[i] < minDis1) {
                //   minIndex2=minIndex1;
                //  minDis2=minDis1;
                minIndex1 = i + 1;//从minIndex.txt文件中读取的数据与测试数据最可能匹配上
                minDis1 = Dist[i];
            }
        }
        dtw_end = System.currentTimeMillis();
        dtw_time = dtw_end - dtw_start;
        Log.i(TAG, "dtw_Coach:"+dtw_time);*/


        //教练模板 with fastdtw
/*
        dtw_start_fastdtw = System.currentTimeMillis();
        //recordStart();
        for (int i = 0; i < exercise_num; i++) {
            int num = num_mol[i];
            Dist[i] = 0;
            dtwResults.clear();
            Log.i(TAG, "num=" + num);

            dtwResults.add(exec.submit(new FastDTWTask(dtw, ax_mol[i], num, ax_test)));
            dtwResults.add(exec.submit(new FastDTWTask(dtw, ay_mol[i], num, ay_test)));
            dtwResults.add(exec.submit(new FastDTWTask(dtw, az_mol[i], num, az_test)));
            dtwResults.add(exec.submit(new FastDTWTask(dtw, gx_mol[i], num, gx_test)));
            dtwResults.add(exec.submit(new FastDTWTask(dtw, gy_mol[i], num, gy_test)));
            dtwResults.add(exec.submit(new FastDTWTask(dtw, gz_mol[i], num, gz_test)));

            for (int j = 0; j < dimension; j++) {
                try {
                    Dist[i] += dtwResults.get(j).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            Log.i(TAG, "fastDTW Dist[" + (i + 1) + "]=" + Dist[i]);
            //同时找出两个最小的dtw值，并且记录下对应的动作
            if (Dist[i] < minDis7) {
                //   minIndex2=minIndex1;
                //  minDis2=minDis1;
                minIndex7 = i + 1;//从minIndex.txt文件中读取的数据与测试数据最可能匹配上
                minDis7 = Dist[i];
            }
        }
        dtw_end_fastdtw = System.currentTimeMillis();
        dtw_time_fastdtw = dtw_end_fastdtw - dtw_start_fastdtw;
        Log.i(TAG, "fastDTW_time:"+dtw_time_fastdtw);*/


        //去程回程
        dtw_start_forBack = System.currentTimeMillis();
        //recordStart();
        for (int i = 0; i < exercise_num; i++) {
            int num = num_mol_Left[i];
            Dist[i] = 0;
            dtwResults.clear();
            Log.i(TAG, "num=" + num);

            double[] ax_test_beta = new double[idx * 5];
            double[] ay_test_beta = new double[idx * 5];
            double[] az_test_beta = new double[idx * 5];
            double[] gx_test_beta = new double[idx * 5];
            double[] gy_test_beta = new double[idx * 5];
            double[] gz_test_beta = new double[idx * 5];
            System.arraycopy(ax_test, 0, ax_test_beta, 0, idx * 5);
            System.arraycopy(ay_test, 0, ay_test_beta, 0, idx * 5);
            System.arraycopy(az_test, 0, az_test_beta, 0, idx * 5);
            System.arraycopy(gx_test, 0, gx_test_beta, 0, idx * 5);
            System.arraycopy(gy_test, 0, gy_test_beta, 0, idx * 5);
            System.arraycopy(gz_test, 0, gz_test_beta, 0, idx * 5);

            dtwResults.add(exec.submit(new DTWTask(dtw, ax_mol_left[i], num, ax_test_beta)));
            dtwResults.add(exec.submit(new DTWTask(dtw, ay_mol_left[i], num, ay_test_beta)));
            dtwResults.add(exec.submit(new DTWTask(dtw, az_mol_left[i], num, az_test_beta)));
            dtwResults.add(exec.submit(new DTWTask(dtw, gx_mol_left[i], num, gx_test_beta)));
            dtwResults.add(exec.submit(new DTWTask(dtw, gy_mol_left[i], num, gy_test_beta)));
            dtwResults.add(exec.submit(new DTWTask(dtw, gz_mol_left[i], num, gz_test_beta)));

            double[] ax_test_betar = new double[ax_test.length - idx * 5];
            double[] ay_test_betar = new double[ax_test.length - idx * 5];
            double[] az_test_betar = new double[ax_test.length - idx * 5];
            double[] gx_test_betar = new double[ax_test.length - idx * 5];
            double[] gy_test_betar = new double[ax_test.length - idx * 5];
            double[] gz_test_betar = new double[ax_test.length - idx * 5];
            System.arraycopy(ax_test, idx * 5, ax_test_betar, 0, ax_test.length - idx * 5);
            System.arraycopy(ay_test, idx * 5, ay_test_betar, 0, ay_test.length - idx * 5);
            System.arraycopy(az_test, idx * 5, az_test_betar, 0, az_test.length - idx * 5);
            System.arraycopy(gx_test, idx * 5, gx_test_betar, 0, gx_test.length - idx * 5);
            System.arraycopy(gy_test, idx * 5, gy_test_betar, 0, gy_test.length - idx * 5);
            System.arraycopy(gz_test, idx * 5, gz_test_betar, 0, gz_test.length - idx * 5);

            num = num_mol_Right[i];
            dtwResults.add(exec.submit(new DTWTask(dtw, ax_mol_right[i], num, ax_test_betar)));
            dtwResults.add(exec.submit(new DTWTask(dtw, ay_mol_right[i], num, ay_test_betar)));
            dtwResults.add(exec.submit(new DTWTask(dtw, az_mol_right[i], num, az_test_betar)));
            dtwResults.add(exec.submit(new DTWTask(dtw, gx_mol_right[i], num, gx_test_betar)));
            dtwResults.add(exec.submit(new DTWTask(dtw, gy_mol_right[i], num, gy_test_betar)));
            dtwResults.add(exec.submit(new DTWTask(dtw, gz_mol_right[i], num, gz_test_betar)));

            for (int j = 0; j < dimension; j++) {
                try {
                    Dist[i] += dtwResults.get(j).get();
                    Dist[i] += dtwResults.get(j + 6).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            SingleAveDtw += Dist[i];
            Log.i(TAG, "Dist[" + (i + 1) + "]=" + Dist[i]);
            //同时找出两个最小的dtw值，并且记录下对应的动作
            if (Dist[i] < minDis2) {
                //   minIndex2=minIndex1;
                //  minDis2=minDis1;
                minIndex2 = i + 1;//从minIndex.txt文件中读取的数据与测试数据最可能匹配上
                minDis2 = Dist[i];
            }
            /*else if(Dist[i]>minDis1 && Dist[i]<minDis2){
              minIndex2=i+1;
              minDis2=Dist[i];
          }*/
        }
        //recordEnd();

        dtw_end_forBack = System.currentTimeMillis();
        dtw_time_forBack = dtw_end_forBack - dtw_start_forBack;
        Log.i(TAG, "dtw_forback=" + dtw_time_forBack);

        // 个人模板

        /*dtw_start_Manual = System.currentTimeMillis();
        //recordStart();
        for (int i = 0; i < exercise_num; i++) {
            int num = num_mol_Manual[i];
            Dist[i] = 0;
            dtwResults.clear();
            Log.i(TAG, "num=" + num);

            dtwResults.add(exec.submit(new DTWTask(dtw, ax_mol_Manual[i], num, ax_test)));
            dtwResults.add(exec.submit(new DTWTask(dtw, ay_mol_Manual[i], num, ay_test)));
            dtwResults.add(exec.submit(new DTWTask(dtw, az_mol_Manual[i], num, az_test)));
            dtwResults.add(exec.submit(new DTWTask(dtw, gx_mol_Manual[i], num, gx_test)));
            dtwResults.add(exec.submit(new DTWTask(dtw, gy_mol_Manual[i], num, gy_test)));
            dtwResults.add(exec.submit(new DTWTask(dtw, gz_mol_Manual[i], num, gz_test)));

            for (int j = 0; j < dimension; j++) {
                try {
                    Dist[i] += dtwResults.get(j).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            Log.i(TAG, "Dist[" + (i + 1) + "]=" + Dist[i]);
            //同时找出两个最小的dtw值，并且记录下对应的动作
            if (Dist[i] < minDis3) {
                //   minIndex2=minIndex1;
                //  minDis2=minDis1;
                minIndex3 = i + 1;//从minIndex.txt文件中读取的数据与测试数据最可能匹配上
                minDis3 = Dist[i];
            }
        }
        dtw_end_Manual = System.currentTimeMillis();
        dtw_time_Manual = dtw_end_Manual - dtw_start_Manual;
        Log.i(TAG, "dtw_Manual:"+dtw_time_Manual);*/


        double TimeStart = System.currentTimeMillis();
        exerciseTypeNum = p[minIndex2 - 1];
        setExerciseType(p[minIndex2 - 1]);
        getSingleDtwScore(minDis2, minIndex2 - 1);

        setActionNum();

        /*exerciseTypeNum_forBack = p[minIndex2 - 1];
        setExerciseTypeForBack(p[minIndex2 - 1]);

        exerciseTypeNum_Manual = p[minIndex3 - 1];
        setExerciseType_Manual(p[minIndex3 - 1]);*/

        /*exerciseTypeNum_group2 = p[minIndex4 - 1];
        setExerciseType_group2(p[minIndex4 - 1]);

        exerciseTypeNum_group3 = p[minIndex5 - 1];
        setExerciseType_group3(p[minIndex5 - 1]);

        exerciseTypeNum_group4 = p[minIndex6 - 1];
        setExerciseType_group4(p[minIndex6 - 1]);*/

        /*exerciseTypeNum_fastDTW = p[minIndex7 - 1];
        setExerciseType_fastDTW(p[minIndex7 - 1]);*/
        double TimeEnd = System.currentTimeMillis();

        SingleAveDtw /= 15;
        OutputData();
        storageData.outputType(exerciseTypeNum);
        storageData.outputDTW(minDis2);
        if (minDis1 < 1100)
            SINGLE_DTW_SCORE = 0;
        else
            SINGLE_DTW_SCORE = (int) (100 / ((1 + Math.pow(Math.E, minDis2 / (2000 * 1.0) - 5.0)) * 1.0));
        Log.i(TAG, "SINGLE_DTW_SCORE=" + SINGLE_DTW_SCORE + " dtw=" + minDis2 + " " + (100 / ((1 + Math.pow(Math.E, minDis2 / (2000 * 0.1) - 5.0)) * 1.0)));
        SINGLE_DTW = (int) minDis2;
        Log.i(TAG, "DTW==" + minDis2);
        if (NUM_OF_ACTION <= 10) {
            if (minDis2 > GroupMaxDtw1)
                GroupMaxDtw1 = (float) minDis2;
            if (minDis2 < GroupMinDtw1)
                GroupMinDtw1 = (float) minDis2;
            GroupSumDtw1 += minDis2;
            NUM1_OF_ACTION++;
        } else if (NUM_OF_ACTION >= 11 && NUM_OF_ACTION <= 20) {
            if (minDis2 > GroupMaxDtw2)
                GroupMaxDtw2 = (float) minDis2;
            if (minDis2 < GroupMinDtw2)
                GroupMinDtw2 = (float) minDis2;
            GroupSumDtw2 += minDis2;
            NUM2_OF_ACTION++;
        } else if (NUM_OF_ACTION >= 20) {
            if (minDis2 > GroupMaxDtw3)
                GroupMaxDtw3 = (float) minDis2;
            if (minDis2 < GroupMinDtw3)
                GroupMinDtw3 = (float) minDis2;
            GroupSumDtw3 += minDis2;
            NUM3_OF_ACTION++;
        }

        Log.e(TAG, "ExerciseRecognition,ExerciseType=" + minIndex1 + " minDis=" + minDis1);
    }

    /**
     * 计算单个动作的评分
     */
    private void repetitionScore() {
        if (selectedDataSet != null && exerciseTypeNum > 0 && exerciseTypeNum <= exercise_num) {
        } else {
            Log.e(TAG, "repetitionScore()->selectedDataSet=null");
        }

        ///这里需根据selectedDataSet填充算法
        //repetitionScore[currentTimes];
    }

    /**
     * 计算一组动作的评分
     */
    private double[] setScore() {
        if (selectedDataSet == null) {
            if (dataBuffer.isEmpty() == true) {
                return null;
            } else {
                dataSelect();
            }
        }

        ///这里需根据selectedDataSet填充算法
        return null;
    }

    /**
     * 增加数据记录集合到单个完整的动作数据记录集合中
     * 如果动作结束，返回false，如果动作没有结束返回true
     */
    public boolean addToSet(DataSet dataSet) {

        filter(dataSet, dataBefore); //虑噪

        if (dataBufferManual_isStart) {
            selectedFlag_Manual = false;
            dataBufferManual.add(dataSet);
            count++;
        } else {
            Log.d(TAG, "do not addToManual");
            if (!dataBufferManual.isEmpty()) {
                selectedDataSet_Manual = new SelectedDataSet(dataBufferManual.toDataSet(), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
                selectedFlag_Manual = true;
                Log.e(TAG, "count : " + count);
                count = 0;
            }
            dataBufferManual.clear();
        }

        itemsNumber += dataSet.size();
        //若dataBuffer太长，则说明不是一个标准动作，所以清空
        if (dataBuffer.getCapacity() > MaxSamples) {
            dataBuffer.clear();
            reset_Max_Min();
            return true;
        }

        if (!hasCollected) {
            start = System.currentTimeMillis();
            //filter(dataSet,dataBefore);    //虑噪
            end = System.currentTimeMillis();
            time = end - start;
            Log.i(TAG, "filter=" + time);


            //收集一开始的50组数据作为阈值
            if (!hasCollectedStaticData) {
                collectStaticData(dataSet);
                return true;
            }

            if (!hasCollectedWindow) {
                collectWindowData(dataSet);
                return true;
            }

            //判断是否符合切割条件，若符合，则存数据，否则不断覆盖数据
            if (isBelongSegments(dataSet, 1) == true) {

                if (!isRecordSegmentStart) {//如果是第一次符合切割条件，记录第一个切割点
                    isRecordSegmentStart = true;
                    reset_Max_Min();
                    double time[] = dataSet.getTime();
                    SingleStartTime = time[0];
                }
                dataBuffer.add(dataSet);
                choose_Max_Min(dataSet);
                doingExercise();
//                once = 1;
                return true;
            } else if (hasBegin == false) {//做第一个动作之前会到这
                reset_Max_Min();
                notBeginExercise();
                return false;
            } else {//每一个动作结束之后会到这
                notBeginProcess();
                isRecordSegmentStart = false;
                double time[] = dataSet.getTime();
                SingleEndTime = time[0];
                Log.i(TAG, "segment end in [" + (itemsNumber - 50) + "," + itemsNumber + "]");
                return false;
            }
        } else {
//            notBeginProcess();
            return false;
        }
    }

    private void OutputData() {  //将时间存到本地
        storageData.outputBeginTime(SingleStartTime);
        storageData.outputEndTime(SingleEndTime);
        Log.i(TAG, "Time=" + SingleStartTime + " " + SingleEndTime);
        SingleDuration = SingleEndTime - SingleStartTime;
        totalTime = totalTime + SingleDuration;
        storageData.outputDuration(SingleDuration);
        Log.i(TAG, "single==" + NUM_OF_ACTION);
        if (NUM_OF_ACTION <= 10) {
            if (SingleDuration > GroupMaxTime1)
                GroupMaxTime1 = (float) SingleDuration;
            if (SingleDuration < GroupMinTime1)
                GroupMinTime1 = (float) SingleDuration;
            GroupSumTime1 += SingleDuration;
        } else if (NUM_OF_ACTION >= 11 && NUM_OF_ACTION <= 20) {
            Log.i(TAG, "single==" + GroupMaxTime2 + "");
            if (SingleDuration > GroupMaxTime2)
                GroupMaxTime2 = (float) SingleDuration;
            if (SingleDuration < GroupMinTime2)
                GroupMinTime2 = (float) SingleDuration;
            GroupSumTime2 += SingleDuration;
        } else if (NUM_OF_ACTION >= 20) {
            if (SingleDuration > GroupMaxTime3)
                GroupMaxTime3 = (float) SingleDuration;
            if (SingleDuration < GroupMinTime3)
                GroupMinTime3 = (float) SingleDuration;
            GroupSumTime3 += SingleDuration;
        }

    }

    @Override
    public void analysis() {
        ///这里会存在线程不安全问题
        currentTimes++;
        if (currentTimes >= Conditions.NUM_PRE_EXERCISE - 1) {
            ///给出相应的处理
            currentTimes = 0;
        }
        if (hasCollected) {
            if (dataSelect()) {
                if ((singleScore = ExerciseSpeed()) == 0) {//速度正常
                    startTime = System.currentTimeMillis();
                    ExerciseRecognition();
                    repetitionScore();
                    setScore();
                    notBeginExercise();
                    endTime = System.currentTimeMillis();
                    Log.d(TAG, "AfterRecTotal = " + (endTime - startTime));
                } else if ((singleScore = ExerciseSpeed()) == -1) {//速度太慢
                    setExerciseType(16);
                    setExerciseType_Manual(16);
                    setExerciseTypeForBack(16);
                    /*setExerciseType_group2(16);
                    setExerciseType_group3(16);
                    setExerciseType_group4(16);*/
                } else if ((singleScore = ExerciseSpeed()) == 1) {//速度太快
                    setExerciseType(17);
                    setExerciseType_Manual(17);
                    setExerciseTypeForBack(17);
                    /*setExerciseType_group2(17);
                    setExerciseType_group3(17);
                    setExerciseType_group4(17);*/
                }
//                ExerciseRecognition();
//                repetitionScore();
//                setScore();
//                notBeginExercise();

            } else {
                notBeginExercise();
            }
        }
        recordEnd();
    }

    /**
     * 返回值代表运动速度是否正常，-1太慢，0正常，1太快
     */
    private int ExerciseSpeed() {
        double[] ax = selectedDataSet.getDataByIndex(0);
        int maxSpeed = ax.length;
        if (maxSpeed > Conditions.MAX_SAMPLES_OF_ACTIONS)
            return -1;
        else if (maxSpeed <= Conditions.MIN_SAMPLES_OF_ACTIONS)
            return 1;
        else
            return 0;
    }

    /**
     * 重置最大值最小值
     */
    private void reset_Max_Min() {
        mean_max = new double[3]; // max value(mean) of window(gyroscope)
        mean_min = new double[3];
        axisMax_Min_Index = new int[6];
        segmentCount = 0;
    }

    /**
     * 判断最大值最小值
     */
    private void choose_Max_Min(DataSet dataSet) {
        double[] gx = dataSet.getGxSet();
        double[] gy = dataSet.getGySet();
        double[] gz = dataSet.getGzSet();
        double mean_gx = mean(gx);
        double mean_gy = mean(gy);
        double mean_gz = mean(gz);
        update_Max_Min(mean_gx, mean_gy, mean_gz);
    }

    private void update_Max_Min(double mean_gx, double mean_gy, double mean_gz) {
        if (mean_gx < mean_min[0]) {
            mean_min[0] = mean_gx;
            axisMax_Min_Index[1] = segmentCount;
        }
        if (mean_gx > mean_max[0]) {
            mean_max[0] = mean_gx;
            axisMax_Min_Index[0] = segmentCount;
        }
        if (mean_gy < mean_min[1]) {
            mean_min[1] = mean_gy;
            axisMax_Min_Index[3] = segmentCount;
        }
        if (mean_gy > mean_max[1]) {
            mean_max[1] = mean_gy;
            axisMax_Min_Index[2] = segmentCount;
        }
        if (mean_gz < mean_min[2]) {
            mean_min[2] = mean_gz;
            axisMax_Min_Index[5] = segmentCount;
        }
        if (mean_gz > mean_max[2]) {
            mean_max[2] = mean_gz;
            axisMax_Min_Index[4] = segmentCount;
        }
        segmentCount++;
    }

    /**
     * 选出轴
     */
    private int chooseAxis() {
        double gxDiff = mean_max[0] - mean_min[0];
        double gyDiff = mean_max[1] - mean_min[1];
        double gzDiff = mean_max[2] - mean_min[2];
        if (gxDiff > gyDiff) {
            if (gyDiff > gzDiff) {
                return 1;
            } else {
                if (gxDiff > gzDiff) {
                    return 1;
                } else {
                    return 3;
                }
            }
        } else {
            if (gxDiff > gzDiff) {
                return 2;
            } else {
                if (gyDiff > gzDiff) {
                    return 2;
                } else {
                    return 3;
                }
            }
        }
    }

    /**
     * 计算某一轴的rms最小位置
     */
    private int rms_MaxToMin(int axis) {
        int left, right;
        double[] g_rms;
        switch (axis) {
            case 1:
                left = axisMax_Min_Index[0] > axisMax_Min_Index[1] ? axisMax_Min_Index[1] : axisMax_Min_Index[0];
                Log.i(TAG, "rms_MaxToMin: left" + left);
                right = left == axisMax_Min_Index[0] ? axisMax_Min_Index[1] : axisMax_Min_Index[0];
                Log.i(TAG, "rms_MaxToMin: right" + right);
                g_rms = new double[(right - left + 1) * 5];
                System.arraycopy(gx_test, left * 5, g_rms, 0, (right - left + 1) * 5);
                Log.i(TAG, "rms_MaxToMin: right-left" + (right - left));
                return rms(g_rms) + left;
            case 2:
                left = axisMax_Min_Index[2] > axisMax_Min_Index[3] ? axisMax_Min_Index[3] : axisMax_Min_Index[2];
                Log.i(TAG, "rms_MaxToMin: left" + left);
                right = left == axisMax_Min_Index[2] ? axisMax_Min_Index[3] : axisMax_Min_Index[2];
                Log.i(TAG, "rms_MaxToMin: right" + right);
                g_rms = new double[(right - left + 1) * 5];
                System.arraycopy(gy_test, left * 5, g_rms, 0, (right - left + 1) * 5);
                Log.i(TAG, "rms_MaxToMin: right-left" + (right - left));
                return rms(g_rms) + left;
            case 3:
                left = axisMax_Min_Index[4] > axisMax_Min_Index[5] ? axisMax_Min_Index[5] : axisMax_Min_Index[4];
                Log.i(TAG, "rms_MaxToMin: left" + left);
                right = left == axisMax_Min_Index[4] ? axisMax_Min_Index[5] : axisMax_Min_Index[4];
                Log.i(TAG, "rms_MaxToMin: right" + right);
                g_rms = new double[(right - left + 1) * 5];
                System.arraycopy(gz_test, left * 5, g_rms, 0, (right - left + 1) * 5);
                Log.i(TAG, "rms_MaxToMin: right-left" + (right - left));
                return rms(g_rms) + left;
        }
        return -1;
    }

    /**
     * 获取一个数组中的最大值
     */
    private int getMaxIndex(double t[]) {
        int i, len, maxIndex;
        double max;
        len = t.length;
        max = abs(t[0]);
        maxIndex = 0;
        for (i = 0; i < len; i++) {
            if (abs(t[i]) > max) {
                max = abs(t[i]);
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * 返回教练动作的类型
     */
    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    /**
     * 返回教练的dtw时间
     */
    public double getdtwtime() {
        return dtw_time;
    }

    /**
     * 返回模板动作的类型
     */
    public ExerciseType getExerciseTypeManual() {
        return exerciseTypeManual;
    }

    /**
     * 返回动作的个人模板dtw时间
     */
    public double getdtwtime_Manual() {
        return dtw_time_Manual;
    }


    /**
     * 返回fastdtw动作类型
     */
    public ExerciseType getExerciseType_fastDTW() {
        return exerciseType_fastDTW;
    }

    /**
     * 返回fastdtw动作时间
     */
    public double getfastDTWtime() {
        return dtw_time_fastdtw;
    }

    /**
     * 返回四组动作的dtw时间
     */
    public double getdtwtime_group4() {
        return dtw_time_group4;
    }

    /**
     * 返回指定健身动作的评分
     */

    public double[] getRepetitionScore(int index) {
        return repetitionScore[index];
    }

    public double[] getRepetitionScore() {
        return repetitionScore[currentTimes];
    }

    public double getSetScore() {
//        if(singleScore==0)
//        {
//            if(SINGLE_DTW_SCORE<60)
//                singleScore=2;
//                singleScore=0;
//
//        }
        return singleScore;
    }

    /**
     * 获得MLA
     */
    private double[] MLA(double[] ax, double[] ay, double[] az) {
        int i;
        double[] MLA = new double[ax.length];
        for (i = 0; i < ax.length; i++) {
            MLA[i] = ax[i] * ax[i] + ay[i] * ay[i] + az[i] * az[i] - 100;
        }
        return MLA;
    }

    /**
     * 求方差,利用D(X)=E(X^2)-(EX)^2
     */
    private double Var(double[] singleVector) {
        double var = 0;
        double square_Sum = 0;
        double Sum = 0;
        int i;
        for (i = 0; i < singleVector.length; i++) {
            square_Sum += singleVector[i] * singleVector[i];
            Sum += singleVector[i];
        }
        var = square_Sum / singleVector.length - Sum / singleVector.length * Sum / singleVector.length;

        return var;
    }

    /**
     * 降采样
     */
    private native double[] downSample(double[] singleVector, int srcLength, int scale);

    /**
     * EWMA滤波
     */
    private native double[] ewmaFilter(double[] singleVector, double weight, int firstrun, double[] dataBefore, int index);


    /**
     * 绝对值的平均值
     */
    private native double absAvg(double[] a);

    /**
     * 绝对值
     */
    private double abs(double a) {
        return a < 0 ? -a : a;
    }

    /**
     * 求和
     */
    private double sum(double[] a) {
        int i;
        double res = 0;
        for (i = 0; i < a.length; i++)
            res += a[i];
        return res;
    }

    /**
     * 求均值
     */
    private native double mean(double[] data);

    /**
     * 求root mean square(rms)
     */
    private native int rms(double[] data);

    /**
     * 窗口未满15组数据，继续收集
     */
    private void collectWindowData(DataSet dataSet) {
        if (myTempIndex < myTempLength) {
            temp1 = dataSet.getGxSet();
            temp2 = dataSet.getGySet();
            temp3 = dataSet.getGzSet();

            temp4 = dataSet.getAxSet();
            temp5 = dataSet.getAySet();
            temp6 = dataSet.getAzSet();
            for (int i = 0; i < temp1.length; i++) {
                window_gx[myTempIndex] = temp1[i];
                window_gy[myTempIndex] = temp2[i];
                window_gz[myTempIndex] = temp3[i];
                window_ax[myTempIndex] = temp4[i];
                window_ay[myTempIndex] = temp5[i];
                window_az[myTempIndex] = temp6[i];
                myTempIndex++;
            }
        } else {
            Log.d(TAG, "hasCollected!");
            hasCollectedWindow = true;
        }
    }

    /**
     * 利用一开始的静止，获取到静止时的平均值，以便于做切分,存取50组数据
     */
    private void collectStaticData(DataSet dataSet) {

        if (tempIndex < tempLength) {
            temp1 = dataSet.getGxSet();
            temp2 = dataSet.getGySet();
            temp3 = dataSet.getGzSet();

            temp4 = dataSet.getAxSet();
            temp5 = dataSet.getAySet();
            temp6 = dataSet.getAzSet();

            for (int i = 0; i < temp1.length; i++) {
                gx[tempIndex] = temp1[i];
                gy[tempIndex] = temp2[i];
                gz[tempIndex] = temp3[i];
                ax[tempIndex] = temp4[i];
                ay[tempIndex] = temp5[i];
                az[tempIndex] = temp6[i];
                tempIndex++;
            }
        } else {
            Avrthreshold1 = absAvg(gx) * b;
            Avrthreshold2 = absAvg(gy) * b;
            Avrthreshold3 = absAvg(gz) * b;
            Varthreshold1 = Var(gx) * c[0];
            Varthreshold2 = Var(gy) * c[0];
            Varthreshold3 = Var(gz) * c[0];
            double[] mla = MLA(ax, ay, az);
            r0 = Var(mla) * c[1];

            dataBefore[0] = ax[ax.length - 1];
            dataBefore[1] = ay[ay.length - 1];
            dataBefore[2] = az[az.length - 1];
            dataBefore[3] = gx[gx.length - 1];
            dataBefore[4] = gy[gy.length - 1];
            dataBefore[5] = gz[gz.length - 1];

            hasCollectedStaticData = true;
            Log.e(TAG, "DataBefore:" + dataBefore[0] + " " + dataBefore[1] + " " + dataBefore[2] + " " + dataBefore[3] + " " + dataBefore[4] + " " + dataBefore[5]);
            Log.e(TAG, "ExerciseType ======================================");
        }

    }

    private boolean loadModelData() {
        ModelData model = new ModelData(context);
        model.readModelData();
        ax_mol = model.getAx_mol();
        ay_mol = model.getAy_mol();
        az_mol = model.getAz_mol();
        gx_mol = model.getGx_mol();
        gy_mol = model.getGy_mol();
        gz_mol = model.getGz_mol();
        mx_mol = model.getMx_mol();
        my_mol = model.getMy_mol();
        mz_mol = model.getMz_mol();
        p1_mol = model.getP1_mol();
        p2_mol = model.getP2_mol();
        p3_mol = model.getP3_mol();
        num_mol = model.getNum_mol();
        if (ax_mol != null && ay_mol != null && az_mol != null &&
                gx_mol != null && gy_mol != null && gz_mol != null &&
                mx_mol != null && my_mol != null && mz_mol != null &&
                p1_mol != null && p2_mol != null && p3_mol != null) {
            score.setModelLength(ax_mol);
            score.setModelData(ax_mol, ay_mol, az_mol, gx_mol, gy_mol, gz_mol,
                    mx_mol, my_mol, mz_mol, p1_mol, p2_mol, p3_mol);
            return true;
        } else
            return false;
    }

    private boolean loadModelData_Manual() {
        ModelData_Manual model = new ModelData_Manual(context);
        model.readModelData();
        ax_mol_Manual = model.getAx_mol();
        ay_mol_Manual = model.getAy_mol();
        az_mol_Manual = model.getAz_mol();
        gx_mol_Manual = model.getGx_mol();
        gy_mol_Manual = model.getGy_mol();
        gz_mol_Manual = model.getGz_mol();
        mx_mol_Manual = model.getMx_mol();
        my_mol_Manual = model.getMy_mol();
        mz_mol_Manual = model.getMz_mol();
        p1_mol_Manual = model.getP1_mol();
        p2_mol_Manual = model.getP2_mol();
        p3_mol_Manual = model.getP3_mol();
        num_mol_Manual = model.getNum_mol();
        if (ax_mol_Manual != null && ay_mol_Manual != null && az_mol_Manual != null &&
                gx_mol_Manual != null && gy_mol_Manual != null && gz_mol_Manual != null &&
                mx_mol_Manual != null && my_mol_Manual != null && mz_mol_Manual != null &&
                p1_mol_Manual != null && p2_mol_Manual != null && p3_mol_Manual != null) {
            score.setModelLength(ax_mol_Manual);
            score.setModelData(ax_mol_Manual, ay_mol_Manual, az_mol_Manual, gx_mol_Manual, gy_mol_Manual, gz_mol_Manual,
                    mx_mol_Manual, my_mol_Manual, mz_mol_Manual, p1_mol_Manual, p2_mol_Manual, p3_mol_Manual);
            return true;
        } else
            return false;
    }

    private boolean loadModelData_Left() {
        ModelData_Left model = new ModelData_Left(context);
        model.readModelData();
        ax_mol_left = model.getAx_mol();
        ay_mol_left = model.getAy_mol();
        az_mol_left = model.getAz_mol();
        gx_mol_left = model.getGx_mol();
        gy_mol_left = model.getGy_mol();
        gz_mol_left = model.getGz_mol();
        mx_mol_left = model.getMx_mol();
        my_mol_left = model.getMy_mol();
        mz_mol_left = model.getMz_mol();
        p1_mol_left = model.getP1_mol();
        p2_mol_left = model.getP2_mol();
        p3_mol_left = model.getP3_mol();
        num_mol_Left = model.getNum_mol();
        if (ax_mol_left != null && ay_mol_left != null && az_mol_left != null &&
                gx_mol_left != null && gy_mol_left != null && gz_mol_left != null &&
                mx_mol_left != null && my_mol_left != null && mz_mol_left != null &&
                p1_mol_left != null && p2_mol_left != null && p3_mol_left != null) {
            score.setModelLength(ax_mol_left);
            score.setModelData(ax_mol_left, ay_mol_left, az_mol_left, gx_mol_left, gy_mol_left, gz_mol_left,
                    mx_mol_left, my_mol_left, mz_mol_left, p1_mol_left, p2_mol_left, p3_mol_left);
            return true;
        } else
            return false;
    }

    private boolean loadModelData_Right() {
        ModelData_Right model = new ModelData_Right(context);
        model.readModelData();
        ax_mol_right = model.getAx_mol();
        ay_mol_right = model.getAy_mol();
        az_mol_right = model.getAz_mol();
        gx_mol_right = model.getGx_mol();
        gy_mol_right = model.getGy_mol();
        gz_mol_right = model.getGz_mol();
        mx_mol_right = model.getMx_mol();
        my_mol_right = model.getMy_mol();
        mz_mol_right = model.getMz_mol();
        p1_mol_right = model.getP1_mol();
        p2_mol_right = model.getP2_mol();
        p3_mol_right = model.getP3_mol();
        num_mol_Right = model.getNum_mol();
        if (ax_mol_right != null && ay_mol_right != null && az_mol_right != null &&
                gx_mol_right != null && gy_mol_right != null && gz_mol_right != null &&
                mx_mol_right != null && my_mol_right != null && mz_mol_right != null &&
                p1_mol_right != null && p2_mol_right != null && p3_mol_right != null) {
            score.setModelLength(ax_mol_right);
            score.setModelData(ax_mol_right, ay_mol_right, az_mol_right, gx_mol_right, gy_mol_right, gz_mol_right,
                    mx_mol_right, my_mol_right, mz_mol_right, p1_mol_right, p2_mol_right, p3_mol_right);
            return true;
        } else
            return false;
    }


    private void setExerciseType(int type) {
        if (type < 1 || type > 17) {
            Log.e(TAG, "setExerciseType(),enter wrong type,type<1 or typr>17");
            return;
        }
        //
        NUM_OF_ACTION_ARRAY[type - 1]++;
        switch (type) {
            case 1:
                exerciseType = ExerciseType.Alternate_Dumbbell_Curl_1;
                break;
            case 2:
                exerciseType = ExerciseType.Cable_Crossovers_2;
                break;
            case 3:
                exerciseType = ExerciseType.Dumbbells_Alternate_Aammer_Curls_3;
                break;
            case 4:
                exerciseType = ExerciseType.Bent_over_lateral_raise_4;
                break;
            case 5:
                exerciseType = ExerciseType.Flat_Bench_Barbell_Press_5;
                break;
            case 6:
                exerciseType = ExerciseType.Flat_Bench_Dumbbell_Flye_6;
                break;
            case 7:
                exerciseType = ExerciseType.Bent_Over_Lateral_Raise_7;
                break;
            case 8:
                exerciseType = ExerciseType.Barbell_Bent_Over_Row_8;
                break;
            case 9:
                exerciseType = ExerciseType.Barbell_Neck_After_Bending_9;
                break;
            case 10:
                exerciseType = ExerciseType.Machine_Curls_10;
                break;
            case 11:
                exerciseType = ExerciseType.Pec_Deck_Flye_11;
                break;
            case 12:
                exerciseType = ExerciseType.Instruments_Made_Thoracic_Mobility_12;
                break;
            case 13:
                exerciseType = ExerciseType.Reverse_Grip_Pulldown_13;
                break;

            case 14:
                exerciseType = ExerciseType.One_Arm_Dumbell_Row_14;
                break;
            case 15:
                exerciseType = ExerciseType.Dumbbell_Is_The_Shoulder_15;
                break;
            case 16:
                exerciseType = ExerciseType.TOO_SLOW;
                break;
            case 17:
                exerciseType = ExerciseType.TOO_FAST;
                break;
        }
    }

    private void setExerciseType_fastDTW(int type) {
        if (type < 1 || type > 17) {
            Log.e(TAG, "setExerciseType(),enter wrong type,type<1 or typr>17");
            return;
        }
        //
        switch (type) {
            case 1:
                exerciseType_fastDTW = ExerciseType.Alternate_Dumbbell_Curl_1;
                break;
            case 2:
                exerciseType_fastDTW = ExerciseType.Cable_Crossovers_2;
                break;
            case 3:
                exerciseType_fastDTW = ExerciseType.Dumbbells_Alternate_Aammer_Curls_3;
                break;
            case 4:
                exerciseType_fastDTW = ExerciseType.Bent_over_lateral_raise_4;
                break;
            case 5:
                exerciseType_fastDTW = ExerciseType.Flat_Bench_Barbell_Press_5;
                break;
            case 6:
                exerciseType_fastDTW = ExerciseType.Flat_Bench_Dumbbell_Flye_6;
                break;
            case 7:
                exerciseType_fastDTW = ExerciseType.Bent_Over_Lateral_Raise_7;
                break;
            case 8:
                exerciseType_fastDTW = ExerciseType.Barbell_Bent_Over_Row_8;
                break;
            case 9:
                exerciseType_fastDTW = ExerciseType.Barbell_Neck_After_Bending_9;
                break;
            case 10:
                exerciseType_fastDTW = ExerciseType.Machine_Curls_10;
                break;
            case 11:
                exerciseType_fastDTW = ExerciseType.Pec_Deck_Flye_11;
                break;
            case 12:
                exerciseType_fastDTW = ExerciseType.Instruments_Made_Thoracic_Mobility_12;
                break;
            case 13:
                exerciseType_fastDTW = ExerciseType.Reverse_Grip_Pulldown_13;
                break;
            case 14:
                exerciseType_fastDTW = ExerciseType.One_Arm_Dumbell_Row_14;
                break;
            case 15:
                exerciseType_fastDTW = ExerciseType.Dumbbell_Is_The_Shoulder_15;
                break;
//            case 16:
//                exerciseType_fastDTW = ExerciseType.Birds_Standing_16;
//                break;
//            case 17:
//                exerciseType_fastDTW = ExerciseType.Sitting_On_Shoulder_17;
//                break;
//            case 18:
//                exerciseType_fastDTW = ExerciseType.motion_num_eighteen_18;
//                break;
//            case 19:
//                exerciseType_fastDTW = ExerciseType.motion_num_nineteen_19;
//                break;
//            case 20:
//                exerciseType_fastDTW = ExerciseType.motion_num_twenty_20;
//                break;
            case 16:
                exerciseType_fastDTW = ExerciseType.TOO_SLOW;
                break;
            case 17:
                exerciseType_fastDTW = ExerciseType.TOO_FAST;
                break;
        }
    }

    private void setExerciseType_Manual(int type) {
        if (type < 1 || type > 17) {
            Log.e(TAG, "setExerciseType(),enter wrong type,type<1 or typr>17");
            return;
        }
        //
        switch (type) {
            case 1:
                exerciseTypeManual = ExerciseType.Alternate_Dumbbell_Curl_1;
                break;
            case 2:
                exerciseTypeManual = ExerciseType.Cable_Crossovers_2;
                break;
            case 3:
                exerciseTypeManual = ExerciseType.Dumbbells_Alternate_Aammer_Curls_3;
                break;
            case 4:
                exerciseTypeManual = ExerciseType.Bent_over_lateral_raise_4;
                break;
            case 5:
                exerciseTypeManual = ExerciseType.Flat_Bench_Barbell_Press_5;
                break;
            case 6:
                exerciseTypeManual = ExerciseType.Flat_Bench_Dumbbell_Flye_6;
                break;
            case 7:
                exerciseTypeManual = ExerciseType.Bent_Over_Lateral_Raise_7;
                break;
            case 8:
                exerciseTypeManual = ExerciseType.Barbell_Bent_Over_Row_8;
                break;
            case 9:
                exerciseTypeManual = ExerciseType.Barbell_Neck_After_Bending_9;
                break;
            case 10:
                exerciseTypeManual = ExerciseType.Machine_Curls_10;
                break;
            case 11:
                exerciseTypeManual = ExerciseType.Pec_Deck_Flye_11;
                break;
            case 12:
                exerciseTypeManual = ExerciseType.Instruments_Made_Thoracic_Mobility_12;
                break;
            case 13:
                exerciseTypeManual = ExerciseType.Reverse_Grip_Pulldown_13;
                break;
            case 14:
                exerciseTypeManual = ExerciseType.One_Arm_Dumbell_Row_14;
                break;
            case 15:
                exerciseTypeManual = ExerciseType.Dumbbell_Is_The_Shoulder_15;
                break;
//            case 16:
//                exerciseTypeManual = ExerciseType.Birds_Standing_16;
//                break;
//            case 17:
//                exerciseTypeManual = ExerciseType.Sitting_On_Shoulder_17;
//                break;
//            case 18:
//                exerciseTypeManual = ExerciseType.motion_num_eighteen_18;
//                break;
//            case 19:
//                exerciseTypeManual = ExerciseType.motion_num_nineteen_19;
//                break;
//            case 20:
//                exerciseTypeManual = ExerciseType.motion_num_twenty_20;
//                break;
            case 16:
                exerciseTypeManual = ExerciseType.TOO_SLOW;
                break;
            case 17:
                exerciseTypeManual = ExerciseType.TOO_FAST;
                break;
        }
    }

    private void setExerciseTypeForBack(int type) {
        if (type < 1 || type > 17) {
            Log.e(TAG, "setExerciseType(),enter wrong type,type<1 or typr>17");
            return;
        }
        //
        switch (type) {
            case 1:
                exerciseTypeForBack = ExerciseType.Alternate_Dumbbell_Curl_1;
                break;
            case 2:
                exerciseTypeForBack = ExerciseType.Cable_Crossovers_2;
                break;
            case 3:
                exerciseTypeForBack = ExerciseType.Dumbbells_Alternate_Aammer_Curls_3;
                break;
            case 4:
                exerciseTypeForBack = ExerciseType.Bent_over_lateral_raise_4;
                break;
            case 5:
                exerciseTypeForBack = ExerciseType.Flat_Bench_Barbell_Press_5;
                break;
            case 6:
                exerciseTypeForBack = ExerciseType.Flat_Bench_Dumbbell_Flye_6;
                break;
            case 7:
                exerciseTypeForBack = ExerciseType.Bent_Over_Lateral_Raise_7;
                break;
            case 8:
                exerciseTypeForBack = ExerciseType.Barbell_Bent_Over_Row_8;
                break;
            case 9:
                exerciseTypeForBack = ExerciseType.Barbell_Neck_After_Bending_9;
                break;
            case 10:
                exerciseTypeForBack = ExerciseType.Machine_Curls_10;
                break;
            case 11:
                exerciseTypeForBack = ExerciseType.Pec_Deck_Flye_11;
                break;
            case 12:
                exerciseTypeForBack = ExerciseType.Instruments_Made_Thoracic_Mobility_12;
                break;
            case 13:
                exerciseTypeForBack = ExerciseType.Reverse_Grip_Pulldown_13;
                break;
            case 14:
                exerciseTypeForBack = ExerciseType.One_Arm_Dumbell_Row_14;
                break;
            case 15:
                exerciseTypeForBack = ExerciseType.Dumbbell_Is_The_Shoulder_15;
                break;
//            case 16:
//                exerciseTypeForBack = ExerciseType.Birds_Standing_16;
//                break;
//            case 17:
//                exerciseTypeForBack = ExerciseType.Sitting_On_Shoulder_17;
//                break;
//            case 18:
//                exerciseTypeForBack = ExerciseType.motion_num_eighteen_18;
//                break;
//            case 19:
//                exerciseTypeForBack = ExerciseType.motion_num_nineteen_19;
//                break;
//            case 20:
//                exerciseTypeForBack = ExerciseType.motion_num_twenty_20;
//                break;
            case 16:
                exerciseTypeForBack = ExerciseType.TOO_SLOW;
                break;
            case 17:
                exerciseTypeForBack = ExerciseType.TOO_FAST;
                break;
        }
    }


    private void setActionNum() {
        NUM_OF_ACTION++;
        Log.i(TAG, "exerciseType=" + exerciseType + " lastType=" + exerciseTypeManual);
    }

    public int getActionNum() {
        return NUM_OF_ACTION;
    }

    public int[] getNUM_OF_ACTION_ARRAY() {
        return NUM_OF_ACTION_ARRAY;
    }

    public float getGroupAveTime1() {
        return GroupSumTime1 / NUM1_OF_ACTION;
    }

    public float getGroupMaxTime1() {
        return GroupMaxTime1;
    }

    public float getGroupMinTime1() {
        return GroupMinTime1;
    }

    public float getGroupMaxDtw1() {
        return GroupMaxDtw1;
    }

    public float getGroupMinDtw1() {
        return GroupMinDtw1;
    }

    public float getGroupAveDtw1() {
        return GroupSumDtw1 / NUM1_OF_ACTION;
    }

    public float getGroupAveTime2() {
        return GroupSumTime2 / NUM2_OF_ACTION;
    }

    public float getGroupMaxTime2() {
        return GroupMaxTime2;
    }

    public float getGroupMinTime2() {
        return GroupMinTime2;
    }

    public float getGroupMaxDtw2() {
        return GroupMaxDtw2;
    }

    public float getGroupMinDtw2() {
        return GroupMinDtw2;
    }

    public float getGroupAveDtw2() {
        return GroupSumDtw2 / NUM2_OF_ACTION;
    }

    public float getGroupAveTime3() {
        return GroupSumTime3 / NUM3_OF_ACTION;
    }

    public float getGroupMaxTime3() {
        return GroupMaxTime3;
    }

    public float getGroupMinTime3() {
        return GroupMinTime3;
    }

    public float getGroupMaxDtw3() {
        return GroupMaxDtw3;
    }

    public float getGroupMinDtw3() {
        return GroupMinDtw3;
    }

    public float getGroupAveDtw3() {
        return GroupSumDtw3 / NUM3_OF_ACTION;
    }


    public double getTotalTime() {
        return totalTime;
    }

    public double getActionDuration() {
        return SingleDuration;
    }

    public void getSingleDtwScore(double mydtw, int index) {
//       double coach = Every_dtw[index];
//        int t = (int) (coach / mydtw * 100);
//        if (t >= 100) {
//            SINGLE_DTW_SCORE = 100;
//        } else if (t < 100 && t >= 60) {
//            SINGLE_DTW_SCORE = t;
//        }
//        else if(t<60 && t>=50){
//            SINGLE_DTW_SCORE=t+10;
//        } else if (t < 50 && t >= 10) {
//            SINGLE_DTW_SCORE = t + 50;
//        } else {
//            SINGLE_DTW_SCORE= (int) (Math.random()*49+10);
//        }
        //  int t=(int) Math.random()*100;
//      int t=  (int)Math.floor(Math.random()*100 + 1);
//        Log.e("TAG","t="+t);
//        if(t<=90)
//            SINGLE_DTW_SCORE=1;
//        else
//            SINGLE_DTW_SCORE=2;
    }

    public int getSingleDtwScore() {
        return SINGLE_DTW_SCORE;
    }

    public int getSingleDTW() {
        return SINGLE_DTW;
    }

    public double[] getAxArray() {
        return ax_test;
    }

    public double[] getAyArray() {
        return ay_test;
    }

    public double[] getAzArray() {
        return az_test;
    }

    public double[] getGxArray() {
        return gx_test;
    }

    public double[] getGyArray() {
        return gy_test;
    }

    public double[] getGzArray() {
        return gz_test;
    }

    public double[] getSelectedDataSet(int index) {
        if (selectedFlag)
            return selectedDataSet.getDataByIndex(index);
        return null;
    }

    public double[] getSelectedDataSet_Manual(int index) {
        if (selectedFlag_Manual)
            return selectedDataSet_Manual.getDataByIndex(index);
        return null;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private class DTWTask implements Callable<Double> {

        private DynamicTimeWarping dtw;
        private double[] t;
        private double[] r;
        private int num;

        public DTWTask(DynamicTimeWarping dtw, double[] t, int num, double[] r) {
            this.dtw = dtw;
            this.t = t;
            this.r = r;
            this.num = num;
        }

        @Override
        public Double call() throws Exception {
            return dtw.getDtwValue(t, num, r);
        }
    }

    private class FastDTWTask implements Callable<Double> {

        private DynamicTimeWarping dtw;
        private double[] t;
        private double[] r;
        private int num;

        public FastDTWTask(DynamicTimeWarping dtw, double[] t, int num, double[] r) {
            this.dtw = dtw;
            this.t = t;
            this.r = r;
            this.num = num;
        }

        @Override
        public Double call() throws Exception {
            return dtw.getfastDtwValue(t, num, r);
        }

    }
}


