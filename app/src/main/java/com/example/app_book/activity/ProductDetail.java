package com.example.app_book.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.app_book.R;
import com.example.app_book.connect.CheckConnection;
import com.example.app_book.model.Cart;
import com.example.app_book.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ProductDetail extends AppCompatActivity {

    private ImageView imgProduct;
    private TextView txtvName, txtvPrice,txtvQuantity, txtvDescription;
    private Button btnMinus, btnPlus,btnAddCart;

    private Product product;
    private Toolbar toolbarProductDetail;

    int id = 0;
    String name = "";
    int price = 0;
    String avatar = "";
    String description = "";
    int categoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        mapping();
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            ActionToolBar();
            getAllProductByCategoryIdDetail();
        }else {
            CheckConnection.ShowToast_short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
            finish();
        }
    }

//   tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    // gán sự kiện chuyển màn hình cho nút cart
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnuCart:
                Intent i = new Intent(ProductDetail.this, CartActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickProductDetails(View v){
        switch (v.getId()){
            case R.id.btnMinus:
                minusQuantity();
                break;
            case R.id.btnPlus:
                plusQuantity();
                break;
            case R.id.btnAddCart:
                addToCart();
                break;
        }
    }

    private void addToCart() {
        if(MainActivity.cartList.size() > 0){
            int qtt1 = Integer.parseInt(txtvQuantity.getText().toString());
            boolean existe = false;
            for (int i = 0; i < MainActivity.cartList.size(); i++){
                if(MainActivity.cartList.get(i).getIdProduct() == id){
                    MainActivity.cartList.get(i).setAmountProduct(MainActivity.cartList.get(i).getAmountProduct() + qtt1);
                    if(MainActivity.cartList.get(i).getAmountProduct() >=20){
                        MainActivity.cartList.get(i).setAmountProduct(20);
                    }
                    MainActivity.cartList.get(i).setPriceProduct(price * MainActivity.cartList.get(i).getAmountProduct());
                    existe = true;
                }
            }
            if(existe == false){
                int qtt = Integer.parseInt(txtvQuantity.getText().toString());
                long priceNew = qtt * price;
                MainActivity.cartList.add(new Cart(id, name, priceNew, avatar, qtt));
            }
        }else{
            int qtt = Integer.parseInt(txtvQuantity.getText().toString());
            long priceNew = qtt * price;
            MainActivity.cartList.add(new Cart(id, name, priceNew, avatar, qtt));
        }
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        startActivity(intent);
    }

    private void minusQuantity() {
        int qtt = Integer.parseInt(txtvQuantity.getText().toString());
        if (qtt > 1){
            qtt--;
        }
        txtvQuantity.setText(qtt+"");
    }

    private void plusQuantity() {
        int qtt = Integer.parseInt(txtvQuantity.getText().toString());
        qtt++;
        txtvQuantity.setText(qtt+"");
    }

    private void getAllProductByCategoryIdDetail() {
        product = (Product) getIntent().getSerializableExtra("product");
        id = product.getId();
        name = product.getName();
        price = product.getPrice();
        avatar = product.getAvatar();
        description = product.getDescription();
        categoryId = product.getCategoryId();
        txtvName.setText(name);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtvPrice.setText("Giá: " + decimalFormat.format(price) + " Đ");
        txtvDescription.setText(description); // set nội dung
        Picasso.get().load(product.getAvatar()).into(imgProduct);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbarProductDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarProductDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mapping() {
        imgProduct = findViewById(R.id.imgProduct);
        txtvName = findViewById(R.id.txtvName);
        txtvPrice = findViewById(R.id.txtvPrice);
        txtvDescription = findViewById(R.id.txtvDescription);
        btnAddCart = findViewById(R.id.btnAddCart);
        toolbarProductDetail = findViewById(R.id.toolbarProductDetail);
        txtvQuantity = findViewById(R.id.txtvQuantity);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
    }
}