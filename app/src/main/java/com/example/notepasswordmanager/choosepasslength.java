package com.example.notepasswordmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class choosepasslength extends AppCompatActivity {
    private TextView textView;
    SeekBar seekBar;
    Button button;
    int pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosepasslength);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        textView = findViewById(R.id.position);
        seekBar = findViewById(R.id.seekbar);
        seekBar.setMax(25);
        button = findViewById(R.id.generate);

        getSupportActionBar().hide();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pos = i;
                textView.setText("" + pos);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos > 5 && pos < 21) {
                    Intent intent = new Intent(choosepasslength.this, generatepassword.class);
                    intent.putExtra("val", pos);
                    startActivity(intent);
                } else {
                    textView.setText("Password length should be between 6 to 20");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(choosepasslength.this, passwordactivity.class));
    }
}