package fruitbasket.com.bodyfit.ui;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import fruitbasket.com.bodyfit.R;

public class LoginActivity extends AppCompatActivity {
    private EditText userName;
    private EditText userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        userName=(EditText)findViewById(R.id.editText_userName);
        Drawable drawable=getResources().getDrawable(R.drawable.login_user);
        drawable.setBounds(2,2,50,50);
        userName.setCompoundDrawables(drawable,null,null,null);
        userPassword=(EditText)findViewById(R.id.edit_userPassword);
        drawable=getResources().getDrawable(R.drawable.password);
        drawable.setBounds(0,0,48,48);
        userPassword.setCompoundDrawables(drawable,null,null,null);
    }
}
