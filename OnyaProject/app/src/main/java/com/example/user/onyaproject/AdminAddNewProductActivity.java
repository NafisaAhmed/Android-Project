package com.example.user.onyaproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewItemButton;
    private ImageView InputFoodImage;
    private EditText InputFoodName, InputFoodDescription, InputFoodPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String foodRandomKey,downloadImageUrl;
    private StorageReference FoodImagesRef;
    private DatabaseReference FoodsRef;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState );
        setContentView(R.layout.activity_admin_add_new_product);
        getSupportActionBar().hide();
        CategoryName = getIntent().getExtras().get("category").toString();
        FoodImagesRef = FirebaseStorage.getInstance().getReference().child("Food Images");
        FoodsRef = FirebaseDatabase.getInstance().getReference().child("Foods");


        AddNewItemButton = (Button) findViewById(R.id.add_new_food);
        InputFoodImage = (ImageView) findViewById(R.id.select_food_image);
        InputFoodName = (EditText) findViewById(R.id.food_name);
        InputFoodDescription = (EditText) findViewById(R.id.food_description);
        InputFoodPrice = (EditText) findViewById(R.id.food_price);
        loadingBar = new ProgressDialog(this);


        InputFoodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });

        AddNewItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateItemData();
            }
        });

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            InputFoodImage.setImageURI(ImageUri);
        }
    }

    private void ValidateItemData()
    {
        Description = InputFoodDescription.getText().toString();
        Price = InputFoodPrice.getText().toString();
        Pname = InputFoodName.getText().toString();


        if (ImageUri == null)
        {
            Toast.makeText(this, "Food image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write food description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write food Price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write food name...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreItemInformation();
        }
    }

    private void StoreItemInformation() {
        loadingBar.setTitle("Add New Item");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new item.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        foodRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = FoodImagesRef.child(ImageUri.getLastPathSegment() + foodRandomKey + ".png");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Food Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                } ).addOnCompleteListener( new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewProductActivity.this, "got the Food image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveFoodInfoToDatabase();
                        }
                    }
                } );
            }
        } );
    }

    private void SaveFoodInfoToDatabase() {
        HashMap<String, Object> foodMap = new HashMap<>();
        foodMap.put("pid", foodRandomKey);
        foodMap.put("date", saveCurrentDate);
        foodMap.put("time", saveCurrentTime);
        foodMap.put("description", Description);
        foodMap.put("image", downloadImageUrl);
        foodMap.put("category", CategoryName);
        foodMap.put("price", Price);
        foodMap.put("pname", Pname);
       FoodsRef.child(foodRandomKey).updateChildren(foodMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Food is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}
