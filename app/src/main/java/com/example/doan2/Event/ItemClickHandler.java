package com.example.doan2.Event;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.doan2.Product.Item;
import com.example.doan2.Interface.RecyclerViewInterface;

import java.util.List;

public class ItemClickHandler implements RecyclerViewInterface {
    private Context context;
    private List<Item> listedItems;

    public ItemClickHandler(Context context, List<Item> listedItems) {
        this.context = context;
        this.listedItems = listedItems;
    }

    public void updateItemList(List<Item> updatedList) {
        this.listedItems = updatedList;
    }

    @Override
    public void onItemClick(int position) {
        if (listedItems != null && position < listedItems.size()) {
            Item item = listedItems.get(position);

            Log.d("ItemClickHandler", "Item clicked: " + item.getName());

            String itemNameCapitalized = item.getName().substring(0, 1).toUpperCase() + item.getName().substring(1);

            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("item_id", item.getId());
            intent.putExtra("item_name", itemNameCapitalized);
            intent.putExtra("item_description", item.getDescription());
            intent.putExtra("item_price", item.getPrice());
            intent.putExtra("item_image", item.getImage());

            Log.d("ItemClickHandler", "Starting ItemDetailActivity with item: " + itemNameCapitalized);

            context.startActivity(intent);
        } else {
            Log.e("ItemClickHandler", "Error: listedItems is null or position is out of bounds");
        }
    }
}
