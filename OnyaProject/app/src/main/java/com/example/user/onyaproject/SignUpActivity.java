package com.example.user.onyaproject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.*;

import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText signUpEmailEditText,signUpPasswordEditText,signUpNameEditText,signUpPhoneEditText;
    private TextView signInTextView;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private ImageView userProfilePic;
    String name,email,phone,password;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign Up Activity");

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBarId);
        signUpNameEditText=findViewById(R.id.signUpNameEditTextId);
        signUpPhoneEditText=findViewById(R.id.signUpPhoneEditTextId);
        signUpEmailEditText=findViewById(R.id.signUpEmailEditTextId);
        signUpPasswordEditText=findViewById(R.id.signUpPasswordEditTextId);
        signUpButton=findViewById(R.id.signUpButtonId);
        signInTextView=findViewById(R.id.signInTextViewId);
        userProfilePic=(ImageView)findViewById( R.id.ivProfileId);
        loadingBar = new ProgressDialog(this);


        signInTextView.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.signUpButtonId:
                userRegister();
                break;
            case R.id.signInTextViewId:
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;

        }

    }

    private void userRegister() {
        name = signUpNameEditText.getText().toString().trim();
        email = signUpEmailEditText.getText().toString().trim();
        password = signUpPasswordEditText.getText().toString().trim();
        phone = signUpPhoneEditText.getText().toString().trim();
        if (name.isEmpty()) {
            signUpNameEditText.setError( "Enter a name" );
            signUpNameEditText.requestFocus();
            return;
        } else if (email.isEmpty()) {
            signUpEmailEditText.setError( "Enter an email address" );
            signUpEmailEditText.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher( email ).matches()) {
            signUpEmailEditText.setError( "Enter a valid email address" );
            signUpEmailEditText.requestFocus();
            return;
        } else if (password.isEmpty()) {
            signUpPasswordEditText.setError( "Enter a password" );
            signUpPasswordEditText.requestFocus();
            return;
        } else if (password.length() < 6) {
            signUpPasswordEditText.setError( "Minimum length of a password should be 6" );
            signUpPasswordEditText.requestFocus();
            return;
        } else if (phone.isEmpty()) {
            signUpPhoneEditText.setError( "Enter a phone number" );
            signUpPhoneEditText.requestFocus();
            return;
        } else if (!Patterns.PHONE.matcher( phone ).matches()) {
            signUpPhoneEditText.setError( "Enter a valid phone number" );
            signUpPhoneEditText.requestFocus();
            return;
        } else {
            loadingBar.setTitle( "Create Account" );
            loadingBar.setMessage( "Please wait, while we are checking the credentials." );
            loadingBar.setCanceledOnTouchOutside( false );
            loadingBar.show();
            ValidatephoneNumber( name, phone, password );
        }
    }


        private void ValidatephoneNumber(final String name,final String phone,final String password){
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password",  password);
                    userdataMap.put("name", name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(SignUpActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();

                                        loadingBar.dismiss();
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(SignUpActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();

                    Toast.makeText(SignUpActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}



