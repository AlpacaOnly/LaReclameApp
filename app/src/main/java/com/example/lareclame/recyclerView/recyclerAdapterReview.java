package com.example.lareclame.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lareclame.R;
import com.example.lareclame.reviews.Review;

import java.util.ArrayList;

public class recyclerAdapterReview extends RecyclerView.Adapter<recyclerAdapterReview.MyViewHolder> {
    private ArrayList <Review> reviewsList;

    public recyclerAdapterReview(ArrayList <Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView tv_name;
        private final TextView tv_title;
        private final TextView tv_text;
        private final RatingBar ratingBar;


        public MyViewHolder(final View View) {
            super(View);
            tv_name = View.findViewById(R.id.username);
            tv_title = View.findViewById(R.id.review_title);
            tv_text = View.findViewById(R.id.review_text);
            ratingBar = View.findViewById(R.id.rating_bar);
        }
    }

    @NonNull
    @Override
    public recyclerAdapterReview.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reviews, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapterReview.MyViewHolder holder, int position) {
        Review review = reviewsList.get(position);
        holder.tv_name.setText(review.getTitle());
        holder.tv_title.setText(review.getTitle());
        holder.tv_text.setText(review.getDescription());
        holder.ratingBar.setRating(review.getRating());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

}
