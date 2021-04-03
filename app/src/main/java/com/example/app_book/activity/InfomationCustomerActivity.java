package com.example.app_book.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_book.R;
import com.example.app_book.connect.CheckConnection;
import com.example.app_book.server.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InfomationCustomerActivity extends AppCompatActivity {

    private EditText edtName, edtEMail, edtPhone;
    private Button btnOK, btnCannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation_customer);
        mapping();
        btnCannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        EventButtonInfo();
    }

    private void EventButtonInfo() {
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameCustomer = edtName.getText().toString();
                String phoneCustomer = edtPhone.getText().toString();
                String emailCustomer = edtEMail.getText().toString();

                if(nameCustomer.length() > 0 && phoneCustomer.length() > 0 && emailCustomer.length() > 0){
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, StringUtil.URL_POST_CART_CUSTOMER_INFO, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String idorder) {
                            Log.d("madonhang", idorder);
                            if(Integer.parseInt(idorder) > 0){
                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                StringRequest request = new StringRequest(Request.Method.POST, StringUtil.URL_POST_CART_ORDER, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("1")){
                                            MainActivity.cartList.clear();
                                            CheckConnection.ShowToast_short(getApplicationContext(), "Bạn đã mua hàng thành công, chúng tôi sẽ liêu hệ với bạn sau!!");
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            CheckConnection.ShowToast_short(getApplicationContext(), "Mời bạn tiếp tục mua hàng");
                                        }else{
                                            CheckConnection.ShowToast_short(getApplicationContext(), "Dữ liệu giỏ hàng của bạn đã bị lỗi, mời bạn kiểm tra lại ");
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        JSONArray jsonArray = new JSONArray();
                                        for (int i = 0; i < MainActivity.cartList.size(); i++) {
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("idorder", idorder);
                                                jsonObject.put("idproduct", MainActivity.cartList.get(i).getIdProduct());
                                                jsonObject.put("nameproduct", MainActivity.cartList.get(i).getNameProduct());
                                                jsonObject.put("priceproduct", MainActivity.cartList.get(i).getPriceProduct());
                                                jsonObject.put("amountproduct", MainActivity.cartList.get(i).getAmountProduct());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            jsonArray.put(jsonObject);
                                        }
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("json", jsonArray.toString().trim());
                                        return map;
                                    }
                                };
                                queue.add(request);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("namecustomer", nameCustomer);
                            hashMap.put("phonecustomer", phoneCustomer);
                            hashMap.put("emailcustomer", emailCustomer);
                            return hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);

                }else {
                    CheckConnection.ShowToast_short(getApplicationContext(), "Hãy kiểm tra lại dữ liệu");
                }
            }
        });
    }

    private void mapping() {
        edtName = findViewById(R.id.edtName);
        edtEMail = findViewById(R.id.edtEMail);
        edtPhone = findViewById(R.id.edtPhone);
        btnCannel = findViewById(R.id.btnCannel);
        btnOK = findViewById(R.id.btnOK);
    }


}