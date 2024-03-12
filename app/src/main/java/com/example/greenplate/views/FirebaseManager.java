package com.example.greenplate.views;
import com.example.greenplate.model.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class FirebaseManager {
        private static FirebaseManager instance;
        private DatabaseReference rootDatabaseRef;
        private FirebaseAuth mAuth;

        private FirebaseManager() {
            rootDatabaseRef = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
        }

        public static synchronized FirebaseManager getInstance() {
            if (instance == null) {
                instance = new FirebaseManager();
            }
            return instance;
        }

        public DatabaseReference getRef() {
            return rootDatabaseRef;
        }

        public FirebaseAuth getAuth() {
            return mAuth;
        }
}
