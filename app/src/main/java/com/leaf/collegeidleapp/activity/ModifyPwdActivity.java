package com.leaf.collegeidleapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leaf.collegeidleapp.R;
import com.leaf.collegeidleapp.bean.User;
import com.leaf.collegeidleapp.util.UserDbHelper;

import java.util.LinkedList;

/**
 * 修改密码活动类
 */
public class ModifyPwdActivity extends AppCompatActivity {

    TextView tvStuNumber;
    EditText etOriginPwd,etNewPwd,etConfirmPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        //取消事件
        Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvStuNumber = findViewById(R.id.tv_user_number);
        tvStuNumber.setText(this.getIntent().getStringExtra("stu_number"));
        etOriginPwd = findViewById(R.id.et_original_pwd);
        etNewPwd = findViewById(R.id.et_new_pwd);
        etConfirmPwd = findViewById(R.id.et_confirm_new_pwd);
        Button btnModify = findViewById(R.id.btn_modify_pwd);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先保证输入合法
                if(CheckInput()) {
                    String stuNumber = tvStuNumber.getText().toString();
                    UserDbHelper dbHelper = new UserDbHelper(getApplicationContext(),UserDbHelper.DB_NAME,null,1);
                    LinkedList<User> users = dbHelper.readUsers();
                    for(User user : users) {
                        //首先找到用户名
                        if(user.getUsername().equals(stuNumber)) {
                            if(!etOriginPwd.getText().toString().equals(user.getPassword())) {
                                //提示初始密码输入错误
                                Toast.makeText(getApplicationContext(),"The initial password is incorrectly!",Toast.LENGTH_SHORT).show();
                            }else {
                                //执行修改密码操作
                                user.setPassword(etNewPwd.getText().toString());
                                boolean flag = dbHelper.updateUser(tvStuNumber.getText().toString(),etNewPwd.getText().toString());
                                if(flag) {
                                    Toast.makeText(getApplicationContext(),"Password has been updated!",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),"Failed to change password!",Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            }
                        }
                    }
                }
            }
        });
    }

    //判断输入的合法性
    public boolean CheckInput() {
        String OriginalPwd = etOriginPwd.getText().toString();
        String NewPwd = etNewPwd.getText().toString();
        String NewConfirmPwd = etConfirmPwd.getText().toString();
        if(OriginalPwd.trim().equals("")) {
            Toast.makeText(this,"Original password cannot be empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(NewPwd.trim().equals("")) {
            Toast.makeText(this,"New password cannot be empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(NewConfirmPwd.trim().equals("")) {
            Toast.makeText(this,"Confirm new password cannot be empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!NewPwd.trim().equals(NewConfirmPwd.trim())) {
            Toast.makeText(this,"Inconsistent password entered twice!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
