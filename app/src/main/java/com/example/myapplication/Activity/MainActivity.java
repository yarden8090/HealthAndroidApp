package com.example.myapplication.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.modal.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public ActivityMainBinding binding;
    public NavigationView navigationView;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Uri imageUri;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer

        Button button = findViewById(R.id.button3); // first aid
        Button button1 = findViewById(R.id.button4); //Emergency
        Button button11 = findViewById(R.id.button); //general
        Button button2 = findViewById(R.id.button1); //BMI
        ImageView imageView = findViewById(R.id.img); //profile photo

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads"); //goes to uploads root in storage

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FrstAID.class));
            }
        });
        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, General.class));
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Emergency.class));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BMI.class));
            }
        });

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // Add the drawer toggle listener to the DrawerLayout to respond to open and close events
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        // Synchronize the state of the drawer toggle button with the drawer layout
        actionBarDrawerToggle.syncState();
        // Set an OnClickListener on the drawer icon to open the navigation drawer when clicked

        findViewById(R.id.drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            // Open the drawer from the start (left side)
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        // Initialize the NavigationView
        navigationView = findViewById(R.id.navViw);
        // Set a listener for navigation item selection in the NavigationView
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get the selected item's ID
                int id = item.getItemId();
                if (id == R.id.nav_account) {
                    // Handle Home click
                    startActivity(new Intent(MainActivity.this, Emergency.class));
                } else if (id == R.id.profile) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                } else if (id == R.id.nav_Feedback) {
                    startActivity(new Intent(MainActivity.this, Feadback.class));

                } else if (id == R.id.nav_logout) {
                    startActivity(new Intent(MainActivity.this, login.class));
                }
                // Close the navigation drawer after an item is selected
                drawerLayout.closeDrawers();
                return true;
            }
        });

        // Listen for data changes from the database reference
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the data exists
                if (snapshot.exists()) {
                    // Retrieve the User object from the snapshot
                    User user = snapshot.getValue(User.class);
                    // Get the image URL from the user object
                    if (user != null) {
                        imageUrl = user.getImageUrl();
                        // Load the image into imageView using Glide library
                        Glide.with(MainActivity.this).load(imageUrl).into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load user imag", Toast.LENGTH_SHORT).show();
            }
        });


    }


    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}




