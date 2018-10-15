package fruitbasket.com.bodyfit.DietRecommendations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hedgehog.ratingbar.RatingBar;

import java.util.ArrayList;
import java.util.List;

import fruitbasket.com.bodyfit.R;

/**
 * Created by lkeye on 2018/9/2
 */
public class FoodScoreAdapter extends ArrayAdapter<FoodScore> {

    private String TAG="FoodScoreAdpter";
    private int resourceId;
    private List<Integer> list=new ArrayList<>();

    public FoodScoreAdapter(Context context, int textViewResourceId, List<FoodScore> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        for(int i=0;i<20;i++)
            list.add(0);
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final FoodScore foodScore = getItem(position);
        ViewHolder viewHolder=null;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.foodScoreImage = (ImageView) view.findViewById(R.id.food_score_img);
            viewHolder.foodScoreText = (TextView) view.findViewById(R.id.food_score_text);
            RatingBar mRatingBar = (RatingBar) view.findViewById(R.id.food_score_ratingbar);
            viewHolder.foodScoreRating = mRatingBar;
            final ViewHolder finalViewHolder =viewHolder;
            //通过监听，一旦状态发生改变，就需要在监听方法中使用getTag将对象取出，修改数据项
            viewHolder.foodScoreRating.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
                @Override
                public void onRatingChange(int RatingCount) {
                    FoodScore score=(FoodScore) finalViewHolder.foodScoreRating.getTag();
                    score.setScore(RatingCount);
                    list.set(position,RatingCount);
                    notifyDataSetChanged();
                }
            });
            view.setTag(viewHolder);
            viewHolder.foodScoreRating.setTag(foodScore);

        } else {
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.foodScoreRating.setTag(foodScore);
        }
        viewHolder.foodScoreImage.setImageResource(foodScore.getImageId());
        viewHolder.foodScoreText.setText(foodScore.getName());
        viewHolder.foodScoreRating.setStar(foodScore.getScore());
        list.set(position,foodScore.getScore());
        notifyDataSetChanged();
        return view;
    }

    public List<Integer> getList() {
        return list;
    }

    class ViewHolder {
        ImageView foodScoreImage;
        TextView foodScoreText;
        RatingBar foodScoreRating;
    }
}
