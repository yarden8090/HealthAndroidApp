package com.example.myapplication.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ReviewAdapter;
import com.example.myapplication.modal.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Feadback extends AppCompatActivity {

    private EditText etFeedback;
    private RatingBar ratingBar;
    private Button btnSubmitFeedback;
    private DatabaseReference reviewsReference;
    private FirebaseAuth mAuth;
    private String user_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feadback);


        etFeedback = findViewById(R.id.etFeedback);
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback);


        mAuth = FirebaseAuth.getInstance();
        user_key = mAuth.getCurrentUser().getUid(); //user key

        reviewsReference = FirebaseDatabase.getInstance().getReference("Reviews"); //reviews root

        btnSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });

        // Load existing reviews (function to load the reviews from Firebase)
        loadExistingReviews();
    }

    private void submitFeedback() {
        String feedback = etFeedback.getText().toString().trim();
        float rating = ratingBar.getRating();

        if (!feedback.isEmpty()) {
            // Create a review object
            HashMap<String, Object> reviewData = new HashMap<>();
            reviewData.put("feedback", feedback);
            reviewData.put("rating", rating);

            // Store the feedback in the Firebase Realtime Database under the user key
            //using root
            reviewsReference.child(user_key).setValue(reviewData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Feadback.this, "Feedback submitted!", Toast.LENGTH_SHORT).show();
                        etFeedback.setText(""); // Clear feedback form
                        ratingBar.setRating(0); // Reset rating bar
                    } else {
                        Toast.makeText(Feadback.this, "Failed to submit feedback.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please enter feedback before submitting.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadExistingReviews() {
        reviewsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Review> reviewList = new ArrayList<>();
                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    reviewList.add(review);
                }
                // Setup the RecyclerView with the reviews

                // Create an instance of ReviewAdapter with the reviewList to be used by the adapter
                ReviewAdapter adapter = new ReviewAdapter(reviewList);

                RecyclerView recyclerView = findViewById(R.id.rvReviews);

                // Set the LayoutManager to determine how items will be displayed in the RecyclerView
                // LinearLayoutManager arranges items in a vertical list (or horizontal if specified)
                recyclerView.setLayoutManager(new LinearLayoutManager(Feadback.this));

                // Set the adapter for the RecyclerView to manage and display the data
                // The adapter is responsible for creating item views and binding data to the RecyclerView
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Feadback.this, "Failed to load reviews.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
