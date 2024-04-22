package com.example.greenplate;

import com.example.greenplate.viewModels.CartViewModel;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CartTest {

    @Test
    public void testAddItemToList_ItemAddedToList() {
        CartViewModel cartViewModel = new CartViewModel();

        // Mock data
        String itemName = "Apple";

        // Add item to the list
        cartViewModel.addItemToList(itemName, "5");

        // Get the updated list of items
        List<String> cartItems = cartViewModel.getCartItems();

        // Assert that the item is added to the list
        assertEquals("Item should be added to the list", 1, cartItems.size());
        assertEquals("Item should be added to the list", itemName, cartItems.get(0));
    }

    @Test
    public void testOnCheckboxClicked_ItemRemovedFromList() {
        CartViewModel cartViewModel = new CartViewModel();

        // Mock data
        String itemName = "Apple";

        // Add item to the list
        cartViewModel.addItemToList(itemName, "5");

        // Simulate checkbox clicked event
        cartViewModel.onCheckboxClicked(itemName);

        // Get the updated list of items
        List<String> cartItems = cartViewModel.getCartItems();

        // Assert that the item is removed from the list
        assertEquals("Item should be removed from the list", 0, cartItems.size());
    }

    @Test
    public void testAddItemWithNullName_ItemNotAddedToList() {
        CartViewModel cartViewModel = new CartViewModel();

        // Add item with null name to the list
        cartViewModel.addItemToList(null, "5");

        // Get the updated list of items
        List<String> cartItems = cartViewModel.getCartItems();

        // Assert that no item is added to the list
        assertEquals("No item should be added to the list when item name is null", 0, cartItems.size());
    }

    @Test
    public void testAddItemWithNullQuantity_ItemNotAddedToList() {
        CartViewModel cartViewModel = new CartViewModel();

        // Add item with null quantity to the list
        cartViewModel.addItemToList("Apple", null);

        // Get the updated list of items
        List<String> cartItems = cartViewModel.getCartItems();

        // Assert that no item is added to the list
        assertEquals("No item should be added to the list when quantity is null", 0, cartItems.size());
    }

    @Test
    public void testAddItemWithNullNameAndQuantity_ItemNotAddedToList() {
        CartViewModel cartViewModel = new CartViewModel();

        // Add item with null name and quantity to the list
        cartViewModel.addItemToList(null, null);

        // Get the updated list of items
        List<String> cartItems = cartViewModel.getCartItems();

        // Assert that no item is added to the list
        assertEquals("No item should be added to the list when both item name and quantity are null", 0, cartItems.size());
    }

    @Test
    public void testAddItemWithNegativeQuantity_ItemNotAddedToList() {
        CartViewModel cartViewModel = new CartViewModel();

        // Add item with negative quantity to the list
        cartViewModel.addItemToList("Apple", "-5");

        // Get the updated list of items
        List<String> cartItems = cartViewModel.getCartItems();

        // Assert that no item is added to the list
        assertEquals("No item should be added to the list when quantity is negative", 0, cartItems.size());
    }
}
