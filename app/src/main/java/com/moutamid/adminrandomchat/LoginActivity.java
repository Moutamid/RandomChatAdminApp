package com.moutamid.adminrandomchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.moutamid.adminrandomchat.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding b;
    private String myemail = "admin123@gmail.com";
    private String mypassword = "admin123";
    private SharedPreferencesManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        manager = new SharedPreferencesManager(LoginActivity.this);
        b.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = b.edEmail.getText().toString();
                String password = b.edPassword.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()){
                    if (email.equals(myemail) && password.equals(mypassword)){
                        manager.storeBoolean("login",true);
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean login = manager.retrieveBoolean("login",false);
        if(login){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}