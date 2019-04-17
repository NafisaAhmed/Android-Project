package com.example.user.onyaproject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;


import com.example.user.onyaproject.Model.Users;
import com.example.user.onyaproject.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText signInPhoneEditText,signInPasswordEditText;
    private TextView signUpTextView;
    private Button signInButton;
    private TextView forgotPassword;
    private TextView AdminLink,NotAdminLink;
    private ProgressDialog loadingBar;


    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private String parentDbName ="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        signInPhoneEditText=(EditText)findViewById(R.id.signInPhoneEditTextId);
        signInPasswordEditText=(EditText) findViewById(R.id.signInPasswordEditTextId);
        signInButton=(Button)findViewById(R.id.signInButtonId);
        signUpTextView=(TextView)findViewById(R.id.signUpTextViewId);
        forgotPassword=(TextView) findViewById( R.id.forgotPasswordTextViewId );
        loadingBar = new ProgressDialog(this);
        AdminLink=(TextView)findViewById( R.id.adminTextViewId );
        NotAdminLink=(TextView)findViewById( R.id.notadminTextViewId );


        progressBar=findViewById(R.id.progressBarId);

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                signInButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                signInButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });


        signUpTextView.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        forgotPassword.setOnClickListener( this );



    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.signInButtonId:
                userLogin();
                break;
            case R.id.signUpTextViewId:
                Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
                break;

            case R.id.forgotPasswordTextViewId:

                startActivity( new Intent(MainActivity.this,PasswordActivity.class));
                break;


        }

    }

    private void userLogin() {
        String  phone= signInPhoneEditText.getText().toString().trim();
        String password = signInPasswordEditText.getText().toString().trim();
         if (phone.isEmpty()) {
            signInPhoneEditText.setError( "Enter a phone number" );
             signInPhoneEditText.requestFocus();
            return;
        } else if (!Patterns.PHONE.matcher( phone ).matches()) {
             signInPhoneEditText.setError( "Enter a valid phone number" );
             signInPhoneEditText.requestFocus();
            return;
        }
         else if (password.isEmpty()) {
            signInPasswordEditText.setError( "Enter a password" );
            signInPasswordEditText.requestFocus();
            return;
        } else if (password.length() < 6) {
            signInPasswordEditText.setError( "Minimum length of a password should be 6" );
            signInPasswordEditText.requestFocus();
            return;
        } else {
            loadingBar.setTitle( "Login Account" );
            loadingBar.setMessage( "Please wait, while we are checking the credentials." );
            loadingBar.setCanceledOnTouchOutside( false );
            loadingBar.show();


            AllowAccessToAccount(phone, password);
        }
    }




    private void AllowAccessToAccount(final String phone, final String password)
    {



        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(MainActivity.this, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(MainActivity.this,AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(MainActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    }











