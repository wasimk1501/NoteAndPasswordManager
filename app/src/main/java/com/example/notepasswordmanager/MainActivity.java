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
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity {
    private TextView tvCreateNewAccount;
    EditText loginUsername, loginPassword;
    private String name, pass;
    Button btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    DatabaseReference reference;
    private TextView tvForgetPassword;
//    ProgressBar mprogressbarofmainactivity;
    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    //    so create shared preference name and also create key name
    private static final String SHARED_PREF_NAME="mypref";
    private static final String KEY_NAME="name";
    private static final String KEY_PASSWORD="pass";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        tvCreateNewAccount = findViewById(R.id.tvCreateNewAccount);
        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
//        mprogressbarofmainactivity = findViewById(R.id.progressbarofmainactivity);
        progressDialog =new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance()
                .getReference("credentials");

        if(firebaseUser!=null){
            finish();
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
        }

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgetPassword.class));
                finish();
            }
        });

        tvCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = loginUsername.getText().toString().toLowerCase();
                pass = loginPassword.getText().toString();

                reference.child(name)
                        .addListenerForSingleValueEvent(listener);
            }
        });



    }

    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (snapshot.exists()) {
                String password = snapshot.child("pass")
                        .getValue(String.class);
                String email = snapshot.child("email")
                        .getValue(String.class);
//                String username=snapshot.child("username")
//                        .getValue(String.class);
//                String uid=snapshot.child("uid")
//                        .getValue(String.class);
                if(name.isEmpty()){
                    loginUsername.setError("Enter your username");
                    loginUsername.requestFocus();
                }
                else if (pass.isEmpty()){
                    loginPassword.setError("Enter your password");
                    loginPassword.requestFocus();
                }
                else if (password.equals(pass)) {
                    progressDialog.setMessage("Please wait while Login...");
                    progressDialog.setTitle("Logging In");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    progressDialog.dismiss();
                                    //used shared preference to fetch username and password
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putString(KEY_NAME,loginUsername.getText().toString());
                                    editor.putString(KEY_PASSWORD,loginPassword.getText().toString());
                                    editor.apply();

//                                    mprogressbarofmainactivity.setVisibility(View.VISIBLE);

                                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
//                                    i.putExtra("username",username);
//                                    i.putExtra("uid",uid);
//                                    i.putExtra("email",email);
//                                    i.putExtra("pass",password);
                                    finish();
                                    startActivity(i);
                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
//                                    mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Database Error"+e.toString(), Toast.LENGTH_SHORT).show();

                                }
                            });

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Record Not Found", Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Database Error\n"+error.toString(), Toast.LENGTH_SHORT).show();

        }
    };

}