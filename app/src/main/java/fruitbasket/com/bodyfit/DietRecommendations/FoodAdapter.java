package fruitbasket.com.bodyfit.DietRecommendations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fruitbasket.com.bodyfit.R;

/**
 * Created by lkeye on 2018/9/8
 */
public class FoodAdapter extends ArrayAdapter<Food> {
    private int resouceId;

    public FoodAdapter(Context context, int textViewResourceId, List<Food> objects) {
        super(context, textViewResourceId, objects);
        resouceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Food food = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resouceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.foodImage = (ImageView) view.findViewById(R.id.diet_img);
            viewHolder.foodName = (TextView) view.findViewById(R.id.diet_name);
            viewHolder.foodGram = (TextView) view.findViewById(R.id.diet_gram);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.foodImage.setImageResource(food.getImageId());
        viewHolder.foodName.setText(food.getName());
        viewHolder.foodGram.setText(food.getGram());
        return view;
    }

    class ViewHolder {
        TextView foodName;
        TextView foodGram;
        ImageView foodImage;
    }
}
