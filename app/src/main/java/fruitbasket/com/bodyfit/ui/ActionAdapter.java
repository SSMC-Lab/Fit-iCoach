package fruitbasket.com.bodyfit.ui;

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
 * Created by lkeye on 2018/1/24.
 */

public class ActionAdapter  extends ArrayAdapter<ActionListItem>{
    private int resourceId;
    public ActionAdapter(Context context, int textViewResourceId, List<ActionListItem> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ActionListItem action=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView actionImage=(ImageView)view.findViewById(R.id.action_image);
        TextView actionName=(TextView)view.findViewById(R.id.action_text1);
        TextView actionTimes=(TextView)view.findViewById(R.id.action_text2);
        actionImage.setImageResource(action.getImageId());
        actionName.setText(action.getName());
        actionTimes.setText(action.getTimes());
        return view;
    }
}
