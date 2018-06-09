package fruitbasket.com.bodyfit.data;

import android.util.Log;

/**
 * 储存一个完整动作的所有维度的数据
 */
public class DataSetBuffer {
    private static final String TAG="DataSetBuffer";

    private static final int DEFAULT_CAPACITY=50;
    private int factor=0;//容量因子
    private int capacity;//缓冲区容量

    private int items =0;//记录缓冲区DataUnit的数量

    private double[] timeBuf;
    private double[] axBuffer;
    private double[] ayBuffer;
    private double[] azBuffer;
    private double[] gxBuffer;
    private double[] gyBuffer;
    private double[] gzBuffer;
    private double[] mxBuffer;
    private double[] myBuffer;
    private double[] mzBuffer;
    private double[] p1Buffer;
    private double[] p2Buffer;
    private double[] p3Buffer;

    public DataSetBuffer(){
        resetCapacity();
    }

    private void updateCapacity(){
        capacity=DEFAULT_CAPACITY*factor;
    }

    private void addCapacity(){
        factor++;
        updateCapacity();
    }

    private void resetCapacity(){
        factor=0;
        items=0;
        updateCapacity();
    }

    public int getCapacity(){
        return capacity;
    }

    private void createBuffer(){
        resetCapacity();
        addCapacity();
        timeBuf=new double[capacity];
        axBuffer=new double[capacity];
        ayBuffer=new double[capacity];
        azBuffer=new double[capacity];
        gxBuffer=new double[capacity];
        gyBuffer=new double[capacity];
        gzBuffer=new double[capacity];
        mxBuffer=new double[capacity];
        myBuffer=new double[capacity];
        mzBuffer=new double[capacity];
        p1Buffer=new double[capacity];
        p2Buffer=new double[capacity];
        p3Buffer=new double[capacity];
    }

//    private double cutDecimal(double data){
//        return ((int)(data*1000))/1000.0;
//    }

