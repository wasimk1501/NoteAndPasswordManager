package com.example.notepasswordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createpassword extends AppCompatActivity {
    EditText mtitleofpassword, midofpassword, myourpassword;
    Button mbtnsave;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    ProgressBar mprogressbarofaddpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpassword);

        mbtnsave = findViewById(R.id.btnSave);
        mtitleofpassword = findViewById(R.id.titleofpassword);
        midofpassword = findViewById(R.id.idofpassword);
        myourpassword = findViewById(R.id.yourpassword);

        mprogressbarofaddpassword = findViewById(R.id.progressbarofaddpassword);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionBarColor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Back");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        mbtnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ptitle = mtitleofpassword.getText().toString();
                String pid = midofpassword.getText().toString();
                String ppassword = myourpassword.getText().toString();
                if (ptitle.isEmpty() || pid.isEmpty() || ppassword.isEmpty()) {
                    Toast.makeText(createpassword.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    mprogressbarofaddpassword.setVisibility(View.VISIBLE);
                    DocumentReference documentReference = firebaseFirestore.collection("password")
                            .document(firebaseUser.getUid()).collection("mypassword").document();
                    Map<String, Object> pass = new HashMap<>();
                    pass.put("ptitle", ptitle);
                    pass.put("pid", pid);
                    pass.put("ppassword", ppassword);

                    documentReference.set(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(createpassword.this, "Password Added Successfully", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(createpassword.this, passwordactivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(createpassword.this, "Failed to Add Password", Toast.LENGTH_SHORT).show();
                            mprogressbarofaddpassword.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(createpassword.this, passwordactivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(createpassword.this, passwordactivity.class));
    }
}