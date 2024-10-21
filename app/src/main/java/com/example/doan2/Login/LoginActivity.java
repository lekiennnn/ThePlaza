package com.example.doan2.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan2.Product.MainActivity;
import com.example.doan2.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity {

    EditText username_txt, password_txt;
    Button login_btn;
    TextView signup_txt;

    // Retrofit
    public interface RequestAccount {
        @POST("login")
        Call<AccountResponse> checkAccount(@Body Account account);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username_txt = findViewById(R.id.username_txt);
        password_txt = findViewById(R.id.password_txt);
        login_btn = findViewById(R.id.login_btn);
        signup_txt = findViewById(R.id.sign_up_txt);

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://prod-server-kh18.onrender.com/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestAccount requestAccount = retrofit.create(RequestAccount.class);

        // Login button
        login_btn.setOnClickListener(view -> {
            String username = username_txt.getText().toString();
            String password = password_txt.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Account account = new Account(username, password);

            requestAccount.checkAccount(account).enqueue(new Callback<AccountResponse>() {
                @Override
                public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                    Log.d("API_Response", "Response code: " + response.code());

                    if (response.isSuccessful()) {
                        AccountResponse accountResponse = response.body();
                        if (accountResponse != null) {
                            Account user = accountResponse.getUser();
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed: Invalid response", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed: Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AccountResponse> call, Throwable t) {
                    Log.e("API_Error", "Login request failed", t);
                    Toast.makeText(LoginActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Sign up
        signup_txt.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