    public void add(DataSet dataSet){
        if(capacity<=0){
            Log.i(TAG,"capacity<=0,createBuffer()");
            createBuffer();
        }
        if(items +dataSet.size()>capacity){
//            Log.i(TAG,"items+dataSet.size()>capacity");
//            Log.i(TAG,"items="+items+",dataSet.size()="+dataSet.size());
//            Log.i(TAG, "before addCapacity(): capacity=" + capacity);
            addCapacity();

//            Log.i(TAG,"after addCapacity(): capacity="+capacity);
//            Log.i(TAG,"axBuffer.length="+axBuffer.length);

            double[] newTimeBuf=new double[capacity];
            for(int i=0;i<timeBuf.length;i++){
                newTimeBuf[i]=timeBuf[i];
            }
            timeBuf=newTimeBuf;

            double[] newAxBuffer=new double[capacity];
            for(int i=0;i<axBuffer.length;i++){
                newAxBuffer[i]=axBuffer[i];
            }
            axBuffer=newAxBuffer;

            double[] newAyBuffer=new double[capacity];
            for(int i=0;i<ayBuffer.length;i++){
                newAyBuffer[i]=ayBuffer[i];
            }
            ayBuffer=newAyBuffer;

            double[] newAzBuffer=new double[capacity];
            for(int i=0;i<azBuffer.length;i++){
                newAzBuffer[i]=azBuffer[i];
            }
            azBuffer=newAzBuffer;

            double[] newGxBuffer=new double[capacity];
            for(int i=0;i<gxBuffer.length;i++){
                newGxBuffer[i]=gxBuffer[i];
            }
            gxBuffer=newGxBuffer;

            double[] newGyBuffer=new double[capacity];
            for(int i=0;i<gyBuffer.length;i++){
                newGyBuffer[i]=gyBuffer[i];
            }
            gyBuffer=newGyBuffer;

            double[] newGzBuffer=new double[capacity];
            for(int i=0;i<gzBuffer.length;i++){
                newGzBuffer[i]=gzBuffer[i];
            }
            gzBuffer=newGzBuffer;

            double[] newMxBuffer=new double[capacity];
            for(int i=0;i<mxBuffer.length;i++){
                newMxBuffer[i]=mxBuffer[i];
            }
            mxBuffer=newMxBuffer;

            double[] newMyBuffer=new double[capacity];
            for(int i=0;i<myBuffer.length;i++){
                newMyBuffer[i]=myBuffer[i];
            }
            myBuffer=newMyBuffer;

            double[] newMzBuffer=new double[capacity];
            for(int i=0;i<mzBuffer.length;i++){
                newMzBuffer[i]=mzBuffer[i];
            }
            mzBuffer=newMzBuffer;

            double[] newP1Buffer=new double[capacity];
            for(int i=0;i<p1Buffer.length;i++){
                newP1Buffer[i]=p1Buffer[i];
            }
            p1Buffer=newP1Buffer;

            double[] newP2Buffer=new double[capacity];
            for(int i=0;i<p2Buffer.length;i++){
                newP2Buffer[i]=p2Buffer[i];
            }
            p2Buffer=newP2Buffer;

            double[] newP3Buffer=new double[capacity];
            for(int i=0;i<p3Buffer.length;i++){
                newP3Buffer[i]=p3Buffer[i];
            }
            p3Buffer=newP3Buffer;
        }

        int i;
        double[] set;

        set=dataSet.getTime();
        for(i=0;i<set.length;i++){
            timeBuf[items+i]=set[i];
        }

        set=dataSet.getAxSet();
        for(i=0;i<set.length;i++){
//            Log.i(TAG,"add(): capacity="+capacity+",axBuffer.length="+axBuffer.length+", items="+items+",i="+i+",set.length="+set.length);
            axBuffer[items +i]=set[i];
        }

        set=dataSet.getAySet();
        for(i=0;i<set.length;i++){
            ayBuffer[items +i]=set[i];
        }

        set=dataSet.getAzSet();
        for(i=0;i<set.length;i++){
            azBuffer[items +i]=set[i];
        }

        set=dataSet.getGxSet();
        for(i=0;i<set.length;i++){
            gxBuffer[items +i]=set[i];
        }

        set=dataSet.getGySet();
        for(i=0;i<set.length;i++){
            gyBuffer[items +i]=set[i];
        }

        set=dataSet.getGzSet();
        for(i=0;i<set.length;i++){
            gzBuffer[items +i]=set[i];
        }

        set=dataSet.getMxSet();
        for(i=0;i<set.length;i++){
            mxBuffer[items +i]=set[i];
        }

        set=dataSet.getMySet();
        for(i=0;i<set.length;i++){
            myBuffer[items +i]=set[i];///
        }

        set=dataSet.getMzSet();
        for(i=0;i<set.length;i++){
            mzBuffer[items +i]=set[i];
        }

        set=dataSet.getP1Set();
        for(i=0;i<set.length;i++){
            p1Buffer[items +i]=set[i];
        }

        set=dataSet.getP2Set();
        for(i=0;i<set.length;i++){
            p2Buffer[items +i]=set[i];
        }

        set=dataSet.getP3Set();
        for(i=0;i<set.length;i++){
            p3Buffer[items +i]=set[i];
        }

        items +=dataSet.size();
    }

    /**
     * 将缓冲区清空
     */
    public void clear(){
        resetCapacity();
        timeBuf=null;
        axBuffer=null;
        ayBuffer=null;
        azBuffer=null;
        gxBuffer=null;
        gyBuffer=null;
        gzBuffer=null;
        mxBuffer=null;
        myBuffer=null;
        mzBuffer=null;
        p1Buffer=null;
        p2Buffer=null;
        p3Buffer=null;
    }

    public boolean isEmpty(){
        return items==0;
    }

    public DataSet toDataSet(){
        return new DataSet(
                timeBuf,
                axBuffer,
                ayBuffer,
                azBuffer,
                gxBuffer,
                gyBuffer,
                gzBuffer,
                mxBuffer,
                myBuffer,
                mzBuffer,
                p1Buffer,
                p2Buffer,
                p3Buffer
        );
    }
}
