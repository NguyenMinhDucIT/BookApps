package com.example.app_book.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_book.R;
import com.example.app_book.adapter.CategoryAdapter;
import com.example.app_book.adapter.ProductAdapter;
import com.example.app_book.connect.CheckConnection;
import com.example.app_book.model.Cart;
import com.example.app_book.model.Category;
import com.example.app_book.model.Product;
import com.example.app_book.server.StringUtil;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ViewFlipper viewFlipper;
    private RecyclerView rvLastestProduct;
    private NavigationView navigationView;
    private ListView lvCategory;

    List<String> sliderList;
    private SearchView searchView;

    // mảng và adapter của category
    CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    ProductAdapter productAdapter;
    List<Product> productList;

    // mảng cart
    public static List<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();

        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            InitActionBar();
            InitLeftMenu();
            initViewFlipper();
            getLastestProduct();
            CatchOnItemListView();
        }else{
            CheckConnection.ShowToast_short(MainActivity.this, "Mời bạn kiểm tra lại kết nối");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(MainActivity.this, CartActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    // Đổi thứ tự row database
    private void CatchOnItemListView() {
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (categoryList.get(position).getId()){
                    case 1:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            intent.putExtra("category", categoryList.get(position));
                            startActivity(intent);
                            break;
                        }else{
                            CheckConnection.ShowToast_short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                        }
                    case 4:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                            intent.putExtra("category", categoryList.get(position));
                            startActivity(intent);
                            break;
                        }else{
                            CheckConnection.ShowToast_short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                        }
                    case 5:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                            intent.putExtra("category", categoryList.get(position));
                            startActivity(intent);
                            break;
                        }else{
                            CheckConnection.ShowToast_short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                        }
                    default:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                            intent.putExtra("category", categoryList.get(position));
                            startActivity(intent);
                        }
                }
            }
        });
    }

    private void InitLeftMenu() {
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryList, R.layout.line_category);
        lvCategory.setAdapter(categoryAdapter);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, StringUtil.URL_GET_CATEGORY, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        categoryList.add(new Category(jsonObject.getInt("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("avatar")));
                    }
                    categoryAdapter.notifyDataSetChanged();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.ShowToast_short(getApplicationContext(), error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(arrayRequest);
    }

    private void getLastestProduct() {
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getApplicationContext(), R.layout.line_lastest_product, productList);
        rvLastestProduct.setAdapter(productAdapter);
        rvLastestProduct.setHasFixedSize(true);
        rvLastestProduct.setLayoutManager(new GridLayoutManager(this, 2));
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, StringUtil.URL_GET_LASTEST_PRODUCT, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject obj;
                            for (int i = 0; i < response.length(); i++) {
                                obj = response.getJSONObject(i);
                                productList.add(new Product(obj.getInt("id"),
                                        obj.getString("name"), obj.getInt("price"),
                                        obj.getString("avatar"),obj.getString("description"),
                                        obj.getInt("categoryid")));
                            }
                            productAdapter.notifyDataSetChanged();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CheckConnection.ShowToast_short(getApplicationContext(), error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(arrayRequest);
    }

    // sửa slide ở đây
    private void initViewFlipper() {
        sliderList = new ArrayList<>();
        sliderList.add("https://i0.wp.com/pointwhiskeypublishing.com/wp-content/uploads/2017/12/point-whiskey-books-banner.jpg?ssl=1");
        sliderList.add("https://previews.123rf.com/images/leoedition/leoedition1708/leoedition170800386/84002428-read-the-books-banner-design.jpg");
        sliderList.add("https://laurasbooksandblogs.com/wp-content/uploads/2019/12/Top-10-Books-2019-Banner.png");
        sliderList.add("https://www.slashfilm.com/wp/wp-content/images/scarystories-banner.jpg");
        sliderList.add("https://images.template.net/wp-content/uploads/2016/10/19142344/Sci-fic-book-cover-design.jpg");
        ImageView imageView;
        for (int i = 0; i < sliderList.size(); i++) {
            // gán imageView với màn hình
            imageView = new ImageView(this);
            Picasso.get().load(sliderList.get(i)).into(imageView);
            // thuộc tính vừa khít với màn hình
            imageView.setScaleType(imageView.getScaleType().FIT_XY);
            viewFlipper.addView(imageView);;
            viewFlipper.setFlipInterval(3000); // set thời gian chạy
            viewFlipper.setAutoStart(true); // để chạy tự động
            Animation anim_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_bar_inright);
            Animation anim_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_bar_outright);
            viewFlipper.setInAnimation(anim_in);
            viewFlipper.setOutAnimation(anim_out);
        }
    }

    private void InitActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void mapping() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        viewFlipper = findViewById(R.id.viewFlipper);
        rvLastestProduct = findViewById(R.id.rvLastestProduct);
        navigationView = findViewById(R.id.navigationView);
        lvCategory = findViewById(R.id.lvCategory);

        // kt người dùng có đưa dữ liệu vào trong mảng hay không
        if(cartList != null){

        }else{
            cartList = new ArrayList<>();
        }
    }
}