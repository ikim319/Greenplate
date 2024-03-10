package com.example.greenplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.greenplate.views.FirebaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;

public class FirebaseManagerTest {

    private FirebaseManager firebaseManager;

    @Before
    public void setUp() {
        firebaseManager = FirebaseManager.getInstance();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(firebaseManager);
    }

    @Test
    public void testGetRef() {
        DatabaseReference databaseReference = firebaseManager.getRef();
        assertNotNull(databaseReference);
        assertEquals(FirebaseDatabase.getInstance().getReference(), databaseReference);
    }

    @Test
    public void testGetAuth() {
        FirebaseAuth firebaseAuth = firebaseManager.getAuth();
        assertNotNull(firebaseAuth);
        assertEquals(FirebaseAuth.getInstance(), firebaseAuth);
    }

    // Mocked test for getInstance()
    @Test
    public void testGetInstanceSingleton() {
        FirebaseManager mockedInstance = mock(FirebaseManager.class);
        when(FirebaseManager.getInstance()).thenReturn(mockedInstance);

        FirebaseManager instance1 = FirebaseManager.getInstance();
        FirebaseManager instance2 = FirebaseManager.getInstance();

        assertEquals(mockedInstance, instance1);
        assertEquals(instance1, instance2);
    }
}

