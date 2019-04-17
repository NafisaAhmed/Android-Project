package com.example.user.onyaproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.*;

import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {
    private EditText passwordEmail;
    private Button resetPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getSupportActionBar().hide();
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_password );

        passwordEmail=(EditText)findViewById( R.id.passwordEmailId );
        resetPassword=(Button)findViewById(R.id.resetPasswordButtonId );
        firebaseAuth=FirebaseAuth.getInstance();
        resetPassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail=passwordEmail.getText().toString().trim();
                if(userEmail.isEmpty())
                {
                    passwordEmail.setError("Enter an email address");
                    passwordEmail.requestFocus();
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    passwordEmail.setError("Enter a valid email address");
                    passwordEmail.requestFocus();
                    return;
                }
                else{
                    firebaseAuth.sendPasswordResetEmail( userEmail).addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText( PasswordActivity .this,"Password reset email sent",Toast.LENGTH_SHORT ).show();
                                finish();
                                startActivity( new Intent( PasswordActivity .this,MainActivity .class ) );
                            }
                            else
                            {
                                Toast.makeText(PasswordActivity .this, "Error in sending pasword reset email", Toast.LENGTH_SHORT).show();

                            }

                        }
                    } );
                }

            }
        } );

    }
}
