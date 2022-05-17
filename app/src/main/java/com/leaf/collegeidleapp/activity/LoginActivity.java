package com.leaf.collegeidleapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leaf.collegeidleapp.MainActivity;
import com.leaf.collegeidleapp.R;
import com.leaf.collegeidleapp.bean.User;
import com.leaf.collegeidleapp.util.UserDbHelper;

import java.util.LinkedList;


public class LoginActivity extends AppCompatActivity {

    EditText EtStuNumber,EtStuPwd;
    TextView tvRegister;
    private String username;

    LinkedList<User> users = new LinkedList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvRegister = findViewById(R.id.tv_register);
        //跳转到注册界面
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        EtStuNumber = findViewById(R.id.et_username);
        EtStuPwd = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                if(CheckInput()) {
                    UserDbHelper dbHelper = new UserDbHelper(getApplicationContext(),UserDbHelper.DB_NAME,null,1);
                    users = dbHelper.readUsers();
                    for(User user : users) {
                        //如果可以找到,则输出登录成功,并跳转到主界面
                        if(user.getUsername().equals(EtStuNumber.getText().toString()) && user.getPassword().equals(EtStuPwd.getText().toString()) ) {
                            flag = true;
                            Toast.makeText(LoginActivity.this,"Login Successful!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            username = EtStuNumber.getText().toString();
                            bundle.putString("username",username);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                    //否则提示登录失败,需要重新输入
                    if (!flag) {
                        Toast.makeText(LoginActivity.this,"Username or password entered incorrectly!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //检查输入是否符合要求
    public boolean CheckInput() {
        String StuNumber = EtStuNumber.getText().toString();
        String StuPwd = EtStuPwd.getText().toString();
        if(StuNumber.trim().equals("")) {
            Toast.makeText(LoginActivity.this,"Username can not be empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StuPwd.trim().equals("")) {
            Toast.makeText(LoginActivity.this,"Password can not be blank!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
