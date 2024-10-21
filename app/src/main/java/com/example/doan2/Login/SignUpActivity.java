package com.example.doan2.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class SignUpActivity extends AppCompatActivity {

    EditText username_txt, password_txt, customer_phone_number_txt;
    Button sign_up_btn;
    ImageButton Logo;

    public interface RequestAccount {
        @POST("signup")
        Call<AccountResponse> createAccount(@Body Account account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Logo = findViewById(R.id.Logo);
        username_txt = findViewById(R.id.username_txt);
        password_txt = findViewById(R.id.password_txt);
        customer_phone_number_txt = findViewById(R.id.customer_phone_number_txt);
        sign_up_btn = findViewById(R.id.sign_up_btn);

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://prod-server-kh18.onrender.com/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestAccount requestAccount = retrofit.create(RequestAccount.class);

        Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        sign_up_btn.setOnClickListener(view -> {
            String username = username_txt.getText().toString();
            String password = password_txt.getText().toString();
            String phoneNumber = customer_phone_number_txt.getText().toString();

            if (username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Account account = new Account(username, password, phoneNumber);

            requestAccount.createAccount(account).enqueue(new Callback<AccountResponse>() {
                @Override
                public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                    if (response.isSuccessful()) {
                        AccountResponse accountResponse = response.body();

                        if (accountResponse != null) {
                            Toast.makeText(SignUpActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Signup failed: Invalid response", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("API_Response", "Error response body: " + response.errorBody().toString());
                        Toast.makeText(SignUpActivity.this, "Signup failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AccountResponse> call, Throwable t) {
                    Log.e("API_Error", "Signup request failed", t);
                    Toast.makeText(SignUpActivity.this, "Signup failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
