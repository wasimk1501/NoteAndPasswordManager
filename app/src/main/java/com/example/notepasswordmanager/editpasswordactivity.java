package com.example.notepasswordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editpasswordactivity extends AppCompatActivity {
    Intent data;
    private EditText mtitleofeditpassword, midofeditpassword, myoureditpassword;
    private Button mbtnApplyChanges;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpasswordactivity);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mtitleofeditpassword = findViewById(R.id.titleofeditpassword);
        midofeditpassword = findViewById(R.id.idofeditpassword);
        myoureditpassword = findViewById(R.id.youreditpassword);
        mbtnApplyChanges = findViewById(R.id.btnApplyChanges);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.actionBarColor)));

        data = getIntent();

        mbtnApplyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newptitle = mtitleofeditpassword.getText().toString();
                String newpid = midofeditpassword.getText().toString();
                String newppassword = myoureditpassword.getText().toString();
                if (newptitle.isEmpty() || newpid.isEmpty() || newppassword.isEmpty()) {
                    Toast.makeText(editpasswordactivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    DocumentReference documentReference = firebaseFirestore.collection("password")
                            .document(firebaseUser.getUid()).collection("mypassword")
                            .document(data.getStringExtra("passId"));
                    Map<String, Object> pass = new HashMap<>();
                    pass.put("ptitle", newptitle);
                    pass.put("pid", newpid);
                    pass.put("ppassword", newppassword);
                    documentReference.set(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(editpasswordactivity.this, "Password is updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(editpasswordactivity.this, passwordactivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editpasswordactivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        String ptitle = data.getStringExtra("ptitle");
        String pid = data.getStringExtra("pid");
        String ppassword = data.getStringExtra("ppassword");
        mtitleofeditpassword.setText(ptitle);
        midofeditpassword.setText(pid);
        myoureditpassword.setText(ppassword);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(editpasswordactivity.this, passwordactivity.class));
        finish();
    }
}