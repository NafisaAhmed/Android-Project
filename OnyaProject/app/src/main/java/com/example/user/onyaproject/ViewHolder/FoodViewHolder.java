package com.example.user.onyaproject.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;


import com.example.user.onyaproject.Interface.ItemClickListner;
import com.example.user.onyaproject.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtFoodName, txtFoodDescription, txtFoodPrice;
    public ImageView imageView;
    public ItemClickListner listner;


    public FoodViewHolder(@NonNull View itemView) {
        super( itemView );


        imageView = (ImageView) itemView.findViewById(R.id.Food_image);
        txtFoodName = (TextView) itemView.findViewById(R.id.Food_name);
        txtFoodDescription = (TextView) itemView.findViewById(R.id.Food_description);
        txtFoodPrice = (TextView) itemView.findViewById(R.id.Food_price);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }


     @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
