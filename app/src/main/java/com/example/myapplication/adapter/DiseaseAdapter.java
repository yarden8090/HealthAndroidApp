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

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> {

    private List<Disease> diseaseList;

    public DiseaseAdapter(List<Disease> diseaseList) {
        this.diseaseList = diseaseList;
    }

    @NonNull
    @Override
    public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_disease, parent, false);
        return new DiseaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
        Disease disease = diseaseList.get(position);
        holder.nameTextView.setText(disease.getName());
        holder.symptomsTextView.setText(disease.getSymptoms());
        holder.whenToSeeDoctorTextView.setText(disease.getWhenToSeeDoctor());
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    public static class DiseaseViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView symptomsTextView;
        public TextView whenToSeeDoctorTextView;

        public DiseaseViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            symptomsTextView = itemView.findViewById(R.id.symptomsTextView);
            whenToSeeDoctorTextView = itemView.findViewById(R.id.whenToSeeDoctorTextView);
        }
    }
}


