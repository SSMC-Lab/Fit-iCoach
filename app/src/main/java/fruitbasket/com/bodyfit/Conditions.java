package fruitbasket.com.bodyfit;

import android.os.Environment;

import fruitbasket.com.bodyfit.utils.SPUtils;

public class Conditions {
    private static final Conditions conditions = new Conditions();

    public static final String APP_FILE_DIR = Environment.getExternalStorageDirectory() + "/BodyFit";

    public static final int MAX_SAMPLE_NUMBER = 5;
    // public static final int MID_SPAN=MAX_SAMPLE_NUMBER/2+1;
    public static final int MID_SPAN = 3;
    public static final int MAX_COLLECT_DATA = 5;  //用于判断切割点的数据量

    public static final int NUM_PRE_EXERCISE = 15;    //规定每组动作的最大次数
    public static final double VALUE_OF_VARTHRESHOLD = 0.002;   //判断切割时方差的阈值
    public static final double VALUE_OF_AVRTHRESHOLD = 0.15 + 0.8;   //判断切割时平均值的阈值
    public static final int MAX_SAMPLES_OF_ACTIONS = 400; //单个动作中最大的samples数
    public static final int MIN_SAMPLES_OF_ACTIONS = 80; //单个动作中最小的samples数

    //Massage.what
    public static final int MESSAGE_BLUETOOTH_TEST = 0x100;
    public static final int MESSAGE_ERROR_JSON = 0x101;
    public static final int MESSAGE_EXERCISE_TYPE = 0x102;
    public static final int MESSAGE_EXERCESE_STATUS = 0x103;
    public static final int MESSAGE_EXERCISE_TIME = 0x104;

    //Bundle keys
    public static final String JSON_KEY_ITEMS_PRE_SECOND = "items_pre_second";
    public static final String JSON_KEY_JOSNERROR = "error_json_string";
    public static final String JSON_KEY_EXERCISE_TYPE = "exercise_type";
    public static final String JSON_KEY_EXERCISE_TYPE_MANUAL = "exercise_type_manual";
    public static final String DTW_TIME = "dtw_time";
    public static final String DTW_TIME_MANUAL = "dtw_time_manual";
    public static final String REPETITION_SCORE = "repetition_score";
    public static final String GROUP_EXERCISE_ASSESS = "group_exercise_assess";
    public static final String SET_SCORE = "set_score";
    public static final String ACTION_NUM = "action_num"; //动作次数
    public static final String NUM_OF_ACTION_ARRAY = "num_of_action_array";//十五个动作各自的次数
    public static final String DTW_SCORE = "dtw_score";
    public static final String DTW = "dtw";
    public static final String TIME_TEST = "time_test";
    public static final String AX_TEST = "ax_test";
    public static final String AY_TEST = "ay_test";
    public static final String AZ_TEST = "az_test";
    public static final String GX_TEST = "gx_test";
    public static final String GY_TEST = "gy_test";
    public static final String GZ_TEST = "gz_test";

    public static final String DURATION = "duration"; //持续时间
    public static final String GROUP_MAX_TIME = "group_max_time";
    public static final String GROUP_MIN_TIME = "group_min_time";
    public static final String GROUP_AVE_TIME = "group_ave_time";
    public static final String GROUP_MAX_DTW = "group_max_dtw";
    public static final String GROUP_MIN_DTW = "group_min_dtw";
    public static final String GROUP_AVE_DTW = "group_ave_dtw";

    public static final String GROUP_MAX_TIME1 = "group_max_time1";
    public static final String GROUP_MIN_TIME1 = "group_min_time1";
    public static final String GROUP_AVE_TIME1 = "group_ave_time1";
    public static final String GROUP_MAX_DTW1 = "group_max_dtw1";
    public static final String GROUP_MIN_DTW1 = "group_min_dtw1";
    public static final String GROUP_AVE_DTW1 = "group_ave_dtw1";

    public static final String GROUP_MAX_TIME2 = "group_max_time2";
    public static final String GROUP_MIN_TIME2 = "group_min_time2";
    public static final String GROUP_AVE_TIME2 = "group_ave_time2";
    public static final String GROUP_MAX_DTW2 = "group_max_dtw2";
    public static final String GROUP_MIN_DTW2 = "group_min_dtw2";
    public static final String GROUP_AVE_DTW2 = "group_ave_dtw2";

    public static final String GROUP_MAX_TIME3 = "group_max_time3";
    public static final String GROUP_MIN_TIME3 = "group_min_time3";
    public static final String GROUP_AVE_TIME3 = "group_ave_time3";
    public static final String GROUP_MAX_DTW3 = "group_max_dtw3";
    public static final String GROUP_MIN_DTW3 = "group_min_dtw3";
    public static final String GROUP_AVE_DTW3 = "group_ave_dtw3";

