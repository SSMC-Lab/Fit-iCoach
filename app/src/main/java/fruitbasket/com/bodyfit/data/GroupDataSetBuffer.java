package fruitbasket.com.bodyfit.data;

import android.util.Log;

/**
 * Created by Administrator on 2016/11/21.
 */
public class GroupDataSetBuffer {
    public static final String TAG="GroupDataSetBuffer";

    private int size;
    private int index;

    private DataSetBuffer dataSetBufArray[];

    public GroupDataSetBuffer(){
        reset();
    }

    private void create(){
        size=1;
        index=0;
        dataSetBufArray=new DataSetBuffer[size];
    }

    public void reset(){
        size=0;
        index=0;
        dataSetBufArray=null;
    }

    private void addSize(){
        size++;
        index++;
    }

    public boolean add(DataSetBuffer data){
        if(data==null || data.getCapacity()==0){
            Log.e(TAG,"add()->data=null");
            return false;
        }
        if(size==0){
            create();
        }
        if(index!=0) {
            addSize();
            DataSetBuffer newArray[] = new DataSetBuffer[size];
            for (int i = 0; i < dataSetBufArray.length; i++)
                newArray[i] = dataSetBufArray[i];
            dataSetBufArray = newArray;
        }

        dataSetBufArray[index]=data;

        return true;
    }

    public int getLength(){
        return size;
    }

    public boolean empty(){
        return size==0;
    }
}
