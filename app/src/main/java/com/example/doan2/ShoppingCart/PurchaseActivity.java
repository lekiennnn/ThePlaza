package com.example.doan2.ShoppingCart;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan2.Product.MainActivity;
import com.example.doan2.R;

public class PurchaseActivity extends AppCompatActivity {

    TextView customer_name_txt, customer_phone_number_txt, total_cost_txt;
    CheckBox cod_cb, atm_cb;
    EditText customer_address_txt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        customer_name_txt = findViewById(R.id.customer_name_txt);
        customer_address_txt = findViewById(R.id.customer_address_txt);
        customer_phone_number_txt = findViewById(R.id.customer_phone_number_txt);
        cod_cb = findViewById(R.id.cod_cb);
        atm_cb = findViewById(R.id.atm_cb);
        total_cost_txt = findViewById(R.id.total_cost_txt);

        // Get the total cost from previous intent
        Intent intent = getIntent();
        double totalCost = intent.getDoubleExtra("TOTAL_COST", 0);
        total_cost_txt.setText("Total cost: $" + totalCost);

        // Confirm
        Button confirmButton = findViewById(R.id.Confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(customer_address_txt)) {
                    Toast.makeText(PurchaseActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!cod_cb.isChecked() && !atm_cb.isChecked()) {
                    Toast.makeText(PurchaseActivity.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                } else {
                    showPopupNotification();
                }
            }
        });

        // cash on delivery check-box
        cod_cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                atm_cb.setChecked(false);
            }
        });

        // atm check-box
        atm_cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cod_cb.setChecked(false);
            }
        });
    }

    // Check if edittext is empty
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    // Notification
    private void showPopupNotification() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Your purchase is completed.")
                .setPositiveButton("OK", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent intent = new Intent(PurchaseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.show();
    }
}
