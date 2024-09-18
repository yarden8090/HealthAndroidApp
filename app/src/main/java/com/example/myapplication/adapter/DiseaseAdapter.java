package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.modal.Disease;

import java.util.List;

//Adapter class for binding disease data to the RecyclerView
public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> {

    private List<Disease> diseaseList;
    //Constructor for DiseaseAdapter.
    public DiseaseAdapter(List<Disease> diseaseList) {
        this.diseaseList = diseaseList;
    }

    @NonNull
    @Override
    //Creates and returns a ViewHolder for the given view type.
    public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_disease, parent, false);
        return new DiseaseViewHolder(view);
    }

    @Override
    //Binds the data to the provided ViewHolder.
    public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
        Disease disease = diseaseList.get(position);
        holder.nameTextView.setText(disease.getName());
        holder.symptomsTextView.setText(disease.getSymptoms());
        holder.whenToSeeDoctorTextView.setText(disease.getWhenToSeeDoctor());
    }

    // Returns the total number of items in the data set.
    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    // ViewHolder class for holding the views for each item in the RecyclerView.
    public static class DiseaseViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView symptomsTextView;
        public TextView whenToSeeDoctorTextView;

        // Constructor for DiseaseViewHolder.
        public DiseaseViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            symptomsTextView = itemView.findViewById(R.id.symptomsTextView);
            whenToSeeDoctorTextView = itemView.findViewById(R.id.whenToSeeDoctorTextView);
        }
    }
}


