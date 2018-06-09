package fruitbasket.com.bodyfit.test;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fruitbasket.com.bodyfit.data.DataUnit;
import fruitbasket.com.bodyfit.data.DataUnitForSQL;

/**
 * 测试数据提供者
 *
 * @Author Welkinshadow
 * @Date 2017/10/13
 */

public class TestDataProvider {
    private static final String TAG = "TestDataProvider";
    private static final String TEST_FILE = "Data1_1.txt";

    private BufferedReader bufferedReader;

    public TestDataProvider(Context context) {
        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(context.getResources().getAssets().open(TEST_FILE), "GBK"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized DataUnit getDataUnit() {
        try {
            try {
                Thread.sleep(10);//模拟蓝牙传输速度
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String line;
            String[] dataRead;
            double dataUnit[] = new double[DataUnit.SENSER_NUMBER + 1];
            if ((line = bufferedReader.readLine()) != null) {
                dataRead = line.split(" ");
                for (int i = 0; i < DataUnit.SENSER_NUMBER + 1; i++) {
                    dataUnit[i] = Double.parseDouble(dataRead[i]);
                    DataUnitForSQL dataUnitForSQL=new DataUnitForSQL(dataUnit[0],dataUnit[1],dataUnit[2],
                            dataUnit[3],dataUnit[4],dataUnit[5],dataUnit[6],dataUnit[7],
                            dataUnit[8],dataUnit[9],dataUnit[10],dataUnit[11],dataUnit[12]);
                    dataUnitForSQL.save();
                }
                return new DataUnit(dataUnit);
            } else {
                Log.e(TAG, "data read null");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
