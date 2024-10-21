package com.example.doan2.Event;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.example.doan2.Interface.FilterableAdapter;
import com.example.doan2.Product.Item;
import com.example.doan2.Product.MainActivity;
import com.example.doan2.ShoppingCart.ShoppingCartActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewUtils {

    // Logo
    public static void setLogoClickListener(Context context, ImageButton logo) {
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
    }

    // Shopping cart
    public static void setShoppingCartClickListener(Context context, FloatingActionButton shoppingCart) {
        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShoppingCartActivity.class);
                context.startActivity(intent);
            }
        });
    }

    // Search
    public static void filterList(String newText, List<Item> listedItems, FilterableAdapter adapter) {
        List<Item> filteredList = new ArrayList<>();
        for (Item item : listedItems) {
            if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.setFilteredList(filteredList);
    }

    // Sort by name
    public static void sortByName(List<Item> listedItems, FilterableAdapter adapter, boolean isAzSorted) {
        if (isAzSorted) {
            Collections.sort(listedItems, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } else {
            Collections.sort(listedItems, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return o2.getName().compareTo(o1.getName());
                }
            });
        }
        adapter.setFilteredList(listedItems);
    }

    // Sort by price
    public static void sortByPrice(List<Item> listedItems, FilterableAdapter adapter, boolean isPriceSorted) {
        if (isPriceSorted) {
            Collections.sort(listedItems, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return Double.compare(Double.parseDouble(o1.getPrice()), Double.parseDouble(o2.getPrice()));
                }
            });
        } else {
            Collections.sort(listedItems, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return Double.compare(Double.parseDouble(o2.getPrice()), Double.parseDouble(o1.getPrice()));
                }
            });
        }
        adapter.setFilteredList(listedItems);
    }
}
