package com.example.app_book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_book.R;
import com.example.app_book.activity.CartActivity;
import com.example.app_book.activity.MainActivity;
import com.example.app_book.model.Cart;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends BaseAdapter {

    public Context context;
    private List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        ImageView imgProductCart;
        TextView txtNameCart;
        TextView txtPriceCart;
        Button btnMinusCart, btnPlusCart, btnValueCart;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.line_cart, null);
            viewHolder.txtNameCart = convertView.findViewById(R.id.txtNameCart);
            viewHolder.btnValueCart = convertView.findViewById(R.id.btnValueCart);
            viewHolder.txtPriceCart = convertView.findViewById(R.id.txtvPriceCart);
            viewHolder.imgProductCart = convertView.findViewById(R.id.imgProductCart);
            viewHolder.btnMinusCart = convertView.findViewById(R.id.btnMinusCart);
            viewHolder.btnPlusCart = convertView.findViewById(R.id.btnPlusCart);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Cart cart = (Cart) getItem(position);
        viewHolder.txtNameCart.setText(cart.getNameProduct());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtPriceCart.setText(decimalFormat.format(cart.getPriceProduct()) + " Đ");
        Picasso.get().load(cart.getAvatarProduct())
//                .placeholder(R.drawable.loading)
//                .error(R.drawable.noimage)
                .into(viewHolder.imgProductCart);
        viewHolder.btnValueCart.setText(cart.getAmountProduct() + "");
        int sl = Integer.parseInt(viewHolder.btnValueCart.getText().toString());
        if(sl >= 10){
            viewHolder.btnPlusCart.setVisibility(View.INVISIBLE);
            viewHolder.btnMinusCart.setVisibility(View.VISIBLE);
        }else if(sl <= 1){
            viewHolder.btnMinusCart.setVisibility(View.INVISIBLE);
        }else if(sl >= 1){
            viewHolder.btnPlusCart.setVisibility(View.VISIBLE);
            viewHolder.btnMinusCart.setVisibility(View.VISIBLE);
        }
        viewHolder.btnPlusCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slnew  = Integer.parseInt(viewHolder.btnValueCart.getText().toString()) + 1;
                int slht = MainActivity.cartList.get(position).getAmountProduct();
                long priceht = MainActivity.cartList.get(position).getPriceProduct();
                MainActivity.cartList.get(position).setAmountProduct(slnew);
                long pricenew = (priceht * slnew) / slht;
                MainActivity.cartList.get(position).setPriceProduct(pricenew);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                viewHolder.txtPriceCart.setText(decimalFormat.format(pricenew) + " Đ");
                CartActivity.EventUtil();
                if(slnew > 9){ // vì khi click 1 lần đã cộng 1 lần click lần 9 là 10 r
                    viewHolder.btnPlusCart.setVisibility(View.INVISIBLE);
                    viewHolder.btnMinusCart.setVisibility(View.VISIBLE);
                    viewHolder.btnValueCart.setText(String.valueOf(slnew));
                }else{
                    viewHolder.btnMinusCart.setVisibility(View.VISIBLE);
                    viewHolder.btnPlusCart.setVisibility(View.VISIBLE);
                    viewHolder.btnValueCart.setText(String.valueOf(slnew));
                }
            }
        });

        viewHolder.btnMinusCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slnew  = Integer.parseInt(viewHolder.btnValueCart.getText().toString()) - 1;
                int slht = MainActivity.cartList.get(position).getAmountProduct();
                long priceht = MainActivity.cartList.get(position).getPriceProduct();
                MainActivity.cartList.get(position).setAmountProduct(slnew);
                long pricenew = (priceht * slnew) / slht;
                MainActivity.cartList.get(position).setPriceProduct(pricenew);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                viewHolder.txtPriceCart.setText(decimalFormat.format(pricenew) + " Đ");
                CartActivity.EventUtil();
                if(slnew < 2){ // vì khi click 1 lần đã cộng 1 lần click lần 9 là 10 r
                    viewHolder.btnMinusCart.setVisibility(View.INVISIBLE);
                    viewHolder.btnPlusCart.setVisibility(View.VISIBLE);
                    viewHolder.btnValueCart.setText(String.valueOf(slnew));
                }else{
                    viewHolder.btnMinusCart.setVisibility(View.VISIBLE);
                    viewHolder.btnPlusCart.setVisibility(View.VISIBLE);
                    viewHolder.btnValueCart.setText(String.valueOf(slnew));
                }
            }
        });
        return convertView;
    }
}