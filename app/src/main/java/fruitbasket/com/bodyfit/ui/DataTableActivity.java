package fruitbasket.com.bodyfit.ui;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import org.litepal.crud.DataSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.R;
import fruitbasket.com.bodyfit.analysis.EarthMoverDistance;
import fruitbasket.com.bodyfit.data.DataUnitForSQL;
import fruitbasket.com.bodyfit.utils.SPUtils;
import win.smartown.android.library.tableLayout.TableAdapter;
import win.smartown.android.library.tableLayout.*;


public class DataTableActivity extends AppCompatActivity {

    static {
        System.loadLibrary("jni-dtw");
    }

    private List<Double> ax1, ay1, az1, gx1, gy1, gz1, mx1, my1, mz1, p11, p21, p31;
    private List<Double> ax2, ay2, az2, gx2, gy2, gz2, mx2, my2, mz2, p12, p22, p32;
    private List<Double> ax3, ay3, az3, gx3, gy3, gz3, mx3, my3, mz3, p13, p23, p33;

    double aInterval[] = {-15, -12, -9, -6, -3, 0, 3, 6, 9, 12, 15};
    double gInterval[] = {-200, -160, -120, -80, -40, 0, 40, 80, 120, 160, 200};
    double pInterval[];
    private int bounds = 10000;
    private int dataSize1 = 0;
    private int dataSize2 = 0;
    private int count = 0, index = 0;
    private int actionCount = 0;
    private double time[][];
    private List<Content> contentList;
    private List<Content2> kurtosisList;
    private List<Content3> scoreList;
    private TableLayout tableLayout;
    private TableLayout kurtosisLayout;
    private TableLayout scoreLayout;


    private BarChart barChart1;
    private BarChart barChart2;
    private BarChart barChart3;
    private BarChartManager barChartManager1;
    private BarChartManager barChartManager2;
    private BarChartManager barChartManager3;
    private List<String> names;
    private List<Integer> colours;
    private ArrayList<Float> xValues;
    private List<List<Float>> y1Values;
    private List<List<Float>> y2Values;
    private List<List<Float>> y3Values;
    private String TAG = "DataTableActivity";

    private double[] groupDtwScore = new double[3];
    private double[] groupTime = new double[3];
    private double[] groupScore = new double[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_data_table);
        tableLayout = (TableLayout) findViewById(R.id.main_table);
        kurtosisLayout = (TableLayout) findViewById(R.id.kurtosis_table);
        scoreLayout = (TableLayout) findViewById(R.id.score_table);
        initList();
        initContent();
        initBarchart();
        firstRowAsTitle();

