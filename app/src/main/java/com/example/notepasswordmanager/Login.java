package com.example.notepasswordmanager;

import android.widget.EditText;

public class Login {
    public String username, pass, email;
    public String uid;

    public Login(String uid, String email, String username, String pass) {
        this.uid = uid;
        this.username = username;
        this.pass = pass;
        this.email = email;
    }
}
