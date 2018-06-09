package fruitbasket.com.bodyfit.helper;

import org.json.JSONException;
import org.json.JSONObject;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.data.DataUnit;

public class JSONHelper {
    private static final JSONHelper jsonHelper=new JSONHelper();

    private JSONHelper(){}

    public static JSONHelper getInstance(){
        return jsonHelper;
    }

    /**
     * 将json格式的字符串转化成double数组，储存在DataUnit对象中，并返回对象
     * @param jsonString
     * @return
     * @throws JSONException
     */
    public static DataUnit parser(String jsonString) throws JSONException {

        JSONObject jsonObject=new JSONObject(jsonString);

        double[] array={
                jsonObject.getDouble(Conditions.AX),//即jsonObject.getDouble("ax"),
                jsonObject.getDouble(Conditions.AY),
                jsonObject.getDouble(Conditions.AZ),
                jsonObject.getDouble(Conditions.GX),
                jsonObject.getDouble(Conditions.GY),
                jsonObject.getDouble(Conditions.GZ),
                jsonObject.getDouble(Conditions.MX),
                jsonObject.getDouble(Conditions.MY),
                jsonObject.getDouble(Conditions.MZ),
                jsonObject.getDouble(Conditions.P1),
                jsonObject.getDouble(Conditions.P2),
                jsonObject.getDouble(Conditions.P3)
        };

        DataUnit dataUnit=new DataUnit(jsonObject.getDouble(Conditions.TIME), array);

        return dataUnit;
    }
}
