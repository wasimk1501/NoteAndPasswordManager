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
import android.widget.TextView;

public class passworddetails extends AppCompatActivity {
    private TextView mtitleofpassworddetail, midofpassworddetail, myourpassworddetail;
    Button mbtnedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passworddetails);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mtitleofpassworddetail = findViewById(R.id.titleofpassworddetail);
        midofpassworddetail = findViewById(R.id.idofpassworddetail);
        myourpassworddetail = findViewById(R.id.yourpassworddetail);
        mbtnedit = findViewById(R.id.btnEdit);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.actionBarColor)));
        Intent pdata = getIntent();

        mbtnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), editpasswordactivity.class);
                intent.putExtra("ptitle", pdata.getStringExtra("ptitle"));
                intent.putExtra("pid", pdata.getStringExtra("pid"));
                intent.putExtra("ppassword", pdata.getStringExtra("ppassword"));
                intent.putExtra("passId", pdata.getStringExtra("passId"));
                view.getContext().startActivity(intent);

            }
        });

        mtitleofpassworddetail.setText(pdata.getStringExtra("ptitle"));
        midofpassworddetail.setText(pdata.getStringExtra("pid"));
        myourpassworddetail.setText(pdata.getStringExtra("ppassword"));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            startActivity(new Intent(passworddetails.this, passwordactivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(passworddetails.this, passwordactivity.class));
        finish();
    }
}