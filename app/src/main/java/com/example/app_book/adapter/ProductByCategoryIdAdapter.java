package com.example.app_book.adapter;

import android.content.Context;
import android.text.TextUtils;
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

public class ProductByCategoryIdAdapter extends RecyclerView.Adapter<ProductByCategoryIdAdapter.ViewHolder>{

    Context context;
    int layout;
    List<Product> productList;

    public ProductByCategoryIdAdapter(Context context, int layout, List<Product> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(layout, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product  p = productList.get(position);
        holder.txtvName.setText(p.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtvPrice.setText(decimalFormat.format(p.getPrice()) + " Đ");
        holder.txtvDescription.setMaxLines(2);
        holder.txtvDescription.setEllipsize(TextUtils.TruncateAt.END);
        holder.txtvDescription.setText(p.getDescription());

        Picasso.get().load(p.getAvatar())
//                .placeholder(R.drawable.loading)
//                .error(R.drawable.noimage)
                .into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtvName, txtvPrice, txtvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtvName = itemView.findViewById(R.id.txtvName);
            txtvPrice = itemView.findViewById(R.id.txtvPrice);
            txtvDescription = itemView.findViewById(R.id.txtvDescription);
        }
    }
}
