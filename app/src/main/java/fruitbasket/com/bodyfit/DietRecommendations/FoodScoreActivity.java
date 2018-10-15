package fruitbasket.com.bodyfit.DietRecommendations;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.R;

public class FoodScoreActivity extends AppCompatActivity {

    private ListView scoreListView;
    private Button score_submit;

    private double vegetableArray[][]={
            {4,4,4,4,5,5,5,5,5,4,5,4,4,4,5,5,5},
            {4,4,5,3,3,3,3,4,3,3,3,3,3,3,4,3,2},
            {3,1,1,1,1,3,3,1,3,2,2,2,2,4,4,3,2},
            {5,5,4,3,4,3,3,3,3,5,1,3,4,2,5,3,3},
            {1,3,3,2,4,3,2,3,3,3,5,5,3,2,5,5,4},
            {3,4,2,3,5,3,2,5,4,4,3,4,3,3,3,2,3},
            {3,3,3,2,4,4,3,3,3,2,3,5,3,2,5,3,3},
            {3,4,4,3,3,2,3,3,4,3,4,3,5,1,4,3,3},
            {2,3,1,3,3,4,3,5,4,3,2,2,4,3,5,4,3},
            {4,4,3,3,4,4,4,4,3,3,3,3,3,4,3,4,4},
            {5,5,4,2,3,4,4,5,3,4,3,4,2,3,4,4,3},
            {5,5,3,5,3,5,4,5,5,5,5,5,3,5,5,5,5},
            {4,5,4,4,5,3,4,2,4,3,5,3,4,4,5,5,4},
            {3,5,3,4,1,3,5,5,3,2,4,5,5,3,3,4,5},
            {5,3,4,3,4,4,2,3,5,4,1,1,4,2,4,2,3},
            {1,4,2,3,3,1,2,3,1,2,2,2,4,4,4,4,4},
            {5,5,5,5,5,5,3,5,5,5,5,5,5,3,5,1,5},
            {3,3,3,3,3,4,3,4,3,4,2,2,4,2,4,2,2},
            {1,5,1,5,3,1,5,3,3,3,1,5,5,1,3,2,4},
            {3,5,4,5,4,4,4,4,4,4,2,3,3,4,4,2,5},
            {1,5,1,4,5,1,5,3,4,4,2,2,5,4,4,3,5},
            {3,4,2,1,3,2,2,4,4,1,1,3,5,1,5,2,4},
            {4,3,4,4,4,3,5,5,3,5,2,4,4,3,4,5,5},
            {4,4,4,4,4,4,3,4,5,5,5,5,3,3,5,5,2}
    };

    private double fruitArray[][]={
            {5,5,5,5,5,3,5,5,5},
            {3,4,3,3,5,4,3,4,4},
            {2,4,4,5,5,1,2,5,5},
            {1,3,4,4,5,4,3,4,4},
            {3,4,3,3,4,4,1,5,5},
            {2,4,5,3,4,4,4,2,3},
            {3,5,4,5,4,3,2,4,4},
            {3,1,3,4,4,3,3,5,5},
            {2,3,3,5,4,3,4,3,4},
            {5,4,4,3,3,2,2,4,5},
            {4,4,3,4,4,3,3,4,4},
            {5,5,4,4,4,5,4,5,4},
            {3,4,5,4,5,4,4,4,4},
            {3,3,5,5,5,5,4,4,3},
            {4,3,5,4,5,1,2,2,4},
            {3,2,2,2,4,5,3,3,3},
            {5,3,5,3,5,5,5,3,5},
            {3,3,4,3,4,3,4,4,4},
            {5,5,3,5,5,3,5,3,5},
            {3,4,4,4,4,4,4,4,1},
            {4,4,5,2,4,5,4,4,4},
            {3,3,5,5,5,5,3,3,3},
            {3,4,5,5,5,5,5,5,5},
            {4,5,5,5,5,4,4,4,3}
    };

