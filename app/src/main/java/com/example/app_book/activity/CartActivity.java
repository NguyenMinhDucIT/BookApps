package com.example.app_book.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.app_book.R;
import com.example.app_book.adapter.CartAdapter;
import com.example.app_book.connect.CheckConnection;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {

    private ListView lvCart;
    private TextView txtCartEmptyNotification;
    private TextView txtSumMonney;
    public static TextView txtvTotalPrice;
    private Button btnThanhtoan,btnTTMuahang;
    private Toolbar toolbarCart;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mapping();
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            ActionToolBar();
            CheckData();
            EventUtil();
            CatchOnItemListView();
            EventButtonScreen();
        }else{
            CheckConnection.ShowToast_short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
            finish();
        }
    }

    // gán sự kiện nút tieps tục mua hàng và nút thanh toán
    private void EventButtonScreen() {
        btnTTMuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnThanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nếu có sp mới thanh toán có giá trị bên trong mảng phải lớn hơn 0
                if(MainActivity.cartList.size() > 0){
                    Intent intent = new Intent(getApplicationContext(), InfomationCustomerActivity.class);
                    startActivity(intent);
                }else{
                    CheckConnection.ShowToast_short(getApplicationContext(), "Giỏ hàng của bạn chưa có sản phẩm để thanh toán");
                }
            }
        });
    }

    private void CatchOnItemListView() {
        lvCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Xác nhận xóa sản phẩm");
                builder.setMessage("Bạn có chắc muốn xóa sản phẩm này ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(MainActivity.cartList.size() <= 0){
                            txtCartEmptyNotification.setVisibility(View.VISIBLE);
                        }else{
                            MainActivity.cartList.remove(position);
                            cartAdapter.notifyDataSetChanged();
                            EventUtil();
                            // trường hợp muốn xóa hết thì sẽ xóa hết phần tử bên trong sẽ hiện textview thông báo
                            if(MainActivity.cartList.size() <= 0){
                                txtCartEmptyNotification.setVisibility(View.VISIBLE);
                            }else{
                                txtCartEmptyNotification.setVisibility(View.INVISIBLE);
                                cartAdapter.notifyDataSetChanged();
                                EventUtil();
                            }
                        }
                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cartAdapter.notifyDataSetChanged();
                        EventUtil();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    public static void EventUtil() {
        long sumMonney = 0;
        for (int i = 0; i< MainActivity.cartList.size(); i++){
            sumMonney += MainActivity.cartList.get(i).getPriceProduct();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtvTotalPrice.setText(decimalFormat.format(sumMonney) + " Đ"); // chú ý
    }

    private void CheckData() {
        if(MainActivity.cartList.size() <= 0){
            cartAdapter.notifyDataSetChanged();
            txtCartEmptyNotification.setVisibility(View.VISIBLE);
            lvCart.setVisibility(View.INVISIBLE);
        }else{
            cartAdapter.notifyDataSetChanged();
            txtCartEmptyNotification.setVisibility(View.INVISIBLE);
            lvCart.setVisibility(View.VISIBLE);
        }
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbarCart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarCart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mapping() {
        lvCart = findViewById(R.id.lvCart);
        txtCartEmptyNotification = findViewById(R.id.txtCartEmptyNotification);
        txtSumMonney = findViewById(R.id.txtSumMonney);
        txtvTotalPrice = findViewById(R.id.txtvTotalPrice);
        btnThanhtoan = findViewById(R.id.btnThanhtoan);
        btnTTMuahang = findViewById(R.id.btnTTMuahang);
        toolbarCart = findViewById(R.id.toolbarCart);
        cartAdapter = new CartAdapter(CartActivity.this, MainActivity.cartList);
        lvCart.setAdapter(cartAdapter);
    }
}