package com.example.myapplication.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class FrstAID extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_frst_aid);

        // Set an OnApplyWindowInsetsListener to adjust the view when system window insets (e.g., status and navigation bars) change
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Get the system bars' insets (e.g., status and navigation bars)
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Add padding to the view based on the system bars' insets to prevent content from overlapping with system UI
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
