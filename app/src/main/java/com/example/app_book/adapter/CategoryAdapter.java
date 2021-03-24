package com.example.app_book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_book.R;
import com.example.app_book.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category>  categoryList;
    private int layout;

    public CategoryAdapter(Context context, List<Category> categoryList, int layout) {
        this.context = context;
        this.categoryList = categoryList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categoryList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder.imgIcon = convertView.findViewById(R.id.imgIcon);
            viewHolder.txtvName = convertView.findViewById(R.id.txtvName);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Category category = (Category) getItem(position);
        viewHolder.txtvName.setText(category.getName());
        Picasso.get().load(category.getAvatar()).into(viewHolder.imgIcon);
        return convertView;
    }

    class ViewHolder{
        ImageView imgIcon;
        TextView txtvName;
    }
}
