package com.leaf.collegeidleapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.leaf.collegeidleapp.R;

/**
 * 个人中心主界面Activity类
 */
public class PersonalCenterActivity extends AppCompatActivity {

    TextView TvStuNumber;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        //取出登录时的登录名
        TvStuNumber = findViewById(R.id.tv_student_number);
        String StuNumber = this.getIntent().getStringExtra("username1");
        TvStuNumber.setText(StuNumber);
        //返回主界面
        ImageButton ibBack = findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击修改密码按钮
        final Button btnModifyPwd = findViewById(R.id.btn_modify_password);
        btnModifyPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),ModifyPwdActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("stu_number",TvStuNumber.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);


                Toast.makeText(PersonalCenterActivity.this,"Change Password!",Toast.LENGTH_SHORT).show();
            }
        });
        //点击查看我的发布按钮
        Button btnMyGoods = findViewById(R.id.btn_my_goods);
        btnMyGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyCommodityActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("stu_id",TvStuNumber.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //点击查看我的收藏按钮
        Button btnMyCollection = findViewById(R.id.btn_my_collection);
        btnMyCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyCollectionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("stuId",TvStuNumber.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //退出登录按钮点击事件
        Button btnLogOut = findViewById(R.id.btn_logout);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalCenterActivity.this);
                builder.setTitle("Tip:").setMessage("Are you sure to log out?").setIcon(R.drawable.icon_user).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //跳转到登录界面
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
    }
}
