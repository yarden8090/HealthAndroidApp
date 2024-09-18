package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.modal.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    // List of reviews to be displayed
    private List<Review> reviewList;

    // Constructor for ReviewAdapter
    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override

    //Creates a new ViewHolder for the RecyclerView.
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (review_item.xml) and create a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    //Binds the data to the ViewHolder at the specified position.
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        //Get the review object at the specified position
        Review review = reviewList.get(position);
        //Bind the review data to the ViewHolder
        holder.feedbackText.setText(review.getFeedback());
        holder.ratingBar.setRating(review.getRating());
    }



    //Returns the total number of items in the data set.
    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    //ViewHolder class for holding references to the views in a single item of the RecyclerView.
    class ReviewViewHolder extends RecyclerView.ViewHolder {
        // Views in the item layout
        TextView feedbackText;
        RatingBar ratingBar;

        // Constructor for ReviewViewHolder.   get parameter view calling father constructor send him view for starting ViewHolder
        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            feedbackText = itemView.findViewById(R.id.tvFeedback);
            ratingBar = itemView.findViewById(R.id.rbRating);
        }
    }
}