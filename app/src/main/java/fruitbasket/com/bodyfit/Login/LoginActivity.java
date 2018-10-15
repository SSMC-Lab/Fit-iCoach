package fruitbasket.com.bodyfit.Login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import es.dmoral.toasty.Toasty;
import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.R;
import fruitbasket.com.bodyfit.ui.ExerciseFragment;
import fruitbasket.com.bodyfit.ui.MainActivity;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class LoginActivity extends AppCompatActivity {

    private EditText mUserName;  //输入用户名
    private EditText mUserPsw;   //输入密码
    private Button bt_submit;   //提交按钮
    private CheckBox mCheckBoxRemember;  //是否记住密码
    private TextView textView_logout;  //注销
    private CardView cv;
    private FloatingActionButton fab;

    private SharedPreferences login_sp;
    private String userNameValue,passwordValue;

    private UserDataManager mUserDataManager; // 用户数据管理类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        initView();
        setListener();
        checkIsRemember();
        if(mUserDataManager==null){
            mUserDataManager=new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
    }

    private void initView() {
        mUserName = (EditText) findViewById(R.id.login_username);
        mUserPsw= (EditText) findViewById(R.id.login_password);
        bt_submit = (Button) findViewById(R.id.login_bt_submit);
        mCheckBoxRemember=(CheckBox) findViewById(R.id.login_remember_psw);
//        textView_logout=(TextView)findViewById(R.id.textview_logout);
        cv = (CardView) findViewById(R.id.cv);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void setListener() {
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Explode explode = new Explode();
                explode.setDuration(500);
                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                /*ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                Intent i2 = new Intent(LoginActivity.this,ExerciseFragment.class);
                startActivity(i2, oc2.toBundle());*/
                login();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, fab, fab.getTransitionName());
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class), options.toBundle());
            }
        });

    }

    private void login(){
        if(isUserNameAndPwdValid()){
            String userName=mUserName.getText().toString().trim();
            String userPwd=mUserPsw.getText().toString().trim();
            SharedPreferences.Editor editor=login_sp.edit();
            int result=mUserDataManager.findUserByNameAndPwd("'"+userName+"'","'"+userPwd+"'");
            if(result==1){
                editor.putString("USER_NAME",userName);
                editor.putString("PASSWORD",userPwd);

                if(mCheckBoxRemember.isChecked()){
                    editor.putBoolean("mRememberCheck",true);
                } else{
                    editor.putBoolean("mRememberCheck",false);
                }
                editor.commit();
                Bundle bundle=new Bundle();
                bundle.putString(Conditions.USER_NAME,userName);
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("fragmentId",Conditions.PERSON_R_CODE);
                startActivity(intent);
                finish();
                Toasty.success(this,"登录成功！",Toast.LENGTH_SHORT,true).show();
            } else if(result==0){
                Toasty.error(this,"账号信息有误，请重新输入",Toast.LENGTH_SHORT,true).show();
            }
        }
    }

    private boolean isUserNameAndPwdValid(){
        String userName=mUserName.getText().toString().trim();
        String userPwd=mUserPsw.getText().toString().trim();

        if(userName.equals("")){
            Toasty.error(this,"用户名不可为空！", Toast.LENGTH_SHORT,true).show();
            return false;
        } else if(userPwd.equals("")){
            Toasty.error(this,"密码不可为空！",Toast.LENGTH_SHORT,true).show();
            return false;
        }
        return true;
    }

    private void checkIsRemember(){
        login_sp=getSharedPreferences("userInfo",0);
        String name=login_sp.getString("USER_NAME","");
        String pwd=login_sp.getString("PASSWORD","");
        boolean isRemember=login_sp.getBoolean("mRememberCheck",false);
        boolean choseAutoLogin=login_sp.getBoolean("mAutoLoginCheck",false);

        //若上次勾选了记住密码
        if(isRemember){
            mUserName.setText(name);
            mUserPsw.setText(pwd);
            mCheckBoxRemember.setChecked(true);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        fab.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
    }
}
