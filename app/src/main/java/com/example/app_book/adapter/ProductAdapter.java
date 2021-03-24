package com.example.app_book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_book.R;
import com.example.app_book.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    Context context;
    int layout;
    List<Product> productList;

    public ProductAdapter(Context context, int layout, List<Product> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = productList.get(position);
        Picasso.get().load(p.getAvatar()).into(holder.imgProductAvatar);
        holder.txtvProductName.setText(p.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtvProductPrice.setText("Giá: "+decimalFormat.format(p.getPrice())+" Đ");
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProductAvatar;
        TextView txtvProductName, txtvProductPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProductAvatar = itemView.findViewById(R.id.imgProductAvatar);
            txtvProductName = itemView.findViewById(R.id.txtvProductName);
            txtvProductPrice = itemView.findViewById(R.id.txtvProductPrice);
        }
    }
}
