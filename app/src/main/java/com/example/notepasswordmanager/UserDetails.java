package com.example.notepasswordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetails extends AppCompatActivity {
    private TextView showEmail, showUsername, showPassword, showUid;
    private Button btnLogout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        showEmail = findViewById(R.id.showEmail);
        showUsername = findViewById(R.id.showUsername);
        showPassword = findViewById(R.id.showPassword);
        showUid = findViewById(R.id.showUid);
        btnLogout = findViewById(R.id.btnLogout);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.actionBarColor)));
        getSupportActionBar().setTitle("User Profile");

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(UserDetails.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_NAME, null);
        String email = firebaseUser.getEmail().toString();
        String password = sharedPreferences.getString(KEY_PASSWORD, null);
        ;
        String uid = firebaseUser.getUid().toString();

        if (username != null) {
            showEmail.setText("Your Email :\n" + email);
            showPassword.setText("Your Password :\n" + password);
            showUid.setText("Your Unique UserID :\n" + uid);
            showUsername.setText("Your Username :\n" + username);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(UserDetails.this, HomeActivity.class));
    }
}
