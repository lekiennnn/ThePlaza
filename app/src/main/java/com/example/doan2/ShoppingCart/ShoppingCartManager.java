package com.example.doan2.ShoppingCart;

import com.example.doan2.Product.Item;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartManager {

    private static ShoppingCartManager instance;
    private List<Item> shoppingCart;

    private ShoppingCartManager() {
        shoppingCart = new ArrayList<>();
    }

    public static synchronized ShoppingCartManager getInstance() {
        if (instance == null) {
            instance = new ShoppingCartManager();
        }
        return instance;
    }

    public void addItem(Item newItem) {
        for (Item item : shoppingCart) {
            if (item.getId().equals(newItem.getId())) {
                return;  // Item already exists in the cart, do not add again
            }
        }
        shoppingCart.add(newItem);
    }

    public List<Item> getShoppingCart() {
        return new ArrayList<>(shoppingCart);
    }


    public void setShoppingCart(List<Item> newCart) {
        this.shoppingCart = new ArrayList<>(newCart);
    }
}
