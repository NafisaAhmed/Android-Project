package com.example.user.onyaproject;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.user.onyaproject.Model.FoodItems;
import com.example.user.onyaproject.Prevalent.Prevalent;
import com.example.user.onyaproject.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
     DrawerLayout drawerLayout;
     ActionBarDrawerToggle toogle;
    ActionBar actionBar;
    private DatabaseReference FoodsRef;
    private FirebaseAuth firebaseAuth;
    private RecyclerView   recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home);
        FoodsRef= FirebaseDatabase.getInstance().getReference().child( "Foods");
        Paper.init(this);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable( Color.parseColor("#D81B60") ) );
        getSupportActionBar().setTitle( " " );
        drawerLayout=findViewById(R.id.drawerId );

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationId);
        navigationView.setNavigationItemSelectedListener( this );
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.txtFullName);
        CircleImageView profileImageView = headerView.findViewById(R.id.profile_image);
        userNameTextView.setText( Prevalent.currentOnlineUser.getName() );
       Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.user1).into(profileImageView);


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        toogle=new ActionBarDrawerToggle( this,drawerLayout,R.string.nav_open,R.string.nav_close );
        drawerLayout.addDrawerListener( toogle);
        toogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<FoodItems> options =
                new FirebaseRecyclerOptions.Builder<FoodItems>()
                        .setQuery(FoodsRef, FoodItems.class)
                        .build();


        FirebaseRecyclerAdapter<FoodItems, FoodViewHolder> adapter =
                new FirebaseRecyclerAdapter<FoodItems, FoodViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull final FoodItems model) {
                         holder.txtFoodDescription.setText( model.getDescription() );
                        holder.txtFoodName.setText( model.getPname() );
                        holder.txtFoodPrice.setText("Price= "+model.getPrice()+"Tk" );
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.itemView.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(HomeActivity.this,FoodDetailsActivity.class );
                                intent.putExtra("pid",model.getPid() );
                                startActivity( intent );
                            }
                        } );
                    }

                    @NonNull
                    @Override
                    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_items_layout, parent, false);
                        FoodViewHolder holder = new FoodViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toogle.onOptionsItemSelected( item))
        {
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId()==R.id.nav_menu)
        {

        }
       else if(menuItem.getItemId()==R.id.nav_cart)
        {

        }
       else if(menuItem.getItemId()==R.id.nav_orders)
        {

        }
        else if(menuItem.getItemId()==R.id.nav_settings)
        {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);

        }
       else if(menuItem.getItemId()==R.id.nav_log_out)
        {
            Paper.book().destroy();
            Intent intent=new Intent(HomeActivity.this,MainActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity( intent );
            finish();
        }
        return false;
    }
}
