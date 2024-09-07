package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.DiseaseAdapter;
import com.example.myapplication.modal.Disease;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity  extends AppCompatActivity {

    private RecyclerView recyclerViewDiseases;
    private DiseaseAdapter diseaseAdapter;
    private List<Disease> diseases;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerViewDiseases = findViewById(R.id.recyclerViewDiseases);
        recyclerViewDiseases.setLayoutManager(new LinearLayoutManager(this));

        diseases = new ArrayList<>();
        diseaseAdapter = new DiseaseAdapter(diseases);
        recyclerViewDiseases.setAdapter(diseaseAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Fetch data from Firestore
        fetchDataFromFirestore();
    }

    private void fetchDataFromFirestore() {
        db.collection("diseases")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                String symptoms = document.getString("symptoms");
                                String whenToSeeDoctor = document.getString("whenToSeeDoctor");

                                // Create a new Disease object and add it to the list
                                Disease disease = new Disease(name, symptoms, whenToSeeDoctor);
                                diseases.add(disease);
                            }

                            // Notify adapter of data changes
                            diseaseAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("Firestore", "Ërror getting documents.", task.getException());
                        }
                    }
                });
    }
}
