package fruitbasket.com.bodyfit.ui;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TabWidget;

import org.litepal.LitePal;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.ExerciseSociety.ExerciseSocietyFragment;
import fruitbasket.com.bodyfit.R;
import fruitbasket.com.bodyfit.helper.PermissionHelper;
import fruitbasket.com.bodyfit.helper.PermissionInterface;

public class MainActivity extends BaseTabActivity  implements PermissionInterface{

    private String TAG = "MainActivity";

    private TabWidget mTabWidget;
    private ViewPager mViewPager;
    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        permissionHelper=new PermissionHelper(this,this);
        permissionHelper.requestPermissions();
        startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "activityResult=" + requestCode + " " + resultCode);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                BluetoothAdapter.getDefaultAdapter().enable();
                initViews();
            } else
                finish();
            //else 关闭整个程序
        }
        int value = getIntent().getIntExtra("id", 0);
//        Log.i(TAG, "onResume " + value);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected TabWidget getTabWidget() {
        return mTabWidget;
    }

    @Override
    protected ViewPager getViewPager() {
        return mViewPager;
    }

    protected void initViews() {
        mTabWidget = (TabWidget) findViewById(R.id.tabWidget_bodyfit);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_bodyfit);
        mTabWidget.setStripEnabled(false);

        Fragment exerciseSociety = new ExerciseSocietyFragment();
        Fragment exerciseFragment = new ExerciseFragment();
//        Fragment profileFragment=new ProfileFragment();
        Fragment personalFagment = new PersonalFragment();

        LayoutInflater inflater = getLayoutInflater();
        Resources resources = getResources();

        //使用setImageResource就不会变形，用setBackgroundResource就会
        ImageView targetTab = (ImageView) inflater.inflate(R.layout.layout_tab, mTabWidget, false);
        targetTab.setImageResource(R.drawable.target_seletor);

        ImageView exerciseTab = (ImageView) inflater.inflate(R.layout.layout_tab, mTabWidget, false);
        exerciseTab.setImageResource(R.drawable.sport_seletor);

//        ImageView profileTab=(ImageView)inflater.inflate(R.layout.layout_tab,mTabWidget,false);
//        profileTab.setImageResource(R.drawable.profile_seletor);
        ImageView personalTab = (ImageView) inflater.inflate(R.layout.layout_tab, mTabWidget, false);
        personalTab.setImageResource(R.drawable.profile_seletor);

        addTab(targetTab, exerciseSociety, "TARGET");
        addTab(exerciseTab, exerciseFragment, "EXERCISE");
//        addTab(profileTab, profileFragment, "PROFILE");
        addTab(personalTab, personalFagment, "PERSONAL");

        setCurrentTab(1);

        LitePal.getDatabase();
        //测试页面
        /*Fragment BluetoothTestFragment=new BluetoothTestFragment();
        ImageView testTab=(ImageView)inflater.inflate(R.layout.layout_tab,mTabWidget,false);
        addTab(testTab, BluetoothTestFragment, "TEST");*/

//        setCurrentTab(0);

        //测试DataSetBuffer
        /*Fragment test=new TestDataSetFragment();
        ImageView testTab=(ImageView)inflater.inflate(R.layout.layout_tab,mTabWidget,false);
        addTab(testTab,test,"test");
        setCurrentTab(3);*/
    }

    @Override
    public void onResume() {
        Bundle bundle = getIntent().getExtras();
        Log.i(TAG, "onResume: Bundle=="+bundle);
        int fragmentId = getIntent().getIntExtra("fragmentId", 0);
        if (fragmentId == Conditions.SOCIETY_R_CODE) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ExerciseSocietyFragment fragment = new ExerciseSocietyFragment();
            fragment.setArguments(bundle);
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
            finish();
        }
        else if(fragmentId==Conditions.PERSON_R_CODE){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PersonalFragment fragment=new PersonalFragment();
            Log.i(TAG, "onResume: Bundle= 进入person");
            fragment.setArguments(bundle);
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
            finish();
        }
        super.onResume();
    }

    /*@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = getIntent().getExtras();
        Log.i(TAG, "onNewIntent: Bundle=="+bundle);
        int fragmentId = getIntent().getIntExtra("fragmentId", 0);
        if (fragmentId == Conditions.SOCIETY_R_CODE) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ExerciseSocietyFragment fragment = new ExerciseSocietyFragment();
            fragment.setArguments(bundle);
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
            finish();
        }
        else if(fragmentId==Conditions.PERSON_R_CODE){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PersonalFragment fragment=new PersonalFragment();
            fragment.setArguments(bundle);
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
            finish();
        }
    }
*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(permissionHelper.requestPermissionsResult(requestCode,permissions,grantResults))
            return;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public int getPermissionsRequestCode() {
        return 10000;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.CAMERA
        };
    }

    @Override
    public void requestPermissionsSuccess() {

    }

    @Override
    public void requestPermissionsFail() {
        finish();
    }
}
