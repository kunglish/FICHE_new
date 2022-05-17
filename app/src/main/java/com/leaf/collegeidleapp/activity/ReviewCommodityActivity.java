package com.leaf.collegeidleapp.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leaf.collegeidleapp.R;
import com.leaf.collegeidleapp.adapter.ReviewAdapter;
import com.leaf.collegeidleapp.bean.Collection;
import com.leaf.collegeidleapp.bean.Commodity;
import com.leaf.collegeidleapp.bean.Review;
import com.leaf.collegeidleapp.util.MyCollectionDbHelper;
import com.leaf.collegeidleapp.util.ReviewDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * 商品信息评论/留言类
 */
public class ReviewCommodityActivity extends AppCompatActivity {

    TextView title,description,price,phone;
    ImageView ivCommodity;
    ListView lvReview;
    LinkedList<Review> reviews = new LinkedList<>();
    EditText etComment;
    int position;
    byte[] picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_commodity);
        ivCommodity = findViewById(R.id.iv_commodity);
        title = findViewById(R.id.tv_title);
        description = findViewById(R.id.tv_description);
        price = findViewById(R.id.tv_price);
        phone = findViewById(R.id.tv_phone);
        Bundle b = getIntent().getExtras();
        if( b != null) {
            picture = b.getByteArray("picture");
            Bitmap img = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            ivCommodity.setImageBitmap(img);
            title.setText(b.getString("title"));
            description.setText(b.getString("description"));
            price.setText(String.valueOf(b.getFloat("price"))+"RMB");
            phone.setText(b.getString("phone"));
            position = b.getInt("position");
        }
        //返回
        ImageButton ibBack = findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击收藏按钮
        Button ibMyLove = findViewById(R.id.btn_my_love);
        ibMyLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCollectionDbHelper dbHelper = new MyCollectionDbHelper(getApplicationContext(),MyCollectionDbHelper.DB_NAME,null,1);
                Collection collection = new Collection();
                collection.setTitle(title.getText().toString());
                String price1 = price.getText().toString().substring(0,price.getText().toString().length()-3);
                collection.setPrice(Float.parseFloat(price1));
                collection.setPhone(phone.getText().toString());
                collection.setDescription(description.getText().toString());
                collection.setPicture(picture);
                String stuId = getIntent().getStringExtra("stuId");
                collection.setStuId(stuId);
                dbHelper.addMyCollection(collection);
                Toast.makeText(getApplicationContext(),"Added to my collection!",Toast.LENGTH_SHORT).show();
            }
        });

        //BUY
        Button ibBuy = findViewById(R.id.btn_buy);
        ibBuy.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(),"Purchased Card successfully!",Toast.LENGTH_SHORT).show();
            }
        });

        etComment = findViewById(R.id.et_comment);
        lvReview = findViewById(R.id.list_comment);
        //提交评论点击事件
        Button btnReview = findViewById(R.id.btn_submit);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先检查是否为空
                if(CheckInput()) {
                    ReviewDbHelper dbHelper = new ReviewDbHelper(getApplicationContext(),ReviewDbHelper.DB_NAME,null,1);
                    Review review = new Review();
                    review.setContent(etComment.getText().toString());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
                    //获取当前时间
                    Date date = new Date(System.currentTimeMillis());
                    review.setCurrentTime(simpleDateFormat.format(date));
                    String stuId = getIntent().getStringExtra("stuId");
                    review.setStuId(stuId);
                    review.setPosition(position);
                    dbHelper.addReview(review);
                    //评论置为空
                    etComment.setText("");


                    Toast.makeText(getApplicationContext(),"Comment successful!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        final ReviewAdapter adapter = new ReviewAdapter(getApplicationContext());
        final ReviewDbHelper dbHelper = new ReviewDbHelper(getApplicationContext(),ReviewDbHelper.DB_NAME,null,1);
        reviews = dbHelper.readReviews(position);
        adapter.setData(reviews);
        //设置适配器
        lvReview.setAdapter(adapter);
        //刷新页面
        ImageButton ibRefresh = findViewById(R.id.ib_refresh);
        ibRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviews = dbHelper.readReviews(position);
                adapter.setData(reviews);
                lvReview.setAdapter(adapter);
            }
        });
    }

    /**
     * 检查输入评论是否为空
     * @return true
     */
    public boolean CheckInput() {
        String comment = etComment.getText().toString();
        if (comment.trim().equals("")) {
            Toast.makeText(this,"Comment can not be empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
