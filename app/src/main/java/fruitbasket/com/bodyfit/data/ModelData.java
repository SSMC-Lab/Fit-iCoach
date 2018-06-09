package fruitbasket.com.bodyfit.data;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import fruitbasket.com.bodyfit.Conditions;

/**
 * 读取模板数据
 * Created by Administrator on 2016/11/14.
 */
public class ModelData {
    public static final String TAG = "ModelData";

    private Context context;
    private static final int exercise_num = Conditions.EXERCISE_NUM;       //模板数（动作数）
    private static final int MaxSamples = Conditions.MAX_SAMPLES_OF_ACTIONS;    //单个动作中允许的最大samples
    public static String dirName;
    //所有模板文件
//    private final String[] FILENAME= {
//            "1.txt", "3.txt", "5.txt", "6.txt", "7.txt", "8.txt", "9.txt", "10.txt", "11.txt",
//            "12.txt", "13.txt", "14.txt", "15.txt", "16.txt", "17.txt"
//    };
    private final String[] FILENAME = {
//            "1.txt", "3.txt", "5.txt", "13.txt", "14.txt", "16.txt",
//            "1_1.txt", "3_1.txt","13_1.txt", "14_1.txt","16_1.txt"
            "Template-3/1.txt", "Template-3/2.txt", "Template-3/3.txt", "Template-3/4.txt", "Template-3/5.txt", "Template-3/6.txt", "Template-3/7.txt",
            "Template-3/8.txt", "Template-3/9.txt", "Template-3/10.txt", "Template-3/11.txt", "Template-3/12.txt", "Template-3/13.txt", "Template-3/14.txt", "Template-3/15.txt"
//            "Template-3/16.txt", "Template-3/17.txt", "Template-3/18.txt", "Template-3/19.txt", "Template-3/20.txt"
    };
    //储存模板的空间
    private double[][] ax_mol;
    private double[][] ay_mol;
    private double[][] az_mol;
    private double[][] gx_mol;
    private double[][] gy_mol;
    private double[][] gz_mol;
    private double[][] mx_mol;
    private double[][] my_mol;
    private double[][] mz_mol;
    private double[][] p1_mol;
    private double[][] p2_mol;
    private double[][] p3_mol;
    private int[] num_mol;

    public ModelData(Context con) {
        context = con;
        ax_mol = new double[exercise_num][];
        ay_mol = new double[exercise_num][];
        az_mol = new double[exercise_num][];
        gx_mol = new double[exercise_num][];
        gy_mol = new double[exercise_num][];
        gz_mol = new double[exercise_num][];
        mx_mol = new double[exercise_num][];
        my_mol = new double[exercise_num][];
        mz_mol = new double[exercise_num][];
        p1_mol = new double[exercise_num][];
        p2_mol = new double[exercise_num][];
        p3_mol = new double[exercise_num][];

        num_mol = new int[exercise_num];

        for (int i = 0; i < exercise_num; i++) {
            ax_mol[i] = new double[MaxSamples];
            ay_mol[i] = new double[MaxSamples];
            az_mol[i] = new double[MaxSamples];
            gx_mol[i] = new double[MaxSamples];
            gy_mol[i] = new double[MaxSamples];
            gz_mol[i] = new double[MaxSamples];
            mx_mol[i] = new double[MaxSamples];
            my_mol[i] = new double[MaxSamples];
            mz_mol[i] = new double[MaxSamples];
            p1_mol[i] = new double[MaxSamples];
            p2_mol[i] = new double[MaxSamples];
            p3_mol[i] = new double[MaxSamples];
        }
    }

    /**
     * 读取模板数据
     */
    public void readModelData() {
        //读取模板数据,先将模板数据定为MaxSample大小
        try {

            int count = 0;//记录第count-1个动作的数据
            if (context != null) {
                while (count < FILENAME.length) {

                    InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(FILENAME[count]), "GBK");
                    BufferedReader bufReader = new BufferedReader(inputReader);
                    Log.i(TAG, "load successfully,count=" + count + " Path.length=" + FILENAME.length);

                    String line = "";
                    String str[];
                    int num = 0;//记录第count-1个动作的第num-1个ax/ay/az
                    while ((line = bufReader.readLine()) != null) {
                        str = line.split(" ");
                        ax_mol[count][num] = Double.parseDouble(str[1]);
                        ay_mol[count][num] = Double.parseDouble(str[2]);
                        az_mol[count][num] = Double.parseDouble(str[3]);
                        gx_mol[count][num] = Double.parseDouble(str[4]);
                        gy_mol[count][num] = Double.parseDouble(str[5]);
                        gz_mol[count][num] = Double.parseDouble(str[6]);
                        mx_mol[count][num] = Double.parseDouble(str[7]);
                        my_mol[count][num] = Double.parseDouble(str[8]);
                        mz_mol[count][num] = Double.parseDouble(str[9]);
                        p1_mol[count][num] = Double.parseDouble(str[10]);
                        p2_mol[count][num] = Double.parseDouble(str[11]);
                        p3_mol[count][num] = Double.parseDouble(str[12]);

                        num++;
                        if (num >= MaxSamples)
                            break;
                    }
                    num_mol[count] = num;

                    count++;
                    inputReader.close();
                    bufReader.close();
                }
            } else {
                ///若context==null
                Log.e(TAG, "ModelData-->context=null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double[][] getAx_mol() {
        return ax_mol;
    }

    public double[][] getAy_mol() {
        return ay_mol;
    }

    public double[][] getAz_mol() {
        return az_mol;
    }

    public double[][] getGx_mol() {
        return gx_mol;
    }

    public double[][] getGy_mol() {
        return gy_mol;
    }

    public double[][] getGz_mol() {
        return gz_mol;
    }

    public double[][] getMx_mol() {
        return mx_mol;
    }

    public double[][] getMy_mol() {
        return my_mol;
    }

    public double[][] getMz_mol() {
        return mz_mol;
    }

    public double[][] getP1_mol() {
        return p1_mol;
    }

    public double[][] getP2_mol() {
        return p2_mol;
    }

    public double[][] getP3_mol() {
        return p3_mol;
    }

    public int[] getNum_mol() {
        return num_mol;
    }
}
