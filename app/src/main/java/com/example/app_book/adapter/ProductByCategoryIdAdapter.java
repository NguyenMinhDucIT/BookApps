package com.example.app_book.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_book.R;
import com.example.app_book.activity.ProductDetail;
import com.example.app_book.interfaces.ItemClickListener;
import com.example.app_book.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductByCategoryIdAdapter extends RecyclerView.Adapter<ProductByCategoryIdAdapter.ViewHolder>{

    public Context context;
    private int layout;
    private List<Product> list;
    private List<Product> arrayList;

    public ProductByCategoryIdAdapter(Context context, int layout, List<Product> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
        arrayList = new ArrayList<Product>();
        this.arrayList.addAll(list);
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
        Product  p = list.get(position);
        holder.txtvName.setText(p.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtvPrice.setText(decimalFormat.format(p.getPrice()) + " Đ");
        holder.txtvDescription.setMaxLines(2);
        holder.txtvDescription.setEllipsize(TextUtils.TruncateAt.END);
        holder.txtvDescription.setText(p.getDescription());

        Picasso.get().load(p.getAvatar())
                .into(holder.imgAvatar);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick){
                    Toast.makeText(context, "Long click" + p, Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new     Intent(context, ProductDetail.class);
                    intent.putExtra("product", p);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        ImageView imgAvatar;
        TextView txtvName, txtvPrice, txtvDescription;
        private ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtvName = itemView.findViewById(R.id.txtvName);
            txtvPrice = itemView.findViewById(R.id.txtvPrice);
            txtvDescription = itemView.findViewById(R.id.txtvDescription);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }
    }
}
