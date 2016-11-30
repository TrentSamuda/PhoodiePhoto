package com.fte.feedthecause;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by trenton on 11/28/16.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;

    //List to store all superheroes
    List<ImageBreakdown> foodPics;

    public CardAdapter(List<ImageBreakdown> foodPics, Context context){
        super();
        //Getting all superheroes
        this.foodPics = foodPics;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewing_photo, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Getting the particular item from the list
        ImageBreakdown foodPic =  foodPics.get(position);

        //Loading image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(foodPic.getImageURL(), ImageLoader.getImageListener(holder.imageView, R.drawable.sample_0, android.R.drawable.ic_dialog_alert));

        //Showing data on the views
        holder.imageView.setImageUrl(foodPic.getImageURL(), imageLoader);
        //holder.textViewName.setText(foodPic.);
        holder.textViewOwner.setText(foodPic.getOwner());
        holder.textViewDateCreated.setText(foodPic.getTimePosted());
        holder.textViewTitle.setText(foodPic.getTitle());
        holder.textViewDescription.setText(foodPic.getDescription());
        holder.textViewKeywords.setText(foodPic.getKeywords());
        holder.textViewIngredients.setText(foodPic.getIngredients());
        holder.textViewInstructions.setText(foodPic.getInstructions());
        holder.textViewOther.setText(foodPic.getOther());
        //holder.textViewLikes.setText(foodPic.getLikeCounter());

    }

    @Override
    public int getItemCount() {
        return foodPics.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public NetworkImageView imageView;
        //public TextView textViewName;
        public TextView textViewOwner;
        public TextView
                textViewDateCreated,
                    textViewTitle,
                    textViewDescription,
                    textViewKeywords,
                    textViewIngredients,
                    textViewInstructions,
                    textViewOther,
                    textViewLikes;


        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewFood);
            //textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewOwner = (TextView) itemView.findViewById(R.id.textViewOwner);
            textViewDateCreated = (TextView) itemView.findViewById(R.id.textViewDateCreated);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            textViewKeywords = (TextView) itemView.findViewById(R.id.textViewKeywords);
            textViewIngredients = (TextView) itemView.findViewById(R.id.textViewIngredients);
            textViewInstructions = (TextView) itemView.findViewById(R.id.textViewInstruction);
            textViewOther = (TextView) itemView.findViewById(R.id.textViewOther);
            textViewLikes = (TextView) itemView.findViewById(R.id.textViewLikes);
        }
    }
}