    private double stapleFoodArray[][]={
            {4,5,5,5,5,5,5},
            {2,3,3,4,1,3,3},
            {3,3,3,3,3,3,3},
            {3,5,3,4,2,4,3},
            {3,5,4,3,5,5,5},
            {5,5,3,1,4,2,2},
            {5,4,2,3,4,2,3},
            {5,4,3,4,3,2,2},
            {2,2,2,2,4,3,4},
            {5,5,4,5,3,3,4},
            {4,4,3,4,3,3,3},
            {3,5,4,4,5,5,5},
            {5,5,5,4,4,4,4},
            {4,4,4,3,3,3,3},
            {4,3,2,1,2,2,4},
            {3,2,2,2,5,2,2},
            {4,5,5,5,5,5,5},
            {5,4,3,3,4,3,3},
            {4,5,3,3,3,5,5},
            {3,3,2,2,4,2,4},
            {2,4,3,3,3,4,4},
            {3,3,2,4,5,3,3},
            {5,3,3,3,5,4,4},
            {5,5,5,5,5,5,5}
    };

    private double meatArray[][]={
            {5,5,5,4},
            {4,5,4,3},
            {4,4,2,3},
            {2,4,3,3},
            {4,5,4,5},
            {5,5,5,5},
            {5,4,5,3},
            {5,5,5,5},
            {4,4,5,3},
            {5,4,4,4},
            {4,5,4,4},
            {5,5,4,4},
            {4,5,5,4},
            {2,5,5,3},
            {3,5,4,3},
            {1,2,2,2},
            {5,5,5,5},
            {3,5,4,5},
            {5,5,5,1},
            {3,5,4,5},
            {3,5,3,4},
            {4,5,4,5},
            {4,5,5,4},
            {4,4,4,4}
    };

    private double beansArray[][]={
            {5,5,5,5,5,3},
            {4,4,4,4,5,5},
            {2,2,4,3,3,3},
            {3,2,3,4,4,4},
            {3,5,4,4,4,4},
            {5,4,4,3,3,4},
            {3,4,3,3,1,2},
            {3,5,3,3,5,3},
            {2,5,4,4,3,3},
            {2,4,4,3,3,3},
            {4,4,4,3,5,5},
            {3,4,3,3,3,3},
            {4,5,5,5,5,5},
            {5,5,5,5,5,5},
            {4,2,3,3,5,4},
            {2,4,2,2,2,2},
            {5,5,5,5,5,5},
            {3,3,3,3,3,3},
            {3,5,5,5,3,3},
            {4,5,4,4,5,5},
            {3,4,3,3,3,4},
            {3,4,4,4,3,4},
            {4,5,4,4,4,4},
            {3,4,5,5,2,4}
    };

    private double seafoodArray[][]={
        {3,3,4,4,4,4},
        {3,3,4,5,3,3},
        {1,1,2,2,2,2},
        {1,2,5,4,3,4},
        {5,5,4,5,4,3},
        {3,3,4,4,3,4},
        {3,3,4,2,3,5},
        {2,2,2,3,3,5},
        {4,4,4,2,2,2},
        {4,5,4,4,3,2},
        {4,3,4,4,4,3},
        {3,5,4,5,5,4},
        {4,4,4,5,5,5},
        {5,5,3,5,5,5},
        {4,2,5,4,2,4},
        {2,2,2,5,2,5},
        {5,5,5,5,5,5},
        {3,4,4,5,5,5},
        {3,3,5,4,4,4},
        {5,5,5,5,5,5},
        {4,3,3,4,5,4},
        {3,3,4,3,5,5},
        {5,5,4,4,3,5},
        {3,3,3,1,1,1}
    };

    private String vegetableName[] = {
            "生菜","西兰花","西红柿", "胡萝卜","南瓜","香菇","土豆","莲藕"  };

    private String totalVegetableName[]={"生菜","娃娃菜","油麦菜","菜心","西兰花",
                                    "胡萝卜","白萝卜","西红柿","茄子","黄瓜",
                                    "南瓜","冬瓜","香菇","韭菜","土豆","芋头","莲藕"};

