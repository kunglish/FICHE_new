package com.leaf.collegeidleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leaf.collegeidleapp.activity.AddCommodityActivity;
import com.leaf.collegeidleapp.activity.CommodityTypeActivity;
import com.leaf.collegeidleapp.activity.PersonalCenterActivity;
import com.leaf.collegeidleapp.activity.ReviewCommodityActivity;
import com.leaf.collegeidleapp.adapter.AllCommodityAdapter;
import com.leaf.collegeidleapp.bean.Commodity;
import com.leaf.collegeidleapp.util.CommodityDbHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ListView lvAllCommodity;
    List<Commodity> allCommodities = new ArrayList<>();
    ImageButton ibSoccer,ibBasketball,ibFootball,ibMore;

    CommodityDbHelper dbHelper;
    AllCommodityAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvAllCommodity = findViewById(R.id.lv_all_commodity);
        dbHelper = new CommodityDbHelper(getApplicationContext(), CommodityDbHelper.DB_NAME, null, 1);
        adapter = new AllCommodityAdapter(getApplicationContext());
        allCommodities = dbHelper.readAllCommodities();
        adapter.setData(allCommodities);
        lvAllCommodity.setAdapter(adapter);
        final Bundle bundle = this.getIntent().getExtras();
        final TextView tvStuNumber = findViewById(R.id.tv_student_number);
        String str = "";
        if (bundle != null) {
            str = "User: " + bundle.getString("username");
        }
        tvStuNumber.setText(str);
        //当前登录的账号
        final String stuNum = tvStuNumber.getText().toString().substring(6,tvStuNumber.getText().length());
        ImageButton IbAddProduct = findViewById(R.id.ib_add_product);
        //跳转到添加物品界面
        IbAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCommodityActivity.class);
                if (bundle != null) {

                    bundle.putString("user_id", stuNum);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });
        ImageButton IbPersonalCenter = findViewById(R.id.ib_personal_center);
        //跳转到个人中心界面
        IbPersonalCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonalCenterActivity.class);
                if (bundle != null) {
                    bundle.putString("username1", stuNum);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });
        //刷新界面
        ImageButton ibRefresh = findViewById(R.id.ib_refresh);
        ibRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allCommodities = dbHelper.readAllCommodities();
                adapter.setData(allCommodities);
                lvAllCommodity.setAdapter(adapter);
            }
        });
        //为每一个item设置点击事件
        lvAllCommodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Commodity commodity = (Commodity) lvAllCommodity.getAdapter().getItem(position);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("position",position);
                bundle1.putByteArray("picture",commodity.getPicture());
                bundle1.putString("title",commodity.getTitle());
                bundle1.putString("description",commodity.getDescription());
                bundle1.putFloat("price",commodity.getPrice());
                bundle1.putString("phone",commodity.getPhone());
                bundle1.putString("stuId",stuNum);
                Intent intent = new Intent(MainActivity.this, ReviewCommodityActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        //点击不同的类别,显示不同的商品信息
        ibSoccer = findViewById(R.id.ib_soccer);
        ibBasketball = findViewById(R.id.ib_basketball);
        ibFootball = findViewById(R.id.ib_football);
        ibMore = findViewById(R.id.ib_more);
        final Bundle bundle2 = new Bundle();
        //soccer
        ibSoccer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status",1);
                Intent intent = new Intent(MainActivity.this, CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);


                Toast.makeText(MainActivity.this,"Soccer!",Toast.LENGTH_SHORT).show();
            }
        });
        //basketball
        ibBasketball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status",2);
                Intent intent = new Intent(MainActivity.this,CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);


                Toast.makeText(MainActivity.this,"Basketball!",Toast.LENGTH_SHORT).show();
            }
        });
        //football
        ibFootball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status",3);
                Intent intent = new Intent(MainActivity.this,CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);


                Toast.makeText(MainActivity.this,"Football!",Toast.LENGTH_SHORT).show();
            }
        });
        //more
        ibMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status",4);
                Intent intent = new Intent(MainActivity.this,CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);


                Toast.makeText(MainActivity.this,"Other!",Toast.LENGTH_SHORT).show();
            }
        });
    }

}