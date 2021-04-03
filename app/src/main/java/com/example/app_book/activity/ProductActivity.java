package com.example.app_book.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_book.R;
import com.example.app_book.adapter.ProductByCategoryIdAdapter;
import com.example.app_book.connect.CheckConnection;
import com.example.app_book.model.Category;
import com.example.app_book.model.Product;
import com.example.app_book.server.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class    ProductActivity extends AppCompatActivity {

    private ProductByCategoryIdAdapter productByCategoryIdAdapter;
    private List<Product> productList;
    private Toolbar toolbarProduct;
    private RecyclerView rcvProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        mapping();
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            Init();
            IntActionBar();
        }else{
            CheckConnection.ShowToast_short(ProductActivity.this, "Mời bạn kiểm tra lại kết nối");
        }
    }

    private void mapping() {
        toolbarProduct = findViewById(R.id.toolbarProduct);
        rcvProduct = findViewById(R.id.rcvProduct);
    }

    private void IntActionBar() {
        setSupportActionBar(toolbarProduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarProduct.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Init() {
        Intent intent = getIntent();
        Category cate = (Category) intent.getSerializableExtra("category");
        getAllProductByCategoryId(cate.getId());
    }

    private void getAllProductByCategoryId(int id) {
        productList = new ArrayList<>();
        productByCategoryIdAdapter = new ProductByCategoryIdAdapter(this, R.layout.line_product, productList);
        rcvProduct.setAdapter(productByCategoryIdAdapter);
        rcvProduct.setHasFixedSize(true);
        rcvProduct.setLayoutManager(new LinearLayoutManager(this));

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, StringUtil.URL_GET_PRODUCT_BY_CATEGORY_ID+"?categoryid="+id, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject obj;
                            for (int i = 0; i < response.length(); i++) {
                                obj = response.getJSONObject(i);
                                productList.add(new Product(obj.getInt("id"), obj.getString("name"),
                                        obj.getInt("price"), obj.getString("avatar"),
                                        obj.getString("description"), obj.getInt("categoryid")));
                            }
                            productByCategoryIdAdapter.notifyDataSetChanged();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(arrayRequest);
    }
}