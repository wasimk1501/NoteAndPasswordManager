package com.example.notepasswordmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;

public class generatepassword extends AppCompatActivity {
    int len;
    TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generatepassword);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        textView = findViewById(R.id.txtpassword);
        button = findViewById(R.id.copy);

        Bundle bundle = getIntent().getExtras();
        len = bundle.getInt("val");
        String pass = Generate(len);
        textView.setText(pass);

        getSupportActionBar().hide();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Edittext", textView.getText().toString());
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(generatepassword.this, "Text copied to Clipboard", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private String Generate(int len) {
        String allchar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890$@#%!";
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(allchar.length());
            sb.append(allchar.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(generatepassword.this, choosepasslength.class));
    }
}