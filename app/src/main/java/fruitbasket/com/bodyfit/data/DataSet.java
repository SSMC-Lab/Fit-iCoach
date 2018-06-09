package fruitbasket.com.bodyfit.data;


import android.util.Log;

/**
 * 数据记录集合，用于存储多条记录。本类会将多条记录中同一维度数据整合到一起。
 */
public class DataSet {

    public static final String TAG = "DataSet";

    private int size;

    private double[] time;
    private double[] axSet;
    private double[] aySet;
    private double[] azSet;
    private double[] gxSet;
    private double[] gySet;
    private double[] gzSet;
    private double[] mxSet;
    private double[] mySet;
    private double[] mzSet;
    private double[] p1Set;
    private double[] p2Set;
    private double[] p3Set;

    public DataSet(DataUnit[] dataUnits) {
        fromSourceData(dataUnits);
    }

    /**
     * 此函数被DataSet中的toDataSet()调用，但是toDataSet()没有被调用过
     */
    public DataSet(double[] time, double[] axSet, double[] aySet, double[] azSet, double[] gxSet, double[] gySet, double[] gzSet,
                   double[] mxSet, double[] mySet, double[] mzSet, double[] p1Set, double[] p2Set, double[] p3Set) {
        int length = axSet.length;
        if (length == aySet.length && length == azSet.length &&
                length == gxSet.length && length == gySet.length && length == gzSet.length &&
                length == mxSet.length && length == mySet.length && length == mzSet.length &&
                length == p3Set.length && length == p2Set.length && length == p1Set.length) {

            size = length;
            this.time = time;
            this.axSet = axSet;
            this.aySet = aySet;
            this.azSet = azSet;
            this.gxSet = gxSet;
            this.gySet = gySet;
            this.gzSet = gzSet;
            this.mxSet = mxSet;
            this.mySet = mySet;
            this.mzSet = mzSet;
            this.p1Set = p1Set;
            this.p2Set = p2Set;
            this.p3Set = p3Set;
        } else {
            Log.e(TAG, "error:param of construct function");
            return;
        }
    }

    /**
     * 将数据记录数组转化为一个数据记录集合
     *
     * @param dataUnits
     */
    private void fromSourceData(DataUnit[] dataUnits) {
        size = dataUnits.length;

        time = new double[dataUnits.length];
        axSet = new double[dataUnits.length];
        aySet = new double[dataUnits.length];
        azSet = new double[dataUnits.length];
        gxSet = new double[dataUnits.length];
        gySet = new double[dataUnits.length];
        gzSet = new double[dataUnits.length];
        mxSet = new double[dataUnits.length];
        mySet = new double[dataUnits.length];
        mzSet = new double[dataUnits.length];
        p1Set = new double[dataUnits.length];
        p2Set = new double[dataUnits.length];
        p3Set = new double[dataUnits.length];

        for (int i = 0; i < dataUnits.length; ++i) {
            time[i] = dataUnits[i].getTime();
            axSet[i] = dataUnits[i].getAx();
            aySet[i] = dataUnits[i].getAy();
            azSet[i] = dataUnits[i].getAz();
            gxSet[i] = dataUnits[i].getGx();
            gySet[i] = dataUnits[i].getGy();
            gzSet[i] = dataUnits[i].getGz();
            mxSet[i] = dataUnits[i].getMx();
            mySet[i] = dataUnits[i].getMy();
            mzSet[i] = dataUnits[i].getMz();
            p1Set[i] = dataUnits[i].getP1();
            p2Set[i] = dataUnits[i].getP2();
            p3Set[i] = dataUnits[i].getP3();
        }
    }

    public int size() {
        return size;
    }

    public double[] getTime() {
        return time;
    }

    public void setTime(double time[]) {
        this.time = time;
    }

    public double[] getAxSet() {
        return axSet;
    }

    public void setAxSet(double[] axSet) {
        this.axSet = axSet;
    }

    public double[] getAySet() {
        return aySet;
    }

    public void setAySet(double[] aySet) {
        this.aySet = aySet;
    }

    public double[] getAzSet() {
        return azSet;
    }

    public void setAzSet(double[] azSet) {
        this.azSet = azSet;
    }

    public double[] getGxSet() {
        return gxSet;
    }

    public void setGxSet(double[] gxSet) {
        this.gxSet = gxSet;
    }

    public double[] getGySet() {
        return gySet;
    }

    public void setGySet(double[] gySet) {
        this.gySet = gySet;
    }

    public double[] getGzSet() {
        return gzSet;
    }

    public void setGzSet(double[] gzSet) {
        this.gzSet = gzSet;
    }

    public double[] getMxSet() {
        return mxSet;
    }

    public void setMxSet(double[] mxSet) {
        this.mxSet = mxSet;
    }

    public double[] getMySet() {
        return mySet;
    }

    public void setMySet(double[] mySet) {
        this.mySet = mySet;
    }

    public double[] getMzSet() {
        return mzSet;
    }

    public void setMzSet(double[] mzSet) {
        this.mzSet = mzSet;
    }

    public double[] getP1Set() {
        return p1Set;
    }

    public void setP1Set(double[] p1Set) {
        this.p1Set = p1Set;
    }

    public double[] getP2Set() {
        return p2Set;
    }

    public void setP2Set(double[] p2Set) {
        this.p2Set = p2Set;
    }

    public double[] getP3Set() {
        return p3Set;
    }

    public void setP3Set(double[] p3Set) {
        this.p3Set = p3Set;
    }

    public double getLastElement(int index) // 根据index返回某轴的集合最后一个元素.
    {
        switch (index)
        {
            case 1:
                return axSet[axSet.length-1];
            case 2:
                return aySet[aySet.length-1];
            case 3:
                return azSet[azSet.length-1];
            case 4:
                return gxSet[gxSet.length-1];
            case 5:
                return gySet[gySet.length-1];
            case 6:
                return gzSet[gzSet.length-1];
            default:
                return 0;
        }
    }

    /**
     * 返回指定的传感器数据
     * @param index 指定传感器
     * @return 返回类型：double数组
     */
    public double[] getDataByIndex(int index){
        switch(index){
            case 0:
                return axSet;
            case 1:
                return aySet;
            case 2:
                return azSet;
            case 3:
                return gxSet;
            case 4:
                return gySet;
            case 5:
                return gzSet;
            case 6:
                return mxSet;
            case 7:
                return mySet;
            case 8:
                return mzSet;
            case 9:
                return p1Set;
            case 10:
                return p2Set;
            case 11:
                return p3Set;
            case 12:
                return time;
            default:
                return null;
        }
    }


}
