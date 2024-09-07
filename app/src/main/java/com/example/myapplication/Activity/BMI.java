package com.example.myapplication.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BMI extends AppCompatActivity {

        private EditText etHeight, etWeight;
            private Button btnCalculateBMI;
                private TextView tvResult;

                    @Override
        protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        setContentView(R.layout.activity_bmi);

                        // Initialize views
                        etHeight = findViewById(R.id.etHeight);
                        etWeight = findViewById(R.id.etWeight);
                        btnCalculateBMI = findViewById(R.id.btnCalculateBMI);
                        tvResult = findViewById(R.id.tvResult);

                        btnCalculateBMI.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                calculateBMI();
                            }
                        });
                    }

                        private void calculateBMI() {
                                String heightStr = etHeight.getText().toString();
                                        String weightStr = etWeight.getText().toString();

                                                if (heightStr.isEmpty() || weightStr.isEmpty()) {
                                                                Toast.makeText(this, "Please enter height and weight", Toast.LENGTH_SHORT).show();
                                                                            return;
                                                                                    }

                                                        float height = Float.parseFloat(heightStr) / 100; // Convert cm to meters
                                    float weight = Float.parseFloat(weightStr);

                                            float bmi = weight / (height * height);

                                                    String bmiResult;
                                                            if (bmi < 18.5) {
                                                                bmiResult = "Ünderweight ";
                                                                            } else if (bmi >= 18.5 && bmi < 24.9) {
                                                                                bmiResult = "Normal weight";
                                                                               } else if (bmi >= 25 && bmi < 29.9) {
                                                                                    bmiResult = "Överweight";
                                                            } else {
                                                                            bmiResult = "Öbesity";
                                                                    }

                                                                            String resultText = "Your BMI: "+ String.format("%.1f", bmi) + "\nCategory: "+ bmiResult;
                                                                               tvResult.setText(resultText);
                                                                          }
                                                                        }
