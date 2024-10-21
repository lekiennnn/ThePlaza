package com.example.doan2.ShoppingCart;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan2.Product.Item;
import com.example.doan2.R;
import com.example.doan2.Interface.RecyclerViewInterface;
import com.example.doan2.Event.ViewUtils;

import java.util.Iterator;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity implements RecyclerViewInterface {

    RecyclerView shopping_cart_items;
    List<Item> shoppingCart;
    CheckBox selectAll_cb;
    ShoppingCartAdapter shoppingCartAdapter;
    Button Purchase;
    TextView total_cost_txt;
    ImageButton delete_from_shopping_cart;
    SearchView searchView;
    ImageButton Logo, sort;
    boolean isAzSorted = true;
    boolean isPriceSorted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // Sort
        sort = findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortMenu(v);
            }
        });

        // SearchView
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ViewUtils.filterList(newText, shoppingCart, shoppingCartAdapter);
                return true;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logo.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) searchView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                searchView.setLayoutParams(params);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Logo.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) searchView.getLayoutParams();
                params.width = 0;
                params.weight = 1;
                searchView.setLayoutParams(params);
                return false;
            }
        });

        // Delete
        delete_from_shopping_cart = findViewById(R.id.delete_from_shopping_cart);
        delete_from_shopping_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator<Item> iterator = shoppingCart.iterator();
                while (iterator.hasNext()) {
                    Item item = iterator.next();
                    if (item.isSelected()) {
                        iterator.remove();
                    }
                }
                ShoppingCartManager.getInstance().setShoppingCart(shoppingCart);
                shoppingCartAdapter.notifyDataSetChanged();
                updateTotalCost();
            }
        });

        // Total cost
        total_cost_txt = findViewById(R.id.total_cost_txt);

        // Shopping cart
        shoppingCart = ShoppingCartManager.getInstance().getShoppingCart();

        shopping_cart_items = findViewById(R.id.shopping_cart_items);
        shopping_cart_items.setLayoutManager(new LinearLayoutManager(this));

        shoppingCartAdapter = new ShoppingCartAdapter(shoppingCart, this, this);
        shopping_cart_items.setAdapter(shoppingCartAdapter);

        selectAll_cb = findViewById(R.id.selectAll_cb);
        selectAll_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (Item item : shoppingCart) {
                    item.setSelected(isChecked);
                }
                shoppingCartAdapter.notifyDataSetChanged();
                updateTotalCost();
            }
        });

        // Logo
        Logo = findViewById(R.id.Logo);
        ViewUtils.setLogoClickListener(this, Logo);

        //Purchase button
        Purchase = findViewById(R.id.Purchase);
        Purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnyItemSelected()) {
                    double totalCost = calculateTotalCost();
                    Intent intent = new Intent(ShoppingCartActivity.this, PurchaseActivity.class);
                    intent.putExtra("TOTAL_COST", totalCost);
                    startActivity(intent);
                } else {
                    Toast.makeText(ShoppingCartActivity.this, "Please select at least one item to purchase", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateTotalCost();
    }

    // Total cost
    private double calculateTotalCost() {
        double totalCost = 0;
        for (Item item : shoppingCart) {
            if (item.isSelected()) {
                totalCost += Double.parseDouble(item.getPrice()) * item.getQuantity();
            }
        }
        return totalCost;
    }

    // Total cost
    public void updateTotalCost() {
        double totalCost = 0;
        for (Item item : shoppingCart) {
            if (item.isSelected()) {
                totalCost += Double.parseDouble(item.getPrice()) * item.getQuantity();
            }
        }
        total_cost_txt.setText(totalCost + "$");
    }

    // Check if any item in shopping cart is selected
    private boolean isAnyItemSelected() {
        for (Item item : shoppingCart) {
            if (item.isSelected()) {
                return true;
            }
        }
        return false;
    }

    // Select all checkbox
    public void setSelectAllCheckbox(boolean isSelected) {
        selectAll_cb.setOnCheckedChangeListener(null);
        selectAll_cb.setChecked(isSelected);
        selectAll_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (Item item : shoppingCart) {
                    item.setSelected(isChecked);
                }
                shoppingCartAdapter.notifyDataSetChanged();
                updateTotalCost();
            }
        });
    }

    // Sort menu
    private void showSortMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.sort_az) {
                    ViewUtils.sortByName(shoppingCart, shoppingCartAdapter, isAzSorted);
                    isAzSorted = !isAzSorted;
                    return true;
                } else if (id == R.id.sort_price) {
                    ViewUtils.sortByPrice(shoppingCart, shoppingCartAdapter, isPriceSorted);
                    isPriceSorted = !isPriceSorted;
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    @Override
    public void onItemClick(int position) {
    }
}
