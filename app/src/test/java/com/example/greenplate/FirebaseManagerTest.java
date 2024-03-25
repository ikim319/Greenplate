package com.example.greenplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import com.example.greenplate.views.FirebaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;

public class FirebaseManagerTest {

    private FirebaseManager firebaseManager;

    // initializes firebaseManager
    @Before
    public void setUp() {
        firebaseManager = FirebaseManager.getInstance();
    }

    // tests whether firebaseManager is null or not after instantiation
    @Test
    public void testGetInstance() {
        assertNotNull(firebaseManager);
    }

    // tests whether authentication instance obtained from firebaseManager.getAuth() is not null
    // and matches the instance obtained directly from FirebaseAuth.getInstance()
    @Test
    public void testGetRef() {
        DatabaseReference databaseReference = firebaseManager.getRef();
        assertNotNull(databaseReference);
        assertEquals(FirebaseDatabase.getInstance().getReference(), databaseReference);
    }

    // mocks FirebaseManager class to return mocked instance when getInstance() is called
    // tests whether two instances obtained by calling getInstance() are same (singleton?)
    @Test
    public void testGetAuth() {
        FirebaseAuth firebaseAuth = firebaseManager.getAuth();
        assertNotNull(firebaseAuth);
        assertEquals(FirebaseAuth.getInstance(), firebaseAuth);
    }

    // Mocked test for getInstance()
    /*
    @Test
    public void testGetInstanceSingleton() {
        FirebaseManager mockedInstance = mock(FirebaseManager.class);
        when(FirebaseManager.getInstance()).thenReturn(mockedInstance);

        FirebaseManager instance1 = FirebaseManager.getInstance();
        FirebaseManager instance2 = FirebaseManager.getInstance();

        assertEquals(mockedInstance, instance1);
        assertEquals(instance1, instance2);
    }

     */

    // test to verify that reinitializing FirebaseManager returns the same instance
    // @Test
    // public void testReinitializeFirebaseManager() {
    //     FirebaseManager instance1 = FirebaseManager.getInstance();

    //     FirebaseManager instance2 = FirebaseManager.getInstance();

    //     // Ensure the reinitialized instance is the same as the original instance
    //     assertSame(instance1, instance2);
    // }
}

