package com.example.greenplate.viewModels;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel {

    private List<String> cartItems;

    public CartViewModel() {
        cartItems = new ArrayList<>();
    }

    public List<String> getCartItems() {
        return cartItems;
    }

    public void addItemToList(String itemName, String quantity) {
        // Check if itemName and quantity are not null
        if (itemName != null && quantity != null && (Integer.parseInt(quantity) > 0)) {
            cartItems.add(itemName);
        }
    }

    public void onCheckboxClicked(String itemName) {
        cartItems.remove(itemName);
    }
}
