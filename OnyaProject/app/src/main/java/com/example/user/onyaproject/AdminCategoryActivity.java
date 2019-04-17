package com.example.user.onyaproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView bengali_food, biriyani,fried_rice;
    private ImageView  fish, chicken, indian_food;
    private ImageView  burger,  pizza, pasta;
    private ImageView  soup,coffee,dessert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getSupportActionBar().hide();
        setContentView( R.layout.activity_admin_category );
        bengali_food= (ImageView) findViewById(R.id.bengali_food);
         biriyani = (ImageView) findViewById(R.id.biriyani);
        fried_rice = (ImageView) findViewById(R.id.fried_rice);
        fish = (ImageView) findViewById(R.id.fish);

        chicken = (ImageView) findViewById(R.id.chicken);
        indian_food = (ImageView) findViewById(R.id. indian_food);
        burger = (ImageView) findViewById(R.id.burger);
        pizza = (ImageView) findViewById(R.id.pizza);

         pasta = (ImageView) findViewById(R.id.pasta);
         soup = (ImageView) findViewById(R.id.soup);
         coffee= (ImageView) findViewById(R.id.coffee);
        dessert = (ImageView) findViewById(R.id.dessert);


        bengali_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "BengaliFood");
                startActivity(intent);
            }
        });


       biriyani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Biriyani");
                startActivity(intent);
            }
        });


         fried_rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "FriedRice");
                startActivity(intent);
            }
        });


         fish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Fish");
                startActivity(intent);
            }
        });


         chicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Chicken");
                startActivity(intent);
            }
        });


         indian_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "IndianFood");
                startActivity(intent);
            }
        });



         burger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Burger");
                startActivity(intent);
            }
        });


         pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Pizza");
                startActivity(intent);
            }
        });



        pasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Pasta");
                startActivity(intent);
            }
        });


        soup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Soup");
                startActivity(intent);
            }
        });


        coffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Coffee");
                startActivity(intent);
            }
        });


        dessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Dessert");
                startActivity(intent);
            }
        });
    }
}
