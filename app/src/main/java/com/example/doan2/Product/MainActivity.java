package com.example.doan2.Product;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan2.Event.ItemClickHandler;
import com.example.doan2.R;
import com.example.doan2.Event.ViewUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

//Vi do an cua bon e dung api free, nen khi chay bon e phai chay call api truoc.
//Neu thay muon chay do an, thay lien he em (Kien - 0962926866)
//Bon em cam on thay a ^^

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    FloatingActionButton shopping_cart;
    ImageButton Logo, sort;
    List<Item> listedItems = new ArrayList<>();
    SearchView searchView;
    ProductAdapter productAdapter;
    ItemClickHandler itemClickHandler;
    boolean isAzSorted = true;
    boolean isPriceSorted = true;

    public interface RequestItem {
        @GET("products")
        Call<Item> getItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                ViewUtils.filterList(newText, listedItems, productAdapter);
                return true;
            }
        });

        // Expand SearchView on click
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logo.setVisibility(View.GONE);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) searchView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                searchView.setLayoutParams(params);
            }
        });

        // Collapse SearchView on close
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

        // Product GridView
        gridView = findViewById(R.id.items);

        // Initialize adapter with empty list
        itemClickHandler = new ItemClickHandler(this, listedItems);
        productAdapter = new ProductAdapter(this, listedItems, itemClickHandler);
        gridView.setAdapter(productAdapter);

        // Shopping cart button
        shopping_cart = findViewById(R.id.shopping_cart);
        ViewUtils.setShoppingCartClickListener(this, shopping_cart);

        // Logo
        Logo = findViewById(R.id.Logo);
        ViewUtils.setLogoClickListener(this, Logo);

        // Sort
        sort = findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortMenu(v);
            }
        });

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://prod-server-kh18.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestItem requestItem = retrofit.create(RequestItem.class);

        requestItem.getItem().enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listedItems = response.body().getItems();
                    if (listedItems != null && !listedItems.isEmpty()) {
                        productAdapter.setFilteredList(listedItems);
                        itemClickHandler.updateItemList(listedItems);
                    } else {
                        Toast.makeText(MainActivity.this, "No items found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    onFailure(call, new Exception("Response unsuccessful"));
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable throwable) {
                Log.e("API_Response", "Failed to fetch data: " + throwable.getMessage());
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Sort drop-down menu
    private void showSortMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.sort_az) {
                    ViewUtils.sortByName(listedItems, productAdapter, isAzSorted);
                    isAzSorted = !isAzSorted;
                    return true;
                } else if (id == R.id.sort_price) {
                    ViewUtils.sortByPrice(listedItems, productAdapter, isPriceSorted);
                    isPriceSorted = !isPriceSorted;
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }
}
