package com.example.doan2.Login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.doan2.Login.LoginActivity;
import com.example.doan2.Product.MainActivity;

public class StartActivity extends AppCompatActivity {

    private static final String sf_name = "my_sf";
    private static final String sf_isLogin = "isLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sf = getSharedPreferences(sf_name, MODE_PRIVATE);

        boolean isLogin = sf.getBoolean(sf_isLogin, false);  // default is false if not set

        if (isLogin) {

            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        } else {

            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }
}
