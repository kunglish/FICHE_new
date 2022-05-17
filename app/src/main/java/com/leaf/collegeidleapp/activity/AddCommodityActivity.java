package com.leaf.collegeidleapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.leaf.collegeidleapp.R;
import com.leaf.collegeidleapp.bean.Commodity;
import com.leaf.collegeidleapp.util.CommodityDbHelper;

import java.io.ByteArrayOutputStream;

/**
 * 物品发布界面Activity类
 */
public class AddCommodityActivity extends AppCompatActivity {

    TextView tvStuId;
    ImageButton ivPhoto;
    EditText etTitle,etPrice,etPhone,etDescription;
    Spinner spType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_commodity);

        tvStuId = findViewById(R.id.tv_user_id);
        tvStuId.setText(this.getIntent().getStringExtra("user_id"));
        ImageButton ibBack = findViewById(R.id.ib_back);
        //返回按钮点击事件
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivPhoto = findViewById(R.id.iv_photo);
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,1);
            }
        });
        etTitle = findViewById(R.id.et_title);
        etPrice = findViewById(R.id.et_price);
        etPhone = findViewById(R.id.et_phone);
        etDescription = findViewById(R.id.et_description);
        spType = findViewById(R.id.spn_type);
        Button btnPublish = findViewById(R.id.btn_publish);
        //发布按钮点击事件
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先检查合法性
                if(CheckInput()) {
                    CommodityDbHelper dbHelper = new CommodityDbHelper(getApplicationContext(), CommodityDbHelper.DB_NAME, null, 1);
                    Commodity commodity = new Commodity();
                    //把图片先转化成bitmap格式
                    BitmapDrawable drawable = (BitmapDrawable) ivPhoto.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    //二进制数组输出流
                    ByteArrayOutputStream byStream = new ByteArrayOutputStream();
                    //将图片压缩成质量为100的PNG格式图片
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byStream);
                    //把输出流转换为二进制数组
                    byte[] byteArray = byStream.toByteArray();
                    commodity.setPicture(byteArray);
                    commodity.setTitle(etTitle.getText().toString());
                    commodity.setCategory(spType.getSelectedItem().toString());
                    commodity.setPrice(Float.parseFloat(etPrice.getText().toString()));
                    commodity.setPhone(etPhone.getText().toString());
                    commodity.setDescription(etDescription.getText().toString());
                    commodity.setStuId(tvStuId.getText().toString());
                    if (dbHelper.AddCommodity(commodity)) {
                        Toast.makeText(getApplicationContext(), "Released Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Released Fail!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            //从相册返回的数据
            if (data != null) {
                //得到图片的全路径
                Uri uri = data.getData();
                ivPhoto.setImageURI(uri);
            }
        }
    }

    /**
     * 检查输入是否合法
     */
    public boolean CheckInput() {
        String title = etTitle.getText().toString();
        String price = etPrice.getText().toString();
        String type = spType.getSelectedItem().toString();
        String phone = etPhone.getText().toString();
        String description = etDescription.getText().toString();
        if (title.trim().equals("")) {
            Toast.makeText(this,"Card name cannot be empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (price.trim().equals("")) {
            Toast.makeText(this,"Card price cannot be empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (type.trim().equals("Select Category")) {
            Toast.makeText(this,"Card category not selected!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.trim().equals("")) {
            Toast.makeText(this,"Phone number can not be empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.trim().equals("")) {
            Toast.makeText(this,"Card description cannot be empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
