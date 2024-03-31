package com.example.greenplate.views;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AlphabeticalSortingStrategy implements SortingStrategy {
    @Override
    public void sort(List<Cookbook> recipes) {
        Collections.sort(recipes, new Comparator<Cookbook>() {
            @Override
            public int compare(Cookbook r1, Cookbook r2) {
                return r1.getRecipeName().compareToIgnoreCase(r2.getRecipeName());
            }
        });
    }
}

