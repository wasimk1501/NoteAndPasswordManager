package com.example.notepasswordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    TextView tvAlreadyHaveAccount;
    private EditText inputEmail, inputUsername, inputPassword;
    Button btnRegister;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    private String username, pass, email;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        tvAlreadyHaveAccount = findViewById(R.id.tvAlreadyHaveAccount);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnRegister = findViewById(R.id.btnRegister);
        inputUsername = findViewById(R.id.inputUsername);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance()
                .getReference("credentials");

        tvAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = inputEmail.getText().toString();
                username = inputUsername.getText().toString().toLowerCase();
                pass = inputPassword.getText().toString();
                if (email.isEmpty() || username.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                } else if (!email.matches("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$")) {
                    inputEmail.setError("Enter correct Email");
                    inputEmail.requestFocus();

                } else if (username.length() < 7) {
                    inputUsername.setError("Username should be greater than 6 character or digit");
                    inputUsername.requestFocus();
                } else if (!username.matches("^[A-Za-z]\\w{5,29}$")) {
                    inputUsername.setError("Username Validation :\n1. It should starts with a character.\n2. It should not contain any blank spaces.");
                    inputUsername.requestFocus();
                } else if (pass.length() < 7) {
                    inputPassword.setError("Password should be greater than 6 digit or character");
                    inputPassword.requestFocus();
                } else {
                    progressDialog.setMessage("Please wait while Registration..");
                    progressDialog.setTitle("Registration");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Login d = new Login(mAuth.getCurrentUser().getUid(), email, username, pass);
                                    reference.child(username).setValue(d)
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    mAuth.signOut();
                                                    Toast.makeText(RegisterActivity.this, "Successfully Registered 👍\nLogin to Continue", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                                                }
                                            });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }
}