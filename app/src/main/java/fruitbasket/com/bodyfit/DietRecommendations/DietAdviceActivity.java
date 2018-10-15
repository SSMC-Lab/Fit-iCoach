package fruitbasket.com.bodyfit.DietRecommendations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.R;

public class DietAdviceActivity extends AppCompatActivity {

    private List<Food> foodList = new ArrayList<>();


    private String [] vegetableName={"生菜","娃娃菜","油麦菜","菜心","西兰花",
            "胡萝卜","白萝卜","西红柿","茄子","黄瓜",
            "南瓜","冬瓜","香菇","韭菜","土豆","芋头","莲藕"};
    private int [] vegetableHeat={16,31,24,36,39,23,20,23,16,23,12,26,29,77,81,73};
    private String [] fruitName={"苹果","香蕉","橙子","猕猴桃","桃子","小番茄","梨","葡萄","火龙果"};
    private int [] fruitHeat={54,93,48,61,51,22,50,44,60};
    private String [] milkName={"纯牛奶","低脂牛奶","脱脂牛奶","酸奶"};
    private int [] milkHeat={68,43,38,72};
    private String [] beanName={"豆腐","豆浆","绿豆","红豆","腐竹","千张"};
    private int [] beanHeat={82,33,329,324,461,262};
    private String [] oilName={"沙拉油","花生油","食物调和油","橄榄油"};
    private int [] oilHeat={898,899,884,899};
    private String [] fishName={"鲈鱼","黄花鱼","鳕鱼","基围虾","螃蟹","鱿鱼"};
    private int [] fishHeat={105,99,88,101,103,75};
    private String [] cerealName={"米饭","面条","面包","小米粥","玉米","红薯","紫薯"};
    private int [] cerealHeat={116,286,313,46,112,99,189};
    private String []meatName={"猪肉","牛肉","鸡肉","羊肉"};
    private int [] meatHeat={143,106,167,203};
    private String [] eggName={"水煮蛋","煎鸡蛋","咸鸭蛋"};
    private int [] eggHeat={151,199,190};

    private double vegetableWeight=0,fruitWeight=0,seafoodWeight=0,meatWeight=0,beanWeight,cerealWeight,eggWeight,milkWeight,oilWeight;
    private int vegetableKey=0,fruitKey=0,cerealKey=0,meatKey=0,beanKey=0,seaFoodKey=0,eggKey=0,oilKey=1,milkKey=0;
    private double heatIn=0;
    private double proportion[]={0.0305,0.0383,0.65,0.0686,0.0652,0.0228,0.0257,0.0217,0.0772};

    public static final String TAG="DietAdviceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_diet_advice);
        init();
        FoodAdapter adapter = new FoodAdapter(DietAdviceActivity.this, R.layout.list_item_diet_advice, foodList);
        ListView listView = (ListView) findViewById(R.id.diet_list_view);
        listView.setAdapter(adapter);
    }

    private void init() {
        Bundle bundle=getIntent().getBundleExtra(Conditions.CURRENT_SCORES);
        heatIn=bundle.getDouble(Conditions.CURRENT_HEATIN);
//        vegetableKey=bundle.getInt(Conditions.RECOMMENDED_FOOD);
        ArrayList list=bundle.getIntegerArrayList(Conditions.RECOMMENDED_FOOD);

        vegetableKey=(int)list.get(0); vegetableWeight=heatIn*proportion[0]/vegetableHeat[vegetableKey];
        fruitKey=(int)list.get(1); fruitWeight=heatIn*proportion[1]/fruitHeat[fruitKey];
        cerealKey=(int)list.get(2); cerealWeight=heatIn*proportion[2]/cerealHeat[cerealKey];
//        meatKey=(int)list.get(3);
        meatKey=1;
        meatWeight=heatIn*proportion[3]/meatHeat[meatKey];
        beanKey=(int)list.get(4); beanWeight=heatIn*proportion[4]/beanHeat[beanKey];
        seaFoodKey=(int)list.get(5); seafoodWeight=heatIn*proportion[5]/fishHeat[seaFoodKey];
        eggWeight=heatIn*proportion[6]/eggHeat[0];
        milkWeight=heatIn*proportion[7]/milkHeat[0];
        oilWeight=heatIn*proportion[8]/oilHeat[0];

        Food vegetable = new Food("蔬菜类:"+vegetableName[vegetableKey], (int)vegetableWeight*100+"g/day", R.drawable.diet_vegetable);
        Food cereal = new Food("粮食类:"+cerealName[cerealKey], (int)cerealWeight*100+"g/day", R.drawable.diet_rice);
        Food fruit = new Food("水果类:"+fruitName[fruitKey], (int)(fruitWeight*100)+"g/day", R.drawable.diet_fruit);

        Food dairyProduct = new Food("奶类:"+milkName[milkKey], (int)(milkWeight*100)+"g/day", R.drawable.diet_milk);
        Food oil = new Food("油类:"+oilName[oilKey], (int)(oilWeight*100)+"g/day", R.drawable.diet_oil);
        Food fish=new Food("鱼虾类:"+fishName[seaFoodKey],(int)(seafoodWeight*100)+"g/day",R.drawable.fish);
        Food bean=new Food("豆类:"+beanName[beanKey],(int)(beanWeight*100)+"g/day",R.drawable.tofu);
        Food meat=new Food("肉类:"+meatName[meatKey],(int)(meatWeight*100)+"g/day",R.drawable.steak);
        Food egg=new Food("蛋类:"+eggName[eggKey],(int)(eggWeight*100)+"g/day",R.drawable.fried_egg);
        foodList.add(vegetable);
        foodList.add(fruit);
        foodList.add(dairyProduct);
        foodList.add(bean);
        foodList.add(oil);
        foodList.add(fish);
        foodList.add(cereal);
        foodList.add(meat);
        foodList.add(egg);


    }
}

