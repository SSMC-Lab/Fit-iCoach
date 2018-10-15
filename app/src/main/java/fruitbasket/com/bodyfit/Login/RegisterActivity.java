package fruitbasket.com.bodyfit.Login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import fruitbasket.com.bodyfit.R;

public class RegisterActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private CardView cvAdd;
    private EditText mUserName;  //输入用户名
    private EditText mUserPwd;    // 输入密码
    private EditText mRepeatPwd;  //输入重复密码
    private Button mSubmitButton;  //提交
    private UserDataManager userDataManager;  //用户数据管理类
    private static final String TAG="RegisterActivity";

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        ShowEnterAnimation();
        initView();

        //建立本地数据库
        if(userDataManager==null){
            userDataManager=new UserDataManager(this);
            userDataManager.openDataBase();
        }

    }

    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        cvAdd = (CardView) findViewById(R.id.cv_add);
        mUserName=(EditText) findViewById(R.id.register_username);
        mUserPwd=(EditText) findViewById(R.id.register_password);
        mRepeatPwd=(EditText) findViewById(R.id.register_repeatpassword);
        mSubmitButton=(Button) findViewById(R.id.register_bt_finish);
        fab.setOnClickListener(register_listener);
        mSubmitButton.setOnClickListener(register_listener);
    }

    View.OnClickListener register_listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.register_bt_finish:
                    register_check();
                    break;
                case R.id.fab:
                    animateRevealClose();
                    break;
            }
        }
    };

    private void register_check(){
        if(isUserNameAndPwdValid()){
            String userName=mUserName.getText().toString().trim();
            String userPwd=mUserPwd.getText().toString().trim();

            //检查用户是否存在
            int count =userDataManager.findUserByName("'"+userName+"'");
            Log.i(TAG, "register_check: count="+count+" name="+userName);
            if(count>0){
                Toasty.error(this,"该用户名已存在！",Toast.LENGTH_SHORT,true).show();
                return;
            }
            UserData userData=new UserData("'"+userName+"'","'"+userPwd+"'");
            userDataManager.openDataBase();
            long flag =userDataManager.insertUserData(userData);
            if(flag==-1){
                Toasty.error(this,"注册失败！",Toast.LENGTH_SHORT,true).show();
            } else{
                Toasty.success(this,"注册成功！",Toast.LENGTH_SHORT,true).show();
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                animateRevealClose();
                finish();
            }
        }
    }

    private boolean isUserNameAndPwdValid(){
        String userName=mUserName.getText().toString().trim();
        String userPwd=mUserPwd.getText().toString().trim();
        String repeatPwd=mRepeatPwd.getText().toString().trim();

        if(userName.equals("")){
            Toasty.error(this,"用户名不可为空！", Toast.LENGTH_SHORT,true).show();
            return false;
        } else if(userPwd.equals("")){
            Toasty.error(this,"密码不可为空！",Toast.LENGTH_SHORT,true).show();
            return false;
        } else if(userPwd.length()<6 || userPwd.length()>15){
            Toasty.error(this,"密码长度需为6~15位!",Toast.LENGTH_SHORT,true).show();
            return false;
        } else if(TextUtils.isDigitsOnly(userPwd)){
            Toasty.error(this,"密码不可全为数字！",Toast.LENGTH_SHORT,true).show();
            return false;
        } else if(repeatPwd.equals("")){
            Toasty.error(this,"重复输入的密码不可为空！",Toast.LENGTH_SHORT,true).show();
            return false;
        } else if(userPwd.equals(repeatPwd)==false){
            Toasty.error(this,"两次密码输入不一致！",Toast.LENGTH_SHORT,true).show();
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }
}