    public static final String TOTAL_TIME = "total_time";


    //json keys
    public static final String TIME = "time";
    public static final String AX = "ax";
    public static final String AY = "ay";
    public static final String AZ = "az";
    public static final String GX = "gx";
    public static final String GY = "gy";
    public static final String GZ = "gz";
    public static final String MX = "mx";
    public static final String MY = "my";
    public static final String MZ = "mz";
    public static final String P1 = "p1";
    public static final String P2 = "p2";
    public static final String P3 = "p3";

    public static final String AX_MANUAL = "ax_manual";
    public static final String AY_MANUAL = "ay_manual";
    public static final String AZ_MANUAL = "az_manual";
    public static final String GX_MANUAL = "gx_manual";
    public static final String GY_MANUAL = "gy_manual";
    public static final String GZ_MANUAL = "gz_manual";
    public static final String MX_MANUAL = "mx_manual";
    public static final String MY_MANUAL = "my_manual";
    public static final String MZ_MANUAL = "mz_manual";
    public static final String P1_MANUAL = "p1_manual";
    public static final String P2_MANUAL = "p2_manual";
    public static final String P3_MANUAL = "p3_manual";

    //All Action Chinese Name
    // "///"means the action dont use now
    /*public final static String exercise_1="交替哑铃弯举_1";
    public final static String exercise_2="锤式弯举_2";
    public final static String exercise_3="俯身飞鸟_3";
    public final static String exercise_4="杠铃划船_4";
    public final static String exercise_5="机械弯曲_5";
    public final static String exercise_6="反向飞鸟运动_6";
    public final static String exercise_7="器械推胸机动作_7";
    public final static String exercise_8="哑铃提肩_8";
    public final static String exercise_9="站姿飞鸟_9";
    public final static String too_slow="太慢";
    public final static String too_fast="太快";
    */
    // "///"means the action dont use now
    public final static String exercise_1 = "1-坐姿哑铃臂弯举";
    public final static String exercise_2 = "2-锤式弯举";
    public final static String exercise_3 = "3-哑铃侧卧外旋";
    public final static String exercise_4 = "4-器械坐姿推胸";
    public final static String exercise_5 = "5-反握下拉";
    public final static String exercise_6 = "6-双手拉绳";
    public final static String exercise_7 = "7-站姿飞鸟";
    public final static String exercise_8 = "8-坐姿推肩";
    public final static String exercise_9 = "9-阿诺德推举";
    public final static String exercise_10 = "10-单臂哑铃侧屈";
    public final static String exercise_11 = "11-低位杠铃划船";
    public final static String exercise_12 = "12-杠铃卧推";
    public final static String exercise_13 = "13-蝴蝶机夹胸";
    public final static String exercise_14 = "14-上斜哑铃飞鸟";
    public final static String exercise_15 = "15-背后拉力器弯举";

    public final static String[] exerciseName = {"坐姿哑铃弯举", "锤式弯举", "哑铃侧卧外旋", "坐姿器械推胸",
            "正握下拉", "弹力绳高位面拉", "站姿飞鸟", "坐姿推肩", "阿诺德推举", "单臂哑铃侧曲", "杠铃划船", "杠铃卧推",
            "蝴蝶机夹胸", "上斜哑铃飞鸟", "背后拉力器弯举"};
//    public final static String exercise_16 = "16-双手拉绳";
//    public final static String exercise_17 = "17-拉力器夹胸";
//    public final static String exercise_18= "18-拉力器双臂上拉";
//    public final static String exercise_19 = "19-哑铃侧卧外旋";
//    public final static String exercise_20 = "20-背后拉力器弯举";

    //    public final static String exercise_16 = "16-站姿飞鸟";
    //  public final static String exercise_17 = "17-坐姿推肩";
    public final static String too_slow = "太慢";
    public final static String too_fast = "太快";
    public final static String User_grade = "User_grade";
    //requestCode
    public final static int SOCIETY_R_CODE = 1;  //运动圈请求码
    public final static int EXERCISE_R_CODE = 2; //运动界面请求码
    public final static int PERSON_R_CODE = 3;   //个人信息界面请求码

    public final static String EXERCISE_SOCIETY_CONTENT = "exercise_society_content";
    public final static String EXERCISE_SOCIETY_IMAGE = "exercise_society_image";

    public final static String CURRENT_SCORES="current_scores";
    public final static String CURRENT_HEATIN="current_heat_in";
    public final static String RECOMMENDED_FOOD="recommended_food";

    public final static String USER_NAME="user_name";

    public final static int recommendVegetableCount=17;
    //模板数（动作数）
    public final static int EXERCISE_NUM = 15;


    private Conditions() {
    }

    public static Conditions getInstance() {
        return conditions;
    }
}
