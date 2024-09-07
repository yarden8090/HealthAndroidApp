package com.example.myapplication.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityRegistrationBinding;
import com.example.myapplication.modal.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class registration extends AppCompatActivity {

    // Constant for image pick request
    private static final int PICK_IMAGE_REQUEST = 1;

    // Declare input fields and buttons
    private EditText editTextEmail, editTextPassword, editTextName, editTextPhone, editTextHeight, editTextWeight, editTextEmergencyContact, editTextLocation;
    private Spinner spinnerBloodType;
    private Button buttonUploadImage, buttonRegister;

    // Firebase-related variables
    private FirebaseAuth mAuth; // For user authentication
    private DatabaseReference mDatabaseRef; // To save user data in Firebase Realtime Database
    private StorageReference mStorageRef; // To upload profile image to Firebase Storage
    private Uri imageUri; // Stores the URI of the selected image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and set the layout for the registration page
        setContentView(R.layout.activity_registration);

        // Initialize Firebase services
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users"); // Reference to "Users" node in the Realtime Database
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads"); // Reference to "uploads" folder in Firebase Storage

        // Bind input fields and buttons to their respective views in the layout
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextEmergencyContact = findViewById(R.id.editTextEmergencyContact);
        editTextLocation = findViewById(R.id.editTextLocation);
        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        buttonUploadImage = findViewById(R.id.buttonUploadImage);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Set up the blood type dropdown (spinner) with predefined options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodType.setAdapter(adapter);

        // Set a click listener for the image upload button to allow users to select an image from their device
        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(); // Opens a file chooser to select an image
            }
        });

        // Set a click listener for the registration button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(); // Registers a new user with email and password
            }
        });
    }

    // Function to open the file chooser for selecting an image
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*"); // Restrict to image files
        intent.setAction(Intent.ACTION_GET_CONTENT); // Set action to pick content
        startActivityForResult(intent, PICK_IMAGE_REQUEST); // Start activity to select an image
    }

    // Handling the result after selecting an image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Store the URI of the selected image
        }
    }

    // Function to register a new user using Firebase Authentication
    private void registerUser() {
        String email = editTextEmail.getText().toString().trim(); // Get email input
        String password = editTextPassword.getText().toString().trim(); // Get password input

        // Create a new user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser(); // Get the current user if registration is successful
                        if (user != null) {
                            uploadImageToFirebase(user.getUid()); // Upload the profile image to Firebase Storage
                        }
                    } else {
                        Toast.makeText(registration.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Function to upload the selected image to Firebase Storage
    private void uploadImageToFirebase(final String userId) {
        if (imageUri != null) {
            // Reference to where the image will be stored in Firebase Storage
            StorageReference fileReference = mStorageRef.child(userId + "." + getFileExtension(imageUri));

            // Upload the image to Firebase Storage
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString(); // Get the image URL after upload
                        saveUserDataToDatabase(userId, imageUrl); // Save user data along with the image URL to the database
                    }))
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(registration.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Function to save the user data to Firebase Realtime Database
    private void saveUserDataToDatabase(String userId, String imageUrl) {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        double height = Double.parseDouble(editTextHeight.getText().toString().trim());
        double weight = Double.parseDouble(editTextWeight.getText().toString().trim());
        String emergencyContact = editTextEmergencyContact.getText().toString().trim();
        String bloodType = spinnerBloodType.getSelectedItem().toString();
        String location = editTextLocation.getText().toString().trim();

        // Create a new User object with the input data
        User newUser = new User(name, phone, imageUrl, height, weight, emergencyContact, bloodType, location);

        // Save the user data under their user ID in Firebase Realtime Database
        mDatabaseRef.child(userId).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(registration.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(registration.this, login.class)); // Redirect to login screen after successful registration
                    } else {
                        Toast.makeText(registration.this, "Failed to save user info: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Helper function to get the file extension of the selected image
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri)); // Get MIME type and return file extension
    }
}