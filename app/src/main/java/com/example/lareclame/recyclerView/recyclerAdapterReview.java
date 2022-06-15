package com.example.lareclame.recyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.lareclame.ProfileActivity;
import com.example.lareclame.R;
import com.example.lareclame.models.Review;
import com.example.lareclame.requests.GetUserInfoRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class recyclerAdapterReview extends RecyclerView.Adapter<recyclerAdapterReview.MyViewHolder> {
    private ArrayList <Review> reviewsList;

    public recyclerAdapterReview(ArrayList <Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView tv_name;
        private final TextView tv_title;
        private final TextView tv_text;
        private final TextView created;
        private final RatingBar ratingBar;
        private final CircleImageView user_photo;


        public MyViewHolder(final View View) {
            super(View);
            tv_name = View.findViewById(R.id.username);
            tv_title = View.findViewById(R.id.review_title);
            tv_text = View.findViewById(R.id.review_text);
            created = View.findViewById(R.id.created);
            ratingBar = View.findViewById(R.id.rating_bar);
            user_photo = View.findViewById(R.id.user_photo);
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
        holder.tv_name.setText(review.getAuthor().getUsername());
        holder.tv_title.setText(review.getTitle());
        holder.tv_text.setText(review.getDescription());
        holder.created.setText(review.getCreated());
        holder.ratingBar.setRating(review.getRating());

        String user_photo = review.getAuthor().getImage();
        if (!user_photo.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(user_photo, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            holder.user_photo.setImageBitmap(bitmap);
        }

        holder.tv_name.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ProfileActivity.class);
            intent.putExtra("not_owner", true);

            Response.Listener<String> listener = response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int user_id = jsonObject.getInt("id");
                    int rating = jsonObject.getInt("rating");
                    String username = jsonObject.getString("username");
                    String telegram = jsonObject.getString("telegram");
                    String bio = jsonObject.getString("bio");
                    String picture = URLDecoder.decode(jsonObject.getString("image"), StandardCharsets.UTF_8.name());

                    intent.putExtra("user_id", user_id);
                    intent.putExtra("rating", rating);
                    intent.putExtra("username", username);
                    intent.putExtra("telegram", telegram);
                    intent.putExtra("bio", bio);
                    intent.putExtra("picture", picture);



                    view.getContext().startActivity(intent);
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            };

            GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest(review.getAuthor().getId(), listener, System.out::println);
            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            requestQueue.add(getUserInfoRequest);
        });
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

}
