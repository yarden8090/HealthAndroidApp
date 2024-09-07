package com.example.myapplication.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.modal.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewProfile;
    private EditText editTextName, editTextPhone, editTextHeight, editTextWeight, editTextEmergencyContact, editTextLocation;
    private Spinner spinnerBloodType;
    private Button buttonUpdateProfile, buttonDeleteAccount;

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Uri imageUri;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        // Initialize Views
        imageViewProfile = findViewById(R.id.imageViewProfile);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextEmergencyContact = findViewById(R.id.editTextEmergencyContact);
        editTextLocation = findViewById(R.id.editTextLocation);
        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile);
        buttonDeleteAccount = findViewById(R.id.buttonDeleteAccount);

        // Set up Blood Type Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodType.setAdapter(adapter);

        // Load User Info
        loadUserInfo();

        // Set Listeners
        imageViewProfile.setOnClickListener(v -> openFileChooser());

        buttonUpdateProfile.setOnClickListener(v -> updateUserProfile());

        buttonDeleteAccount.setOnClickListener(v -> deleteUserAccount1());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewProfile.setImageURI(imageUri);
        }
    }

    private void loadUserInfo() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        editTextName.setText(user.getName());
                        editTextPhone.setText(user.getPhone());
                        editTextHeight.setText(String.valueOf(user.getHeight()));
                        editTextWeight.setText(String.valueOf(user.getWeight()));
                        editTextEmergencyContact.setText(user.getEmergencyContact());
                        editTextLocation.setText(user.getLocation());
                        imageUrl = user.getImageUrl();

                        // Load the image using Glide
                        Glide.with(ProfileActivity.this).load(imageUrl).into(imageViewProfile);

                        // Set the spinner value for Blood Type
                        String[] bloodTypes = getResources().getStringArray(R.array.blood_types);
                        for (int i = 0; i < bloodTypes.length; i++) {
                            if (bloodTypes[i].equals(user.getBloodType())) {
                                spinnerBloodType.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load user info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        double height = Double.parseDouble(editTextHeight.getText().toString().trim());
        double weight = Double.parseDouble(editTextWeight.getText().toString().trim());
        String emergencyContact = editTextEmergencyContact.getText().toString().trim();
        String bloodType = spinnerBloodType.getSelectedItem().toString();
        String location = editTextLocation.getText().toString().trim();

        if (imageUri != null) {
            StorageReference fileReference = mStorageRef.child(mUser.getUid() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                imageUrl = uri.toString();
                updateUserDatabase(name, phone, imageUrl, height, weight, emergencyContact, bloodType, location);
            })).addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            updateUserDatabase(name, phone, imageUrl, height, weight, emergencyContact, bloodType, location);
        }
    }

    private void updateUserDatabase(String name, String phone, String imageUrl, double height, double weight, String emergencyContact, String bloodType, String location) {
        User updatedUser = new User(name, phone, imageUrl, height, weight, emergencyContact, bloodType, location);
        mDatabaseRef.setValue(updatedUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteUserAccount() {
        mUser.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mDatabaseRef.removeValue().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to delete account data", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteUser() {

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void deleteUserAccount1() {
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads").child(mUser.getUid()+".jpg");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());
        // Step 1: Delete user's profile image from Firebase Storage
        mStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Step 2: Delete user data from Firebase Realtime Database
                mDatabaseRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Step 3: Delete user from Firebase Authentication
                            Objects.requireNonNull(mAuth.getCurrentUser()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ProfileActivity.this, login.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Handle failure to delete from Authentication +
                                        Toast.makeText(ProfileActivity.this, "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // Handle failure to delete from Realtime Database +
                            Toast.makeText(ProfileActivity.this, "Failed to delete user data. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure to delete from Storage +
                Toast.makeText(ProfileActivity.this, "Failed to delete profile image. Please try again.1"+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                editTextName.setText(e.getMessage().toString());
            }
        });
    }
}