    private String fruitName[]={"苹果","猕猴桃","梨"};
    private String totalFruitName[]={"苹果","香蕉","橙子","猕猴桃","桃子","小番茄","梨","葡萄","火龙果"};
    private String stapleFoodName[]={"面包","小米粥","红薯"};
    private String totalStapleFoodName[]={"米饭","面条","面包","小米粥","玉米","红薯","紫薯"};
    private String meatName[]={"猪肉"};
    private String totalMeatName[]={"猪肉","牛肉","鸡肉","羊肉"};
    private String beansName[]={"豆腐","红豆"};
    private String totalBeansName[]={"豆腐","豆浆","绿豆","红豆","腐竹","千张"};
    private String seafoodName[]={"鲈鱼","螃蟹","鱿鱼"};
    private String totalSeafoodName[]={"鲈鱼","黄花鱼","鳕鱼","基围虾","螃蟹","鱿鱼"};
    private Map<String,Integer> vegetableMap=new HashMap<>();
    private Map<String,Integer> fruitMap=new HashMap<>();
    private Map<String,Integer> stapleFoodMap=new HashMap<>();
    private Map<String,Integer> meatMap=new HashMap<>();
    private Map<String,Integer> beansMap=new HashMap<>();
    private Map<String,Integer> seaFoodMap=new HashMap<>();


    private int[] foodImgId1 = {R.drawable.rice, R.drawable.bread, R.drawable.noodles, R.drawable.porridge,
            R.drawable.chicken, R.drawable.steak, R.drawable.pork, R.drawable.fish,
            R.drawable.prawn, R.drawable.crab, R.drawable.fried_egg, R.drawable.tofu,
            R.drawable.soybean_milk, R.drawable.yogurt, R.drawable.milk, R.drawable.tomato,
            R.drawable.mushroom, R.drawable.lettuce, R.drawable.pumpkin, R.drawable.broccoli,
            R.drawable.apple, R.drawable.banana, R.drawable.pear, R.drawable.orange};
    private int[] vegetableImgId = { R.drawable.lettuce,R.drawable.broccoli,R.drawable.tomato,R.drawable.carrot,
                                    R.drawable.pumpkin,  R.drawable.mushroom,R.drawable.potato,R.drawable.lotus_root };
    private int[] fruitImgId={R.drawable.apple,R.drawable.kiwi,R.drawable.pear};
    private int[] stapleFoodImg={R.drawable.bread,R.drawable.porridge,R.drawable.sweet_potato};
    private int[] meatImgId={R.drawable.pork};
    private int[] beansImgId={R.drawable.tofu,R.drawable.red_bean};
    private int[] seaFoodImgId={R.drawable.fish,R.drawable.crab,R.drawable.squid};
    private int vegetableNum;
    private int fruitNum;
    private int stapeFoodNum;
    private int meatNum;
    private int beansNum;
    private int seaFoodNum;
    private int userNum;
    private List<FoodScore> foodList = new ArrayList<>();
    private  Map<Integer, Double> sortedMap;
    private int recommendvegetable;
    private String TAG = "MainActivity";
    private List<Integer> list1;
    private ArrayList<Integer> recommendIndex=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_food_score
        );
        initFood();
        final FoodScoreAdapter foodScoreAdapter = new FoodScoreAdapter(FoodScoreActivity.this, R.layout.food_score_item, foodList);
        scoreListView = (ListView) findViewById(R.id.score_list_view);
        scoreListView.setAdapter(foodScoreAdapter);
        score_submit=(Button) findViewById(R.id.score_submit);
        userNum=vegetableArray.length;

        score_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list1=foodScoreAdapter.getList();
                recommendFood();
                Bundle bundle1=getIntent().getBundleExtra("energy");
                double heatIn=bundle1.getDouble(Conditions.CURRENT_HEATIN);
