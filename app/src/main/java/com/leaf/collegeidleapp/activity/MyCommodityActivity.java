package com.leaf.collegeidleapp.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leaf.collegeidleapp.R;
import com.leaf.collegeidleapp.adapter.MyCommodityAdapter;
import com.leaf.collegeidleapp.bean.Commodity;
import com.leaf.collegeidleapp.util.CommodityDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的发布物品Activity类
 */
public class MyCommodityActivity extends AppCompatActivity {

    ListView lvMyCommodity;
    List<Commodity> myCommodities = new ArrayList<>();
    TextView tvStuId;

    CommodityDbHelper dbHelper;

    MyCommodityAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_commodity);
        ImageButton ibBack = findViewById(R.id.ib_back);
        //点击返回销毁当前界面
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvStuId = findViewById(R.id.tv_stu_id);
        tvStuId.setText(this.getIntent().getStringExtra("stu_id"));
        lvMyCommodity = findViewById(R.id.lv_my_commodity);
        adapter = new MyCommodityAdapter(getApplicationContext());
        dbHelper = new CommodityDbHelper(getApplicationContext(),CommodityDbHelper.DB_NAME,null,1);
        myCommodities = dbHelper.readMyCommodities(tvStuId.getText().toString());
        adapter.setData(myCommodities);
        lvMyCommodity.setAdapter(adapter);
        //长按点击事件
        lvMyCommodity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyCommodityActivity.this);
                builder.setTitle("Tip:").setMessage("Are you sure you want to delete this card?").setIcon(R.drawable.icon_user).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //根据商品名称,商品描述和价格执行删除操作
                        Commodity commodity = (Commodity) adapter.getItem(position);
                        dbHelper.deleteMyCommodity(commodity.getTitle(),commodity.getDescription(),commodity.getPrice());
                        //数据一样,可以直接用,关联删除
                        //dbHelper2.deleteMyCollection(commodity.getTitle(),commodity.getDescription(),commodity.getPrice());
                        Toast.makeText(MyCommodityActivity.this,"Successfully deleted!",Toast.LENGTH_SHORT).show();
                    }
                }).show();
                return false;
            }
        });
        //刷新界面点击事件
        ImageButton ibRefresh = findViewById(R.id.ib_refresh);
        ibRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new CommodityDbHelper(MyCommodityActivity.this,CommodityDbHelper.DB_NAME,null,1);
                adapter = new MyCommodityAdapter(MyCommodityActivity.this);
                myCommodities = dbHelper.readMyCommodities(tvStuId.getText().toString());
                adapter.setData(myCommodities);
                lvMyCommodity.setAdapter(adapter);
            }
        });
    }
}
