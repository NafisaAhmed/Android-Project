package com.example.user.onyaproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.user.onyaproject.Model.FoodItems;
import com.example.user.onyaproject.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class FoodDetailsActivity extends AppCompatActivity {

    private Button addtoCartBtn;
    private ImageView foodImage;
    private ElegantNumberButton numberButton;
    private TextView foodPrice,foodDescription,foodName;
    private String foodId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_food_details );
        foodId=getIntent().getStringExtra( "pid" );
        addtoCartBtn = (Button) findViewById( R.id.add_food_to_cart_btn );
        numberButton = (ElegantNumberButton) findViewById( R.id.number_btn );
        foodImage = (ImageView) findViewById( R.id.food_image_details );
        foodName = (TextView) findViewById( R.id.food_name_details );
        foodDescription = (TextView) findViewById( R.id.food_description_details );
        foodPrice = (TextView) findViewById( R.id.food_price_details );
        getFoodDetails(foodId);
        addtoCartBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        } );
    }

    private void addingToCartList() {
        String saveCurrentTime,saveCurrentDate;
        Calendar callForDate=Calendar.getInstance();
        SimpleDateFormat currentData=new SimpleDateFormat( "MMM dd,yyyy" );
        saveCurrentDate=currentData.format( callForDate.getTime() );
        SimpleDateFormat currentTime=new SimpleDateFormat( "HH:mm:ss a" );
        saveCurrentTime=currentData.format( callForDate.getTime() );

    final  DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child( "Cart List" );
   final HashMap <String,Object> cartMap=new HashMap<>( );
       cartMap.put( "pid",foodId );
       cartMap.put( "pname",foodName.getText().toString() );
       cartMap.put( "price",foodPrice.getText().toString() );
       cartMap.put( "date",saveCurrentDate);
       cartMap.put( "time",saveCurrentTime );
        cartMap.put( "quantity",numberButton.getNumber() );
        cartMap.put( "discount","" );
        cartListRef.child( "User View").child( Prevalent.currentOnlineUser.getPhone())
                .child("Foods").child(foodId )
                .updateChildren( cartMap )
                .addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                      {
                          cartListRef.child( "Admin View").child( Prevalent.currentOnlineUser.getPhone())
                                  .child("Foods").child(foodId )
                                  .updateChildren( cartMap )
                                  .addOnCompleteListener( new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                          if(task.isSuccessful())
                                          {
                                              Toast.makeText(FoodDetailsActivity.this,"Added to Cart List",Toast.LENGTH_SHORT).show();
                                              Intent intent=new Intent(FoodDetailsActivity.this,HomeActivity.class);
                                              startActivity( intent );
                                          }

                                      }
                                  } );
                      }
                    }
                } );


    }

    private void getFoodDetails(String foodId) {
        DatabaseReference foodRef= FirebaseDatabase.getInstance().getReference().child( "Foods" );
        foodRef.child(foodId ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    FoodItems foods=dataSnapshot.getValue(FoodItems.class);
                    foodDescription.setText( foods.getDescription() );
                    foodName.setText(foods.getPname() );
                    foodPrice.setText(foods.getPrice() );
                    Picasso.get().load( foods.getImage()).into(foodImage );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }
}
