package com.example.doan2.Product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doan2.Interface.FilterableAdapter;
import com.example.doan2.R;
import com.example.doan2.Interface.RecyclerViewInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends BaseAdapter implements FilterableAdapter {

    private List<Item> listedItems;
    private final Context context;
    private final RecyclerViewInterface recyclerViewInterface;

    public ProductAdapter(Context context, List<Item> listedItems, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.listedItems = listedItems;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    public void setFilteredList(List<Item> filteredList) {
        this.listedItems = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listedItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listedItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false);
        }

        ImageView image_img = convertView.findViewById(R.id.image_img);
        TextView name_txt = convertView.findViewById(R.id.name_txt);
        TextView price_txt = convertView.findViewById(R.id.price_txt);

        Item currentItem = listedItems.get(position);
        String capitalizedItemName = currentItem.getName().substring(0, 1).toUpperCase() + currentItem.getName().substring(1);

        // Use Picasso to load image into ImageView
        Picasso.get().load(currentItem.getImage()).into(image_img);

        // Set other item details
        name_txt.setText(capitalizedItemName);
        price_txt.setText(currentItem.getPrice() + "$");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewInterface != null) {
                    recyclerViewInterface.onItemClick(position);
                }
            }
        });

        return convertView;
    }
}