        readFile();
        queryDatabase();
//        firstColumnAsTitle();
    }

    private void initList() {
        ax1 = new ArrayList<>();
        ay1 = new ArrayList<>();
        az1 = new ArrayList<>();

        gx1 = new ArrayList<>();
        gy1 = new ArrayList<>();
        gz1 = new ArrayList<>();

        mx1 = new ArrayList<>();
        my1 = new ArrayList<>();
        mz1 = new ArrayList<>();

        p11 = new ArrayList<>();
        p21 = new ArrayList<>();
        p31 = new ArrayList<>();

        ax2 = new ArrayList<>();
        ay2 = new ArrayList<>();
        az2 = new ArrayList<>();

        gx2 = new ArrayList<>();
        gy2 = new ArrayList<>();
        gz2 = new ArrayList<>();

        mx2 = new ArrayList<>();
        my2 = new ArrayList<>();
        mz2 = new ArrayList<>();

        p12 = new ArrayList<>();
        p22 = new ArrayList<>();
        p32 = new ArrayList<>();

        ax3 = new ArrayList<>();
        ay3 = new ArrayList<>();
        az3 = new ArrayList<>();

        gx3 = new ArrayList<>();
        gy3 = new ArrayList<>();
        gz3 = new ArrayList<>();

        mx3 = new ArrayList<>();
        my3 = new ArrayList<>();
        mz3 = new ArrayList<>();

        p13 = new ArrayList<>();
        p23 = new ArrayList<>();
        p33 = new ArrayList<>();
    }

    private void initBarchart() {

        barChart1 = (BarChart) findViewById(R.id.bar_chart1);
        barChart2 = (BarChart) findViewById(R.id.bar_chart2);
        barChart3 = (BarChart) findViewById(R.id.bar_chart3);

        barChartManager1 = new BarChartManager(barChart1);
        barChartManager2 = new BarChartManager(barChart2);
        barChartManager3 = new BarChartManager(barChart3);

        //设置x轴的数据
        xValues = new ArrayList<>();
        xValues.add(1f);
        xValues.add(2f);
        xValues.add(3f);
        xValues.add(4f);
        xValues.add(5f);
        xValues.add(6f);
        xValues.add(7f);
        xValues.add(8f);
        xValues.add(9f);


        //设置y轴的数据()
        y1Values = new ArrayList<>();
        y2Values = new ArrayList<>();
        y3Values = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            List<Float> yValue = new ArrayList<>();
            yValue.add(0f);
            yValue.add(0f);
            yValue.add(0f);
            yValue.add(0f);
            yValue.add(0f);
            yValue.add(0f);
            yValue.add(0f);
            yValue.add(0f);
            yValue.add(0f);
            y1Values.add(yValue);
            y2Values.add(yValue);
            y3Values.add(yValue);
        }

        //颜色集合
        int color = Color.parseColor("#5CACEE");
        colours = new ArrayList<>();
        colours.add(color);
        colours.add(Color.GREEN);
        colours.add(Color.BLUE);
        colours.add(Color.RED);
        colours.add(Color.CYAN);


        //线的名字集合
        names = new ArrayList<>();
        names.add("EMD");


        //创建多条折线的图表
        barChartManager1.showBarChart(xValues, y1Values.get(0), names.get(0), colours.get(0));
        barChartManager2.showBarChart(xValues, y2Values.get(0), names.get(0), colours.get(0));
        barChartManager3.showBarChart(xValues, y3Values.get(0), names.get(0), colours.get(0));

    }

    private void initContent() {
        contentList = new ArrayList<>();
        kurtosisList = new ArrayList<>();
        scoreList = new ArrayList<>();
        actionCount = (int) SPUtils.get(Conditions.ACTION_NUM, 0);
        String maxTime1 = new DecimalFormat("0.00").format((float) SPUtils.get(Conditions.GROUP_MAX_TIME1, 0.00f));
        String minTime1 = new DecimalFormat("0.00").format((float) SPUtils.get(Conditions.GROUP_MIN_TIME1, 0.00f));
        String aveTime1 = new DecimalFormat("0.00").format((float) SPUtils.get(Conditions.GROUP_AVE_TIME1, 0.00f));
        float maxDtw1 = ((float) SPUtils.get(Conditions.GROUP_MAX_DTW1, 0.00f));
        float minDtw1 = ((float) SPUtils.get(Conditions.GROUP_MIN_DTW1, 0.00f));
        float aveDtw1 = ((float) SPUtils.get(Conditions.GROUP_AVE_DTW1, 0.00f));
        String maxTime2 = new DecimalFormat("0.00").format((float) SPUtils.get(Conditions.GROUP_MAX_TIME2, 0.00f));
        String minTime2 = new DecimalFormat("0.00").format((float) SPUtils.get(Conditions.GROUP_MIN_TIME2, 0.00f));
        String aveTime2 = new DecimalFormat("0.00").format((float) SPUtils.get(Conditions.GROUP_AVE_TIME2, 0.00f));
        float maxDtw2 = ((float) SPUtils.get(Conditions.GROUP_MAX_DTW2, 0.00f));
        float minDtw2 = ((float) SPUtils.get(Conditions.GROUP_MIN_DTW2, 0.00f));
        float aveDtw2 = ((float) SPUtils.get(Conditions.GROUP_AVE_DTW2, 0.00f));
        String maxTime3 = new DecimalFormat("0.00").format((float) SPUtils.get(Conditions.GROUP_MAX_TIME3, 0.00f));
        String minTime3 = new DecimalFormat("0.00").format((float) SPUtils.get(Conditions.GROUP_MIN_TIME3, 0.00f));
        String aveTime3 = new DecimalFormat("0.00").format((float) SPUtils.get(Conditions.GROUP_AVE_TIME3, 0.00f));
        float maxDtw3 = ((float) SPUtils.get(Conditions.GROUP_MAX_DTW3, 0.00f));
        float minDtw3 = ((float) SPUtils.get(Conditions.GROUP_MIN_DTW3, 0.00f));
        float aveDtw3 = ((float) SPUtils.get(Conditions.GROUP_AVE_DTW3, 0.00f));

        scoreList.add(new Content3("   ", "第一组", "第二组", "第三组"));
        scoreList.add(new Content3("组评分", "0.00", "0.00", "0.00"));

        contentList.add(new Content("   ", "最大", "最小", "均值", "最大", "最小", "均值"));
        contentList.add(new Content("第一组", maxTime1, minTime1, aveTime1, (int) maxDtw1 + "", (int) minDtw1 + "", (int) aveDtw1 + ""));
        contentList.add(new Content("第二组", maxTime2, minTime2, aveTime2, (int) maxDtw2 + "", (int) minDtw2 + "", (int) aveDtw2 + ""));
        contentList.add(new Content("第三组", maxTime3, minTime3, aveTime3, (int) maxDtw3 + "", (int) minDtw3 + "", (int) aveDtw3 + ""));

        kurtosisList.add(new Content2("组别\\峰态系数", "gx", "gy", "gz", "mx", "my", "mz", "p1", "p2", "p3"));
        kurtosisList.add(new Content2("1", "0.00", "0.00", "0.00",
                "0.00", "0.00", "0.00", "0.00", "0.00", "0.00"));
        kurtosisList.add(new Content2("2", "0.00", "0.00", "0.00",
                "0.00", "0.00", "0.00", "0.00", "0.00", "0.00"));
        kurtosisList.add(new Content2("3", "0.00", "0.00", "0.00",
                "0.00", "0.00", "0.00", "0.00", "0.00", "0.00"));


        groupDtwScore[0] = (100 / ((1 + Math.pow(Math.E, aveDtw1 * 0.0005 - 5.0)) * 1.0));
        groupDtwScore[1] = (100 / ((1 + Math.pow(Math.E, aveDtw2 * 0.0005 - 5.0)) * 1.0));
        groupDtwScore[2] = (100 / ((1 + Math.pow(Math.E, aveDtw3 * 0.0005 - 5.0)) * 1.0));
        double time1 = (float) SPUtils.get(Conditions.GROUP_AVE_TIME1, 0.00f);
        double time2 = (float) SPUtils.get(Conditions.GROUP_AVE_TIME2, 0.00f);
        double time3 = (float) SPUtils.get(Conditions.GROUP_AVE_TIME3, 0.00f);
        if (time1 <= 3)
            groupTime[0] = (100 / ((1.0 + Math.pow(Math.E, (-1) * time1 / 0.4 + 3.75)) * 1.0));
        else
            groupTime[0] = (100 / ((1.0 + Math.pow(Math.E, (-1) * (6 - time1) / 0.4 + 3.75)) * 1.0));
        if (time2 <= 3)
            groupTime[1] = (100 / ((1.0 + Math.pow(Math.E, (-1) * time2 / 0.4 + 3.75)) * 1.0));
        else
            groupTime[1] = (100 / ((1.0 + Math.pow(Math.E, (-1) * (6 - time2) / 0.4 + 3.75)) * 1.0));
        if (time3 <= 3)
            groupTime[2] = (100 / ((1.0 + Math.pow(Math.E, (-1) * time3 / 0.4 + 3.75)) * 1.0));
        else
            groupTime[2] = (100 / ((1.0 + Math.pow(Math.E, (-1) * (6 - time3) / 0.4 + 3.75)) * 1.0));

        groupScore[0] = groupScore[1] = groupScore[2] = 0;

    }

    //将第一行作为标题
    private void firstRowAsTitle() {
        //fields是表格中要显示的数据对应到Content类中的成员变量名，其定义顺序要与表格中显示的相同
        final String[] fields = {"空", "最大s", "最小s", "均值s", "最大", "最小", "均值"};
        TableAdapter adapter = new TableAdapter() {
            @Override
            public int getColumnCount() {
                return fields.length;
            }

            @Override
            public String[] getColumnContent(int position) {
                int rowCount = contentList.size();
                String contents[] = new String[rowCount];
                try {
                    Field field = Content.class.getDeclaredField(fields[position]);
                    field.setAccessible(true);
                    for (int i = 0; i < rowCount; i++) {
                        contents[i] = (String) field.get(contentList.get(i));
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return contents;
            }
        };
        tableLayout.setAdapter(adapter);

        final String[] fields2 = {"empty", "gx", "gy", "gz", "mx", "my", "mz", "p1", "p2", "p3"};
        TableAdapter durationAdapter = new TableAdapter() {
            @Override
            public int getColumnCount() {
                return fields2.length;
            }

            @Override
            public String[] getColumnContent(int position) {
                int rowCount = kurtosisList.size();
                String contents[] = new String[rowCount];
                try {
                    Field field = Content2.class.getDeclaredField(fields2[position]);
                    field.setAccessible(true);
                    for (int i = 0; i < rowCount; i++) {
                        contents[i] = (String) field.get(kurtosisList.get(i));
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return contents;
            }
        };
        kurtosisLayout.setAdapter(durationAdapter);

        final String[] fields3 = {"空", "第一组", "第二组", "第三组"};
        TableAdapter scoreAdapter = new TableAdapter() {
            @Override
            public int getColumnCount() {
                return fields3.length;
            }

            @Override
            public String[] getColumnContent(int position) {
                int rowCount = scoreList.size();
                String contents[] = new String[rowCount];
                try {
                    Field field = Content3.class.getDeclaredField(fields3[position]);
                    field.setAccessible(true);
                    for (int i = 0; i < rowCount; i++) {
                        contents[i] = (String) field.get(scoreList.get(i));
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return contents;
            }
        };
        scoreLayout.setAdapter(scoreAdapter);
    }


    //将第一列作为标题
    private void firstColumnAsTitle() {
        tableLayout.setAdapter(new TableAdapter() {
            @Override
            public int getColumnCount() {
                return contentList.size();
            }

            @Override
            public String[] getColumnContent(int position) {
                return contentList.get(position).toArray();
            }
        });
    }

    private static String newRandomNumber() {
        return 0.00 + "";
    }

    public static class Content {

        private String 空;
        private String 最大s;
        private String 最小s;
        private String 均值s;
        private String 最大;
        private String 最小;
        private String 均值;

        public Content(String Empty, String Max1, String Min1, String Balance1, String Max2, String Min2, String Balance2) {
            this.空 = Empty;
            this.最大s = Max1;
            this.最小s = Min1;
            this.均值s = Balance1;
            this.最大 = Max2;
            this.最小 = Min2;
            this.均值 = Balance2;
        }

        public String[] toArray() {
            return new String[]{最大s, 最小s, 均值s, 最大, 最小, 均值};
        }
    }

    public static class Content2 {
        private String empty;
        private String gx;
        private String gy;
        private String gz;
        private String mx;
        private String my;
        private String mz;
        private String p1;
        private String p2;
        private String p3;

        public Content2(String empty, String gx, String gy,
                        String gz, String mx, String my, String mz, String p1, String p2, String p3) {
            this.empty = empty;
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

        public String[] toArray() {
            return new String[]{empty, gx, gy, gz, mx, my, mz, p1, p2, p3};
        }
    }

    public static class Content3 {
        String 空;
        private String 第一组;
        private String 第二组;
        private String 第三组;

        public Content3(String empty, String first, String second, String third) {
            this.空 = empty;
            this.第一组 = first;
            this.第二组 = second;
            this.第三组 = third;
        }

        public String[] toArray() {
            return new String[]{空, 第一组, 第二组, 第三组};
        }
    }

    private void readFile() {
        try {
            String dir_path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(dir_path);
            if (!file.exists())
                file.mkdir();
            File fileTime = new File(dir_path + File.separator + "Time.txt");
            if (!fileTime.exists())
                fileTime.createNewFile();
            FileInputStream outTime = new FileInputStream(fileTime);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(outTime));
            time = new double[100][2];
            String line;
            String[] dataRead;
            line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                dataRead = line.split("\t");
                time[count][0] = Double.parseDouble(dataRead[0]);
                time[count][1] = Double.parseDouble(dataRead[1]);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateFirst(int limit) {
        index = 0;
        double emd_sum = 0;
        List<Float> list = new ArrayList<>();
        for (int i = 1; i <= limit; i++) {
            List<DataUnitForSQL> dataUnits1 = DataSupport.where("time>? and time<?", time[i - 1][1] + "", time[i][0] + "")
                    .select("ax", "ay", "az", "gx", "gy", "gz", "mx", "my", "mz", "p1", "p2", "p3")
                    .find(DataUnitForSQL.class);
            List<DataUnitForSQL> dataUnits2 = DataSupport.where("time>? and time<?", time[i - 1][0] + "", time[i - 1][1] + "")
                    .select("ax", "ay", "az", "gx", "gy", "gz")
                    .find(DataUnitForSQL.class);
            List<DataUnitForSQL> dataUnits3 = DataSupport.where("time>? and time<?", time[i][0] + "", time[i][1] + "")
                    .select("ax", "ay", "az", "gx", "gy", "gz")
                    .find(DataUnitForSQL.class);

            dataSize1 += dataUnits1.size();
            for (int j = 0; j < dataUnits1.size(); j++) {
                ax1.add(dataUnits1.get(j).getAx());
                ay1.add(dataUnits1.get(j).getAy());
                az1.add(dataUnits1.get(j).getAz());
                gx1.add(dataUnits1.get(j).getGx());
                gy1.add(dataUnits1.get(j).getGy());
                gz1.add(dataUnits1.get(j).getGz());
                mx1.add(dataUnits1.get(j).getMx());
                my1.add(dataUnits1.get(j).getMy());
                mz1.add(dataUnits1.get(j).getMz());
                p11.add(dataUnits1.get(j).getP1());
                p21.add(dataUnits1.get(j).getP2());
                p31.add(dataUnits1.get(j).getP3());
                index++;
            }
            double emd_ax1[] = new double[dataUnits2.size()];
            double emd_ax2[] = new double[dataUnits3.size()];
            double emd_ay1[] = new double[dataUnits2.size()];
            double emd_ay2[] = new double[dataUnits3.size()];
            double emd_az1[] = new double[dataUnits2.size()];
            double emd_az2[] = new double[dataUnits3.size()];

            for (int k = 0; k < dataUnits2.size(); k++) {
                emd_ax1[k] = dataUnits2.get(k).getAx();
                emd_ay1[k] = dataUnits2.get(k).getAy();
                emd_az1[k] = dataUnits2.get(k).getAz();
            }
            for (int k = 0; k < dataUnits3.size(); k++) {
                emd_ax2[k] = dataUnits3.get(k).getAx();
                emd_ay2[k] = dataUnits3.get(k).getAy();
                emd_az2[k] = dataUnits3.get(k).getAz();
            }
            double MLA1[] = MLA(emd_ax1, emd_ay1, emd_az1);
            double MLA2[] = MLA(emd_ax2, emd_ay2, emd_az2);
            float emd = EarthMoverDistance.EarthMoversDistance(MLA1, MLA2, MLA1.length, MLA2.length);
            emd_sum += emd;
            list.add(emd);
        }
        for (int k = list.size() + 1; k <= 10; k++)
            list.add(0f);
        y1Values.set(0, list);
        barChartManager1.showBarChart(xValues, y1Values.get(0), names.get(0), colours.get(0));
        double gxco = ComputeAveDivideVariance(gx1, index);
        double gyco = ComputeAveDivideVariance(gy1, index);
        double gzco = ComputeAveDivideVariance(gz1, index);
        double mxco = getMean(gx1, index);
        double myco = getMean(gy1, index);
        double mzco = getMean(gz1, index);
        double p1co = getVar(gx1, index);
        double p2co = getVar(gy1, index);
        double p3co = getVar(gz1, index);

        double gx_total = 100 / ((1 + Math.pow(Math.E, (-1) * (gxco + 3) / (6 * 1.0) * 4 - 1.0)) * 1.0);
        double emd_total = emd_sum / (limit * 1.0);
        emd_total = 100 / ((1 + Math.pow(Math.E, emd_total - 4)) * 1.0);
        double totalScore = groupTime[0] * 0.15 + groupDtwScore[0] * 0.4 + gx_total * 0.3 + emd_total * 0.15;
        groupScore[0] = totalScore;
        Log.i(TAG, "Kurtosist=" + ComputeAveDivideVariance(ax1, index) + " " + ComputeAveDivideVariance(ay1, index) + " "
                + ComputeAveDivideVariance(az1, index) + " " + ComputeAveDivideVariance(gx1, index) + " "
                + ComputeAveDivideVariance(gy1, index) + " " + ComputeAveDivideVariance(gz1, index) + " "
                + ComputeAveDivideVariance(mx1, index) + " " + ComputeAveDivideVariance(my1, index) + " "
                + ComputeAveDivideVariance(mz1, index) + " " + ComputeAveDivideVariance(p11, index) + " "
                + ComputeAveDivideVariance(p21, index) + " " + ComputeAveDivideVariance(p31, index));
        Log.i(TAG, "Mean=" + getMean(ax1, index) + " " + getMean(ay1, index) + " " + getMean(az1, index) + " "
                + getMean(gx1, index) + " " + getMean(gy1, index) + " " + getMean(gz1, index) + " "
                + getMean(mx1, index) + " " + getMean(my1, index) + " " + getMean(mz1, index) + " "
                + getMean(p11, index) + " " + getMean(p21, index) + " " + getMean(p31, index));
        Log.i(TAG, "Var=" + getVar(ax1, index) + " " + getVar(ay1, index) + " " + getVar(az1, index) + " "
                + getVar(gx1, index) + " " + getVar(gy1, index) + " " + getVar(gz1, index) + " "
                + getVar(mx1, index) + " " + getVar(my1, index) + " " + getVar(mz1, index) + " "
                + getVar(p11, index) + " " + getVar(p21, index) + " " + getVar(p31, index));
        kurtosisList.set(1, new Content2("1",
                new BigDecimal(gxco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(gyco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(gzco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(mxco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(myco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(mzco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(p1co).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(p2co).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(p3co).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()));

        scoreList.set(1, new Content3("组评分", new DecimalFormat("0.00").format(groupScore[0]),
                new DecimalFormat("0.00").format(groupScore[1]), new DecimalFormat("0.00").format(groupScore[2])));

        firstRowAsTitle();
    }

    private void calculateSecond(int limit) {
        index = 0;
        double emd_sum = 0;
        List<Float> list = new ArrayList<>();
        for (int i = 11; i <= limit; i++) {
            List<DataUnitForSQL> dataUnits1 = DataSupport.where("time>? and time<?", time[i - 1][1] + "", time[i][0] + "")
                    .select("ax", "ay", "az", "gx", "gy", "gz", "mx", "my", "mz", "p1", "p2", "p3")
                    .find(DataUnitForSQL.class);
            List<DataUnitForSQL> dataUnits2 = DataSupport.where("time>? and time<?", time[i - 1][0] + "", time[i - 1][1] + "")
                    .select("ax", "ay", "az", "gx", "gy", "gz")
                    .find(DataUnitForSQL.class);
            List<DataUnitForSQL> dataUnits3 = DataSupport.where("time>? and time<?", time[i][0] + "", time[i][1] + "")
                    .select("ax", "ay", "az", "gx", "gy", "gz")
                    .find(DataUnitForSQL.class);
            Log.i(TAG, "SQLdata" + time[i][1] + " " + time[i + 1][0] + " " + count + "  " + dataUnits1.size());
            for (int j = 0; j < dataUnits1.size(); j++) {
                ax2.add(dataUnits1.get(j).getAx());
                ay2.add(dataUnits1.get(j).getAy());
                az2.add(dataUnits1.get(j).getAz());
                gx2.add(dataUnits1.get(j).getGx());
                gy2.add(dataUnits1.get(j).getGy());
                gz2.add(dataUnits1.get(j).getGz());
                mx2.add(dataUnits1.get(j).getMx());
                my2.add(dataUnits1.get(j).getMy());
                mz2.add(dataUnits1.get(j).getMz());
                p12.add(dataUnits1.get(j).getP1());
                p22.add(dataUnits1.get(j).getP2());
                p32.add(dataUnits1.get(j).getP3());
                index++;
            }
            double emd_ax1[] = new double[dataUnits2.size()];
            double emd_ax2[] = new double[dataUnits3.size()];
            double emd_ay1[] = new double[dataUnits2.size()];
            double emd_ay2[] = new double[dataUnits3.size()];
            double emd_az1[] = new double[dataUnits2.size()];
            double emd_az2[] = new double[dataUnits3.size()];
            for (int k = 0; k < dataUnits2.size(); k++) {
                emd_ax1[k] = dataUnits2.get(k).getAx();
                emd_ay1[k] = dataUnits2.get(k).getAy();
                emd_az1[k] = dataUnits2.get(k).getAz();
            }
            for (int k = 0; k < dataUnits3.size(); k++) {
                emd_ax2[k] = dataUnits3.get(k).getAx();
                emd_ay2[k] = dataUnits3.get(k).getAy();
                emd_az2[k] = dataUnits3.get(k).getAz();
            }
            double MLA1[] = MLA(emd_ax1, emd_ay1, emd_az1);
            double MLA2[] = MLA(emd_ax2, emd_ay2, emd_az2);
            float emd = EarthMoverDistance.EarthMoversDistance(MLA1, MLA2, MLA1.length, MLA2.length);
            emd_sum += emd;
            list.add(emd);
        }
        for (int k = list.size() + 1; k <= 10; k++)
            list.add(0f);
        y2Values.set(0, list);
        barChartManager2.showBarChart(xValues, y2Values.get(0), names.get(0), colours.get(0));
        double gxco = ComputeAveDivideVariance(gx2, index);
        double gyco = ComputeAveDivideVariance(gy2, index);
        double gzco = ComputeAveDivideVariance(gz2, index);
        double mxco = getMean(gx2, index);
        double myco = getMean(gy2, index);
        double mzco = getMean(gz2, index);
        double p1co = getVar(gx2, index);
        double p2co = getVar(gy2, index);
        double p3co = getVar(gz2, index);
        double gx_total = 100 / ((1 + Math.pow(Math.E, (-1) * (gxco + 3) / (6 * 1.0) * 4 - 1.0)) * 1.0);
        double emd_total = emd_sum / ((limit - 10) * 1.0);
        emd_total = 100 / ((1 + Math.pow(Math.E, emd_total - 4)) * 1.0);
        double totalScore = groupTime[1] * 0.15 + groupDtwScore[1] * 0.4 + gx_total * 0.3 + emd_total * 0.15;
        groupScore[1] = totalScore;
        Log.i(TAG, "Kurtosist=" + ComputeAveDivideVariance(ax2, index) + " " + ComputeAveDivideVariance(ay2, index) + " "
                + ComputeAveDivideVariance(az2, index) + " " + ComputeAveDivideVariance(gx2, index) + " "
                + ComputeAveDivideVariance(gy2, index) + " " + ComputeAveDivideVariance(gz2, index) + " "
                + ComputeAveDivideVariance(mx2, index) + " " + ComputeAveDivideVariance(my2, index) + " "
                + ComputeAveDivideVariance(mz2, index) + " " + ComputeAveDivideVariance(p12, index) + " "
                + ComputeAveDivideVariance(p22, index) + " " + ComputeAveDivideVariance(p32, index));
        Log.i(TAG, "Mean=" + getMean(ax2, index) + " " + getMean(ay2, index) + " " + getMean(az2, index) + " "
                + getMean(gx2, index) + " " + getMean(gy2, index) + " " + getMean(gz2, index) + " "
                + getMean(mx2, index) + " " + getMean(my2, index) + " " + getMean(mz2, index) + " "
                + getMean(p12, index) + " " + getMean(p22, index) + " " + getMean(p32, index));
        Log.i(TAG, "Var=" + getVar(ax2, index) + " " + getVar(ay2, index) + " " + getVar(az2, index) + " "
                + getVar(gx2, index) + " " + getVar(gy2, index) + " " + getVar(gz2, index) + " "
                + getVar(mx2, index) + " " + getVar(my2, index) + " " + getVar(mz2, index) + " "
                + getVar(p12, index) + " " + getVar(p22, index) + " " + getVar(p32, index));
        kurtosisList.set(2, new Content2("2",
                new BigDecimal(gxco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(gyco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(gzco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(mxco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(myco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(mzco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(p1co).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(p2co).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(p3co).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()));

        scoreList.set(1, new Content3("组评分", new DecimalFormat("0.00").format(groupScore[0]),
                new DecimalFormat("0.00").format(groupScore[1]), new DecimalFormat("0.00").format(groupScore[2])));
        Log.i(TAG, "index=" + index);
        firstRowAsTitle();
    }

    private void calculateThird(int limit) {
        index = 0;
        double emd_sum = 0;
        List<Float> list = new ArrayList<>();
        for (int i = 21; i <= limit; i++) {
            List<DataUnitForSQL> dataUnits1 = DataSupport.where("time>? and time<?", time[i - 1][1] + "", time[i][0] + "")
                    .select("ax", "ay", "az", "gx", "gy", "gz", "mx", "my", "mz", "p1", "p2", "p3")
                    .find(DataUnitForSQL.class);
            List<DataUnitForSQL> dataUnits2 = DataSupport.where("time>? and time<?", time[i - 1][0] + "", time[i - 1][1] + "")
                    .select("ax", "ay", "az", "gx", "gy", "gz")
                    .find(DataUnitForSQL.class);
            List<DataUnitForSQL> dataUnits3 = DataSupport.where("time>? and time<?", time[i][0] + "", time[i][1] + "")
                    .select("ax", "ay", "az", "gx", "gy", "gz")
                    .find(DataUnitForSQL.class);
            Log.i(TAG, "SQLdata" + time[i][1] + " " + time[i + 1][0] + " " + count + "  " + dataUnits1.size());
            for (int j = 0; j < dataUnits1.size(); j++) {
                ax3.add(dataUnits1.get(j).getAx());
                ay3.add(dataUnits1.get(j).getAy());
                az3.add(dataUnits1.get(j).getAz());
                gx3.add(dataUnits1.get(j).getGx());
                gy3.add(dataUnits1.get(j).getGy());
                gz3.add(dataUnits1.get(j).getGz());
                mx3.add(dataUnits1.get(j).getMx());
                my3.add(dataUnits1.get(j).getMy());
                mz3.add(dataUnits1.get(j).getMz());
                p13.add(dataUnits1.get(j).getP1());
                p23.add(dataUnits1.get(j).getP2());
                p33.add(dataUnits1.get(j).getP3());
                index++;
            }
            double emd_ax1[] = new double[dataUnits2.size()];
            double emd_ax2[] = new double[dataUnits3.size()];
            double emd_ay1[] = new double[dataUnits2.size()];
            double emd_ay2[] = new double[dataUnits3.size()];
            double emd_az1[] = new double[dataUnits2.size()];
            double emd_az2[] = new double[dataUnits3.size()];
            for (int k = 0; k < dataUnits2.size(); k++) {
                emd_ax1[k] = dataUnits2.get(k).getAx();
                emd_ay1[k] = dataUnits2.get(k).getAy();
                emd_az1[k] = dataUnits2.get(k).getAz();
            }
            for (int k = 0; k < dataUnits3.size(); k++) {
                emd_ax2[k] = dataUnits3.get(k).getAx();
                emd_ay2[k] = dataUnits3.get(k).getAy();
                emd_az2[k] = dataUnits3.get(k).getAz();
            }
            double MLA1[] = MLA(emd_ax1, emd_ay1, emd_az1);
            double MLA2[] = MLA(emd_ax2, emd_ay2, emd_az2);
            float emd = EarthMoverDistance.EarthMoversDistance(MLA1, MLA2, MLA1.length, MLA2.length);
            emd_sum += emd;
            list.add(emd);
        }
        for (int k = list.size() + 1; k <= 10; k++)
            list.add(0f);
        y3Values.set(0, list);
        barChartManager3.showBarChart(xValues, y3Values.get(0), names.get(0), colours.get(0));
        double gxco = ComputeAveDivideVariance(gx3, index);
        double gyco = ComputeAveDivideVariance(gy3, index);
        double gzco = ComputeAveDivideVariance(gz3, index);
        double mxco = getMean(gx3, index);
        double myco = getMean(gy3, index);
        double mzco = getMean(gz3, index);
        double p1co = getVar(gx3, index);
        double p2co = getVar(gy3, index);
        double p3co = getVar(gz3, index);
        double gx_total = 100 / ((1 + Math.pow(Math.E, (-1) * (gxco + 3) / (6 * 1.0) * 4 - 1.0)) * 1.0);
        double emd_total = emd_sum / ((limit - 20) * 1.0);
        emd_total = 100 / ((1 + Math.pow(Math.E, emd_total - 4)) * 1.0);
        double totalScore = groupTime[2] * 0.15 + groupDtwScore[2] * 0.4 + gx_total * 0.3 + emd_total * 0.15;
        groupScore[2] = totalScore;
        Log.i(TAG, "Kurtosist=" + ComputeAveDivideVariance(ax3, index) + " " + ComputeAveDivideVariance(ay3, index) + " "
                + ComputeAveDivideVariance(az3, index) + " " + ComputeAveDivideVariance(gx3, index) + " "
                + ComputeAveDivideVariance(gy3, index) + " " + ComputeAveDivideVariance(gz3, index) + " "
                + ComputeAveDivideVariance(mx3, index) + " " + ComputeAveDivideVariance(my3, index) + " "
                + ComputeAveDivideVariance(mz3, index) + " " + ComputeAveDivideVariance(p13, index) + " "
                + ComputeAveDivideVariance(p23, index) + " " + ComputeAveDivideVariance(p33, index));
        Log.i(TAG, "Mean=" + getMean(ax3, index) + " " + getMean(ay3, index) + " " + getMean(az3, index) + " "
                + getMean(gx3, index) + " " + getMean(gy3, index) + " " + getMean(gz3, index) + " "
                + getMean(mx3, index) + " " + getMean(my3, index) + " " + getMean(mz3, index) + " "
                + getMean(p13, index) + " " + getMean(p23, index) + " " + getMean(p33, index));
        Log.i(TAG, "Var=" + getVar(ax3, index) + " " + getVar(ay3, index) + " " + getVar(az3, index) + " "
                + getVar(gx3, index) + " " + getVar(gy3, index) + " " + getVar(gz3, index) + " "
                + getVar(mx3, index) + " " + getVar(my3, index) + " " + getVar(mz3, index) + " "
                + getVar(p13, index) + " " + getVar(p23, index) + " " + getVar(p33, index));
        kurtosisList.set(3, new Content2("3",
                new BigDecimal(gxco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(gyco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(gzco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(mxco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(myco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(mzco).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(p1co).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(p2co).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                new BigDecimal(p3co).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()));
        scoreList.set(1, new Content3("组评分", new DecimalFormat("0.00").format(groupScore[0]),
                new DecimalFormat("0.00").format(groupScore[1]), new DecimalFormat("0.00").format(groupScore[2])));
        firstRowAsTitle();
    }

    private void queryDatabase() {
        Log.i(TAG, "count==" + actionCount);
        if (actionCount <= 1)
            return;
        if (actionCount <= 11) {
            calculateFirst(actionCount - 1);
        }
        if (actionCount >= 12 && actionCount <= 21) {
            calculateFirst(9);
            calculateSecond(actionCount - 1);
        } else if (actionCount >= 22) {
            calculateFirst(9);
            calculateSecond(19);
            calculateThird(actionCount - 1);
        }
    }

    //计算峰态系数
    private double ComputeAveDivideVariance(List<Double> list, int length) {
        double mean = getMean(list, length);
        double variance = getVar(list, length);
        return (mean * mean * mean * mean) / (variance * variance) - 3;
    }

    //计算方差
    private double getVar(List<Double> list, int len) {
        double variance = 0;//方差
        double sum = 0, sum2 = 0;
        for (int i = 0; i < len; i++) {
            sum += list.get(i);
            sum2 += list.get(i) * list.get(i);
        }
        variance = sum2 / len - sum / len * sum / len;
        return variance;
    }

    private double getMean(List<Double> list, int len) {
        double sum = 0;
        for (int i = 0; i < len; i++) {
            sum += list.get(i);
        }
        return sum / len;
    }

    private double[] MLA(double[] ax, double[] ay, double[] az) {
        int i;
        double[] MLA = new double[ax.length];
        for (i = 0; i < ax.length; i++) {
            MLA[i] = ax[i] * ax[i] + ay[i] * ay[i] + az[i] * az[i] - 100;
        }
        return MLA;
    }
}

class BarChartManager {
    private BarChart mBarChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;

    public BarChartManager(BarChart barChart) {
        this.mBarChart = barChart;
        leftAxis = mBarChart.getAxisLeft();
        rightAxis = mBarChart.getAxisRight();
        xAxis = mBarChart.getXAxis();
    }

    /**
     * 初始化LineChart
     */
    private void initLineChart() {
        //背景颜色
        mBarChart.setBackgroundColor(Color.WHITE);
        //网格
        mBarChart.setDrawGridBackground(false);
        //背景阴影
        mBarChart.setDrawBarShadow(false);
        mBarChart.setHighlightFullBarEnabled(false);

        //显示边界
        mBarChart.setDrawBorders(true);
        //设置动画效果
        mBarChart.animateY(1000, Easing.EasingOption.Linear);
        mBarChart.animateX(1000, Easing.EasingOption.Linear);

        mBarChart.getDescription().setEnabled(false);

        //折线图例 标签 设置
        Legend legend = mBarChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);

        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        //XY轴的设置
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
    }

    /**
     * 展示柱状图(一条)
     *
     * @param xAxisValues
     * @param yAxisValues
     * @param label
     * @param color
     */
    public void showBarChart(List<Float> xAxisValues, List<Float> yAxisValues, String label, int color) {
        initLineChart();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < xAxisValues.size(); i++) {
            entries.add(new BarEntry(xAxisValues.get(i), yAxisValues.get(i)));
        }
        // 每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, label);

        barDataSet.setColor(color);
        barDataSet.setValueTextSize(9f);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        BarData data = new BarData(dataSets);
        //设置X轴的刻度数
        xAxis.setLabelCount(9, false);
        mBarChart.setData(data);
        setYAxis(20f, 0f, 5); // 设置y轴的刻度线
    }

    /**
     * 设置Y轴值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setYAxis(float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(labelCount, false);

        rightAxis.setAxisMaximum(max);
        rightAxis.setAxisMinimum(min);
        rightAxis.setLabelCount(labelCount, false);
        mBarChart.invalidate();
    }

    /**
     * 设置X轴的值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setXAxis(float max, float min, int labelCount) {
        xAxis.setAxisMaximum(max);
        xAxis.setAxisMinimum(min);
        xAxis.setLabelCount(labelCount, false);
        mBarChart.invalidate();
    }
}