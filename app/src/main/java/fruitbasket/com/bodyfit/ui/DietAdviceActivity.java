package fruitbasket.com.bodyfit.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fruitbasket.com.bodyfit.R;

public class DietAdviceActivity extends AppCompatActivity {

    private List<Food> foodList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_diet_advice);
        init();
        FoodAdapter adapter=new FoodAdapter(DietAdviceActivity.this,R.layout.list_item_diet_advice,foodList);
        ListView listView=(ListView)findViewById(R.id.diet_list_view);
        listView.setAdapter(adapter);
    }

    private void init(){
        Food vegetable=new Food("蔬菜","2.5 c-eq/day",R.drawable.diet_vegetable);
        Food cereal =new Food("谷物","6 oz-eq/day",R.drawable.diet_rice);
        Food protein =new Food("蛋白质","5.5 oz-eq/day",R.drawable.diet_protein);
        Food fruit =new Food("水果","2 c-eq/day",R.drawable.diet_fruit);
        Food dairyProduct =new Food("奶制品","3 c-eq/day",R.drawable.diet_milk);
        Food oil=new Food("油类","27 g/day",R.drawable.diet_oil);
        Food others=new Food("其他类","270 kCal/day",R.drawable.diet_honey);
        foodList.add(vegetable);
        foodList.add(cereal);
        foodList.add(protein);
        foodList.add(fruit);
        foodList.add(dairyProduct);
        foodList.add(oil);
        foodList.add(others);
    }
}

class Food{

    private String name;
    private String gram;
    private int imageId;

    public Food(String name,String gram,int imageId){
        this.name=name;
        this.gram = gram;
        this.imageId=imageId;
    }

    public String getName() {
        return name;
    }

    public String getGram() {
        return gram;
    }

    public int getImageId() {
        return imageId;
    }
}

class FoodAdapter extends ArrayAdapter<Food>{
    private int resouceId;

    public FoodAdapter(Context context, int textViewResourceId, List<Food> objects){
        super(context,textViewResourceId,objects);
        resouceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Food food =getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView ==null){
            view= LayoutInflater.from(getContext()).inflate(resouceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.foodImage=(ImageView)view.findViewById(R.id.diet_img);
            viewHolder.foodName=(TextView)view.findViewById(R.id.diet_name);
            viewHolder.foodGram=(TextView)view.findViewById(R.id.diet_gram);
            view.setTag(viewHolder);
        }
        else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.foodImage.setImageResource(food.getImageId());
        viewHolder.foodName.setText(food.getName());
        viewHolder.foodGram.setText(food.getGram());
        return view;
    }

    class ViewHolder{
        TextView foodName;
        TextView foodGram;
        ImageView foodImage;
    }
}