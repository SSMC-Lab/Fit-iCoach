package fruitbasket.com.bodyfit.TimeLine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.vipulasri.timelineview.TimelineView;

import fruitbasket.com.bodyfit.R;

public class TimeLineActivity extends AppCompatActivity {

    private TimelineView mTimeLineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_time_line);
        mTimeLineView=(TimelineView)findViewById(R.id.time_marker);
    }


}
