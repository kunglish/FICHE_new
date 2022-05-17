package com.leaf.collegeidleapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leaf.collegeidleapp.MainActivity;
import com.leaf.collegeidleapp.R;
import com.leaf.collegeidleapp.adapter.MyCollectionAdapter;
import com.leaf.collegeidleapp.bean.Collection;
import com.leaf.collegeidleapp.bean.Commodity;
import com.leaf.collegeidleapp.util.MyCollectionDbHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的收藏Activity类
 */
public class MyCollectionActivity extends AppCompatActivity {

    ListView lvMyCollection;
    List<Collection> myCollections = new ArrayList<>();
    TextView tvStuId;

    MyCollectionDbHelper dbHelper;
    //CommodityDbHelper commodityDbHelper;
    MyCollectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        //返回
        ImageButton ibBack = findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvStuId = findViewById(R.id.tv_stuId);
        tvStuId.setText(this.getIntent().getStringExtra("stuId"));
        lvMyCollection = findViewById(R.id.lv_my_collection);
        dbHelper = new MyCollectionDbHelper(getApplicationContext(),MyCollectionDbHelper.DB_NAME,null,1);
        myCollections = dbHelper.readMyCollections(tvStuId.getText().toString());
        adapter = new MyCollectionAdapter(getApplicationContext());
        adapter.setData(myCollections);
        lvMyCollection.setAdapter(adapter);
        //设置长按删除事件
        lvMyCollection.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyCollectionActivity.this);
                builder.setTitle("Tip:").setMessage("Are you sure you want to delete this collection?").setIcon(R.drawable.icon_user).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Collection collection = (Collection) adapter.getItem(position);
                        //删除收藏商品项
                        dbHelper.deleteMyCollection(collection.getTitle(),collection.getDescription(),collection.getPrice());
                        Toast.makeText(MyCollectionActivity.this,"Successfully deleted!",Toast.LENGTH_SHORT).show();
                    }
                }).show();
                return false;
            }
        });
        //页面刷新
        ImageButton ibRefresh = findViewById(R.id.ib_refresh);
        ibRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCollections = dbHelper.readMyCollections(tvStuId.getText().toString());
                adapter.setData(myCollections);
                lvMyCollection.setAdapter(adapter);
            }
        });

    }
}
