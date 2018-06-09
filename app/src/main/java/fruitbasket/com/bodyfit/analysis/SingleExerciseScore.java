package fruitbasket.com.bodyfit.analysis;

import android.util.Log;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.data.SelectedDataSet;

/**
 * Created by Administrator on 2016/11/15.
 */
public class SingleExerciseScore {
    public static final String TAG="SingleExerciseScore";

    private static final int TOO_SLOW=0;
    private static final int NORMAL=1;
    private static final int TOO_FAST=2;

    private int score;
    private static final int exercise_num= Conditions.EXERCISE_NUM;       //模板数（动作数）

    DynamicTimeWarping dtw;

    //保存每一个动作每一个维度的模板的长度，用于判断太快或者太慢
    private static int []mol_length;

    //储存模板的空间
    private double[][]ax_mol;
    private double[][]ay_mol;
    private double[][]az_mol;
    private double[][]gx_mol;
    private double[][]gy_mol;
    private double[][]gz_mol;
    private double[][]mx_mol;
    private double[][]my_mol;
    private double[][]mz_mol;
    private double[][]p1_mol;
    private double[][]p2_mol;
    private double[][]p3_mol;

    SingleExerciseScore(){
        mol_length=new int[exercise_num];
        dtw=new DynamicTimeWarping();
    }

    /**
     * 设置每一个模板动作的长度
     * @param data
     */
    public void setModelLength(double[][] data){

        for(int i=0;i<exercise_num;i++)
        {
            mol_length[i]=getRealLength(data[i]);
            Log.i(TAG,"mol_length["+i+"]="+mol_length[i]);
        }
    }

    /**
     * 设置模板数据
     */
    public void setModelData(double [][]ax,double [][]ay,double [][]az,
                             double [][]gx,double [][]gy,double [][]gz,
                             double [][]mx,double [][]my,double [][]mz,
                             double [][]p1,double [][]p2,double [][]p3)
    {
        ax_mol=ax;
        ay_mol=ay;
        az_mol=az;
        gx_mol=gx;
        gy_mol=gy;
        gz_mol=gz;
        mx_mol=mx;
        my_mol=my;
        mz_mol=mz;
        p1_mol=p1;
        p2_mol=p2;
        p3_mol=p3;

    }

    public double calculateScore(SelectedDataSet selectedData,int exerciseType){

        //test_data存放需要判断的动作的每一维度的测试数据
        double [][]test_data=new double[12][];
        test_data[0]=selectedData.getDataByIndex(0);
        test_data[1]=selectedData.getDataByIndex(1);
        test_data[2]=selectedData.getDataByIndex(2);
        test_data[3]=selectedData.getDataByIndex(3);
        test_data[4]=selectedData.getDataByIndex(4);
        test_data[5]=selectedData.getDataByIndex(5);
        test_data[6]=selectedData.getDataByIndex(6);
        test_data[7]=selectedData.getDataByIndex(7);
        test_data[8]=selectedData.getDataByIndex(8);
        test_data[9]=selectedData.getDataByIndex(9);
        test_data[10]=selectedData.getDataByIndex(10);
        test_data[11]=selectedData.getDataByIndex(11);


        //mol_data存放需要判断的动作的每一个维度的模板数据
        double [][]mol_data=new double[12][];
        mol_data[0]=ax_mol[exerciseType-1];
        mol_data[1]=ay_mol[exerciseType-1];
        mol_data[2]=az_mol[exerciseType-1];
        mol_data[3]=gx_mol[exerciseType-1];
        mol_data[4]=gy_mol[exerciseType-1];
        mol_data[5]=gz_mol[exerciseType-1];
        mol_data[6]=mx_mol[exerciseType-1];
        mol_data[7]=my_mol[exerciseType-1];
        mol_data[8]=mz_mol[exerciseType-1];
        mol_data[9]=p1_mol[exerciseType-1];
        mol_data[10]=p2_mol[exerciseType-1];
        mol_data[11]=p3_mol[exerciseType-1];

        /////简单粗暴，需要改善
        return 100;

    }

    private int getRealLength(double data[]){
        if(data==null || data.length==0) {
            Log.e(TAG,"getRealLength()->data=null");
            return 0;
        }
        int count=5;
        int flag=0;
        int len=0;
        for(int i=0;i<data.length;i++){
            if(flag==count)
                break;
            if(data[i]==0)
                flag++;
            len++;
        }

        return len-count;
    }

}
