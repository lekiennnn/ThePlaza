package com.example.doan2.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan2.Product.Item;
import com.example.doan2.R;
import com.example.doan2.ShoppingCart.ShoppingCartManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class ItemDetailActivity extends AppCompatActivity {

    Button add_to_shopping_cart;
    FloatingActionButton shopping_cart;
    Item selectedItem;
    ImageButton Logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Shopping cart button
        shopping_cart = findViewById(R.id.shopping_cart);
        ViewUtils.setShoppingCartClickListener(this, shopping_cart);

        // Logo button
        Logo = findViewById(R.id.Logo);
        ViewUtils.setLogoClickListener(this, Logo);

        // Retrieve item details from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String itemId = intent.getStringExtra("item_id");
            String itemName = intent.getStringExtra("item_name");
            String itemDescription = intent.getStringExtra("item_description");
            String itemPrice = intent.getStringExtra("item_price");
            String itemImage = intent.getStringExtra("item_image");

            // Populate views with item details
            ImageView imageView = findViewById(R.id.detail_image_img);
            TextView nameTextView = findViewById(R.id.detail_name_txt);
            TextView priceTextView = findViewById(R.id.detail_price_txt);
            TextView descriptionTextView = findViewById(R.id.detail_description_txt);

            Picasso.get().load(itemImage).into(imageView);
            nameTextView.setText(itemName);
            priceTextView.setText("$ " + itemPrice);
            descriptionTextView.setText("Description: " + itemDescription);

            // Create the selected item
            selectedItem = new Item();
            selectedItem.setId(itemId);
            selectedItem.setName(itemName);
            selectedItem.setDescription(itemDescription);
            selectedItem.setPrice(itemPrice);
            selectedItem.setImage(itemImage);
            selectedItem.setQuantity(1);  // Ensure quantity is 1 for newly added items
        }

        // Add to shopping cart button
        add_to_shopping_cart = findViewById(R.id.add_to_shopping_cart);
        add_to_shopping_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the selected item to the shopping cart list
                ShoppingCartManager.getInstance().addItem(selectedItem);
                Toast.makeText(ItemDetailActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
