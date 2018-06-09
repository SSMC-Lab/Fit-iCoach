package fruitbasket.com.bodyfit.data;

import android.util.Log;

/**
 * 本类用于存储从DataSet筛选指定维度的数据
 */
public class SelectedDataSet {
    private int selectSensorsNumber;
    private double[][] selectedDatas;

    public SelectedDataSet(DataSet dataSet, int... selectedSensorData){
        if(dataSet==null || selectedSensorData==null || selectedSensorData.length>dataSet.size()){
            return;
        }
        else{
            selectSensorsNumber=selectedSensorData.length;
            this.selectedDatas =new double[selectSensorsNumber][];
            for(int i=0;i<selectSensorsNumber;i++){
                Log.i("SelectedDataSet",selectSensorsNumber+"");
                this.selectedDatas[i]=dataSet.getDataByIndex(selectedSensorData[i]);
            }
        }
    }

    public SelectedDataSet(double[]...selectedDatas){
        if(selectedDatas!=null && selectedDatas.length>0 && selectedDatas.length<=DataUnit.SENSER_NUMBER){
            selectSensorsNumber=selectedDatas.length;
            this.selectedDatas =new double[selectSensorsNumber][];
            for(int i=0;i<selectSensorsNumber;i++){
                this.selectedDatas[i]=selectedDatas[i];
            }
        }
    }

    /**
     * 返回指定的传感器数据
     * @param index 指定传感器
     * @return
     */
    public double[] getDataByIndex(int index){
        if(index<0||index>=selectSensorsNumber){
            return null;
        }
        else{
            return selectedDatas[index];
        }
    }
}
