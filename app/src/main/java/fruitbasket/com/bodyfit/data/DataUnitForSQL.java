package fruitbasket.com.bodyfit.data;

import android.util.Log;

import org.litepal.crud.DataSupport;

/**
 *
 * 这个类是用于将DataUnit的数据添加到数据库中
 * 数据库对于添加的类 构造函数的参数不能含有数组
 * Created by lkeye on 2018/3/25.
 */

public class DataUnitForSQL extends DataSupport {

    private static final String TAG="DataUnitForSQL";
    public static final int SENSER_NUMBER = 12;//传感器的数据的数量

    private double time;
    private double ax;
    private double ay;
    private double az;
    private double gx;
    private double gy;
    private double gz;
    private double mx;
    private double my;
    private double mz;
    private double p1;
    private double p2;
    private double p3;

    public DataUnitForSQL(double time, double ax, double ay, double az, double gx, double gy, double gz,
                          double mx, double my, double mz, double p1, double p2, double p3){
        this.time = time;
        this.ax = ax;
        this.ay = ay;
        this.az = az;
        this.gx = gx;
        this.gy = gy;
        this.gz = gz;
        this.mx = mx;
        this.my = my;
        this.mz = mz;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getAx() {
        return ax;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public double getAy() {
        return ay;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }

    public double getAz() {
        return az;
    }

    public void setAz(double az) {
        this.az = az;
    }

    public double getGx() {
        return gx;
    }

    public void setGx(double gx) {
        this.gx = gx;
    }

    public double getGy() {
        return gy;
    }

    public void setGy(double gy) {
        this.gy = gy;
    }

    public double getGz() {
        return gz;
    }

    public void setGz(double gz) {
        this.gz = gz;
    }

    public double getMx() {
        return mx;
    }

    public void setMx(double mx) {
        this.mx = mx;
    }

    public double getMy() {
        return my;
    }

    public void setMy(double my) {
        this.my = my;
    }

    public double getMz() {
        return mz;
    }

    public void setMz(double mz) {
        this.mz = mz;
    }

    public double getP1() {
        return p1;
    }

    public void setP1(double p1) {
        this.p1 = p1;
    }

    public double getP2() {
        return p2;
    }

    public void setP2(double p2) {
        this.p2 = p2;
    }

    public double getP3() {
        return p3;
    }

    public void setP3(double p3) {
        this.p3 = p3;
    }

    public void display() {
        Log.i(TAG, "SQLdata"+ time + " " + ax + ay + az + gx + gy + gz + mx + my + mz + p1 + p2 + p3);
    }
    public double []toArray(){
        double a[]=new double[6];
        a[0]=ax;a[1]=ay;
        a[2]=az;a[3]=gx;
        a[4]=gy;a[5]=gz;
        return a;
    }
}
