package com.example.firebaseauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterPage extends AppCompatActivity {
   private EditText edtName,edtEmail,edtPassword;
   private Button register;
   private TextView txtlogin;
   private String name,email,pass;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        edtName=findViewById(R.id.edtName);
        edtEmail=findViewById(R.id.edtEmail);
        edtPassword=findViewById(R.id.edtPassword);
        register=findViewById(R.id.register);

        txtlogin=findViewById(R.id.txtlogin);
        auth=FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUser();
            }
        });
        Intent intent =new Intent(RegisterPage.this,MainActivity.class);

        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                finish();
            }
        });

    }

    //Check User is Exist or Not


    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(this,DashBoard.class));
            finish();
        }
    }

    private void validateUser() {

        name=edtName.getText().toString();
        email=edtEmail.getText().toString();
        pass=edtPassword.getText().toString();

        if (name.isEmpty() ||email.isEmpty() ||pass.isEmpty()){
            Toast.makeText(this, "Fill the field", Toast.LENGTH_SHORT).show();
        }
        else{
            registerUser();
        }
    }

    private void registerUser() {
        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(RegisterPage.this, "User Created", Toast.LENGTH_SHORT).show();
                            updateUser();
                         //   finish();
                        }
                        else {
                            Toast.makeText(RegisterPage.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void updateUser() {
        UserProfileChangeRequest changeRequest=new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        auth.getCurrentUser().updateProfile(changeRequest);
        auth.signOut();
        openLogin();
    }

    private void openLogin() {
        startActivity(new Intent(RegisterPage.this,MainActivity.class));
        finish();
    }
}