//                Log.i(TAG, "onClick: heatIn="+heatIn);
                Bundle bundle=new Bundle();
                Intent intent=new Intent(FoodScoreActivity.this,DietAdviceActivity.class);
                bundle.putIntegerArrayList(Conditions.RECOMMENDED_FOOD,recommendIndex);
//                bundle.putInt(Conditions.RECOMMENDED_FOOD,recommendvegetable);
                bundle.putDouble(Conditions.CURRENT_HEATIN,heatIn);
//                bundle.putDouble(Conditions.CURRENT_HEATIN,1800);

                intent.putExtra(Conditions.CURRENT_SCORES,bundle);
                startActivity(intent);

            }
        });

    }

    private int getIndex(String name,int category){
        switch (category){
            case 0:
                return vegetableMap.get(name);
            case 1:
                return fruitMap.get(name);
            case 2:
                return stapleFoodMap.get(name);
            case 3:
                return meatMap.get(name);
            case 4:
                return beansMap.get(name);
            case 5:
                return seaFoodMap.get(name);

        }
        return  0;
    }

    private void recommendFood(){

        for(int j=0;j<list1.size();j++){
            Log.i(TAG, "recommendFood: score  i="+j+" "+list1.get(j));
        }
        int i;String name;
        for(int k=0;k<6;k++){
            List<Integer> foodList=new ArrayList<>();
            switch (k){
                case 0:
                    for(i=0;i<totalVegetableName.length;i++)
                        foodList.add(0);
                    for(i=0;i<vegetableName.length;i++){
                        name=vegetableName[i];
                        foodList.set(getIndex(name,k),list1.get(i));
                    }
                    recommendIndex.add(svdTest(foodList,vegetableNum,k));
                    break;
                case 1:
                    for(i=0;i<totalFruitName.length;i++)
                        foodList.add(0);
                    for(i=0;i<fruitName.length;i++){
                        name=fruitName[i];
                        foodList.set(getIndex(name,k),list1.get(i+vegetableName.length));
                    }
                    recommendIndex.add(svdTest(foodList,fruitNum,k));
                    break;
                case 2:
                    for(i=0;i<totalStapleFoodName.length;i++)
                        foodList.add(0);
                    for(i=0;i<stapleFoodName.length;i++){
                        name=stapleFoodName[i];
                        foodList.set(getIndex(name,k),list1.get(i+vegetableName.length+fruitName.length));
                    }
                    recommendIndex.add(svdTest(foodList,stapeFoodNum,k));
                    break;
                case 3:
                    for(i=0;i<totalMeatName.length;i++)
                        foodList.add(0);
                    for(i=0;i<meatName.length;i++){
                        name=meatName[i];
                        foodList.set(getIndex(name,k),list1.get(i+vegetableName.length+fruitName.length+stapleFoodName.length));
                    }
                    recommendIndex.add(svdTest(foodList,meatNum,k));
                    break;
                case 4:
                    for(i=0;i<totalBeansName.length;i++)
                        foodList.add(0);
                    for(i=0;i<beansName.length;i++){
                        name=beansName[i];
                        foodList.set(getIndex(name,k),list1.get(i+vegetableName.length+fruitName.length+stapleFoodName.length+meatName.length));
                    }
                    recommendIndex.add(svdTest(foodList,beansNum,k));
                    break;
                case 5:
                    for(i=0;i<totalSeafoodName.length;i++)
                        foodList.add(0);
                    for(i=0;i<seafoodName.length;i++){
                        name=seafoodName[i];
                        foodList.set(getIndex(name,k),list1.get(i+vegetableName.length+fruitName.length+stapleFoodName.length+meatName.length+beansName.length));
                    }
                    recommendIndex.add(svdTest(foodList,seaFoodNum,k));
                    Log.i(TAG, "recommendFood: foodList="+foodList.size()+" k="+k);
                    for(i=0;i<recommendIndex.size();i++)
                        Log.i(TAG, "recommendFood: recommendListIndex  i="+i+" "+recommendIndex.get(i));
                    break;
            }
        }


    }

    private void initFood() {
        int j,i;
        for ( j = 0; j < vegetableName.length; j++) {
            FoodScore foodScore = new FoodScore(vegetableName[j],vegetableImgId[j], 0);
            foodList.add(foodScore);
        }
        for (j = 0; j < fruitName.length; j++) {
            FoodScore foodScore = new FoodScore(fruitName[j],fruitImgId[j], 0);
            foodList.add(foodScore);
        }
        for (j = 0; j < stapleFoodName.length; j++) {
            FoodScore foodScore = new FoodScore(stapleFoodName[j],stapleFoodImg[j], 0);
            foodList.add(foodScore);
        }
        for (j = 0; j < meatName.length; j++) {
            FoodScore foodScore = new FoodScore(meatName[j],meatImgId[j], 0);
            foodList.add(foodScore);
        }
        for (j = 0; j < beansName.length; j++) {
            FoodScore foodScore = new FoodScore(beansName[j],beansImgId[j], 0);
            foodList.add(foodScore);
        }
        for (j = 0; j < seafoodName.length; j++) {
            FoodScore foodScore = new FoodScore(seafoodName[j],seaFoodImgId[j], 0);
            foodList.add(foodScore);
        }
        for(i=0;i<totalVegetableName.length;i++)
            vegetableMap.put(totalVegetableName[i],i);
        for(i=0;i<totalFruitName.length;i++)
            fruitMap.put(totalFruitName[i],i);
        for(i=0;i<totalStapleFoodName.length;i++)
            stapleFoodMap.put(totalStapleFoodName[i],i);
        for(i=0;i<totalBeansName.length;i++)
            beansMap.put(totalBeansName[i],i);
        for(i=0;i<totalMeatName.length;i++)
            meatMap.put(totalMeatName[i],i);
        for(i=0;i<totalSeafoodName.length;i++)
            seaFoodMap.put(totalSeafoodName[i],i);

        vegetableNum=totalVegetableName.length;
        fruitNum=totalFruitName.length;
        stapeFoodNum=totalStapleFoodName.length;
        meatNum=totalMeatName.length;
        beansNum=totalBeansName.length;
        seaFoodNum=totalSeafoodName.length;

    }

    private int svdTest(List<Integer> list,int num,int category) {
        double temp[][]=vegetableArray;
        switch (category){
            case 0:
                temp=vegetableArray;
                break;
            case 1:
                temp=fruitArray;
                break;
            case 2:
                temp=stapleFoodArray;
                break;
            case 3:
                temp=meatArray;
                break;
            case 4:
                temp=beansArray;
                break;
            case 5:
                temp=seafoodArray;
                break;
        }
        double score[][]=new double[userNum+1][num];
        for(int i=0;i<userNum;i++)
            for(int j=0;j<num;j++) {

                score[i][j] = temp[i][j];
            }
        for(int i=0;i<num;i++)
            score[userNum][i]=list.get(i);

        Matrix A = new Matrix(score);
        int user = userNum;
        int foodIndex=recommend(A, user);
        return foodIndex;
    }

    private int recommend(Matrix A, int user) {
        List<Integer> listRow = new LinkedList<>();
        List<Integer> listCol = new LinkedList<>();
        List<Integer> listNum = new LinkedList<>();
        double array[][] = A.getArray();
        double estimatedScore = 0.0;
        int N = A.getRowDimension(), M = A.getColumnDimension();
        for (int i = 0; i < N; i++)
            for (int j = 0; j < M; j++) {
                if (array[i][j] != 0) {
                    listRow.add(i);
                    listCol.add(j);
                }
            }
        if (listRow.size() == 0)
            return 0;
        Map<Integer, Double> map = new TreeMap<>();
        for (int i = 0; i < M ; i++) {

            estimatedScore = svdEst(A, user, i, listNum);
//            Log.i(TAG, "recommend:  i="+i+" "+estimatedScore);
            map.put(i, estimatedScore);
            for (int j = 0; j < listNum.size(); j++) {
                if (map.containsKey(listNum.get(j)))
                    map.remove(listNum.get(j));
            }
            listNum.clear();
        }
       sortedMap = sortByValue(map);
        int count=0;
        for (Integer key : sortedMap.keySet()) {
            if(count==0){
                return key;
            }
        }
        return 0;

    }

    private double svdEst(Matrix A, int user, int item, List listNum) {
        double simTotal = 0.0, ratSimTotal = 0.0, similarity = 0.0;
        long start = System.currentTimeMillis();
        //进行奇异值分解
        SingularValueDecomposition s = A.svd();
        long end = System.currentTimeMillis();
//        System.out.println("Singular Value Decomposition elapsed time: " + (end - start));
        Matrix U = s.getU();
        Matrix S = s.getS();
        Matrix V = s.getV();
//        Log.i(TAG, "svdEst: U="+U.getRowDimension()+" "+U.getColumnDimension()+" "+
//                "S="+S.getRowDimension()+" "+S.getColumnDimension()+" V="+V.getRowDimension()+" "+
//                V.getColumnDimension());

        double temp[][] = U.getArray();
        double array3[][] = new double[temp.length][4];
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < 4; j++) {
                array3[i][j] = temp[i][j];
            }
        }
        Matrix fourthU = new Matrix(array3);

        temp = S.getArray();
        double array4[][] = new double[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                array4[i][j] = temp[i][j];
        Matrix fourthS = new Matrix(array4);

        temp=V.getArray();
        double array5[][]=new double[4][V.getColumnDimension()];
        for(int i=0;i<4;i++)
            for(int j=0;j<V.getColumnDimension();j++)
                array5[i][j]=temp[i][j];
        Matrix fourthV=new Matrix(array5);


        Matrix xformedItem= fourthU.times(fourthS).times(fourthV);

        for (int j = 0; j < A.getColumnDimension(); j++) {
            double userRating = A.get(user, j);
            if (userRating == 0 | j == item) {
                continue;
            }
            similarity = getDistance(xformedItem.getArray()[item], xformedItem.getArray()[j]);
            listNum.add(j);
//            System.out.print("the " + item + " and " + j + " similarity is : " + similarity + "\n");
            simTotal += similarity;
            ratSimTotal += similarity * userRating;
        }
        if (simTotal == 0)
            return 0;
        else
            return ratSimTotal / simTotal;
    }

    private double getDistance(double[] x, double[] y) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += (x[i] - y[i]) * (x[i] - y[i]);
        }
        sum = Math.sqrt(sum);
        return 1 / (1 + sum);
    }

    private Map sortByValue(Map unsortedMap) {
        Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }

    class ValueComparator implements Comparator {
        Map map;

        public ValueComparator(Map map) {
            this.map = map;
        }

        public int compare(Object keyA, Object keyB) {
            Comparable valueA = (Comparable) map.get(keyA);
            Comparable valueB = (Comparable) map.get(keyB);
            return valueB.compareTo(valueA);
        }
    }

    private double sim_pearson(double[] x, double y[]) {
        double sumX = 0.00, sumY = 0.00, sumX_Sq = 0.00, sumY_Sq = 0.00, sumXY = 0.00;
        int N = x.length;
        for (int i = 0; i < x.length; i++) {
            sumX += x[i];
            sumY += y[i];
            sumX_Sq += x[i]*x[i];
            sumY_Sq += y[i]*y[i];
            sumXY += (x[i] * y[i]);
        }
        double numerator = N * sumXY - sumX * sumY;
        double denominator = Math.sqrt(  (N * sumX_Sq - sumX*sumX)
                * (N * sumY_Sq - sumY * sumY));
        // 分母不能为0
        if (denominator == 0) {
            return 0;
        }
        return numerator / denominator;
    }
}
