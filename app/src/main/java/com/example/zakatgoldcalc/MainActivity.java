package com.example.zakatgoldcalc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    Toolbar myToolbar;
    TextView tvOutput1, tvOutput2, tvOutput3;
    TextInputLayout inputLayout1, inputLayout2;
    TextInputEditText etValue1, etValue2;
    RadioButton rbKeep, rbWear;
    RadioGroup radioGroup;
    Button btncalc, btnreset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        // Initialize all views
        tvOutput1 = findViewById(R.id.tvOutput1);
        tvOutput2 = findViewById(R.id.tvOutput2);
        tvOutput3 = findViewById(R.id.tvOutput3);
        inputLayout1 = findViewById(R.id.inputLayout1);
        inputLayout2 = findViewById(R.id.inputLayout2);
        etValue1 = findViewById(R.id.etValue1);
        etValue2 = findViewById(R.id.etValue2);
        btncalc = findViewById(R.id.btncalc);
        btnreset = findViewById(R.id.btnreset);
        rbKeep = findViewById(R.id.rbKeep);
        rbWear = findViewById(R.id.rbWear);
        radioGroup = findViewById(R.id.radioGroup);

        btncalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GOOD PRACTICE 2: IMPROVED ERROR MESSAGES
                if (!validateInputs()) {
                    return; // Stop if validation fails
                }

                // --- Calculation logic ---
                double weight = Double.parseDouble(etValue1.getText().toString());
                double price = Double.parseDouble(etValue2.getText().toString());
                double nisabThreshold;

                if (rbKeep.isChecked()) {
                    nisabThreshold = 85;   // Nisab for kept gold
                } else {
                    nisabThreshold = 200;  // Uruf for worn gold
                }

                double totalGoldValue = weight * price;
                double zakatableWeight = weight - nisabThreshold;
                if (zakatableWeight < 0) {
                    zakatableWeight = 0;
                }

                double zakatableValue = zakatableWeight * price;
                double zakatPayable = zakatableValue * 0.025;

                // Display results
                tvOutput1.setText("RM " + String.format("%.2f", totalGoldValue));
                tvOutput2.setText("RM " + String.format("%.2f", zakatableValue));
                tvOutput3.setText("RM " + String.format("%.2f", zakatPayable));

                // GOOD PRACTICE 3: HELPFUL NOTICE
                if (zakatPayable <= 0) {
                    Toast.makeText(MainActivity.this, "Zakat is not required as the gold weight is below the Nisab/Uruf threshold.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear inputs
                etValue1.setText("");
                etValue2.setText("");

                // Clear errors on the input fields
                inputLayout1.setError(null);
                inputLayout2.setError(null);

                // Reset result texts
                tvOutput1.setText("RM 0.00");
                tvOutput2.setText("RM 0.00");
                tvOutput3.setText("RM 0.00");

                // Clear radio button selection
                radioGroup.clearCheck();
                etValue1.requestFocus();

                // HELPFUL NOTICE
                Toast.makeText(MainActivity.this, "Fields have been reset.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs() {
        // Clear previous errors
        inputLayout1.setError(null);
        inputLayout2.setError(null);

        String weightStr = etValue1.getText().toString();
        String priceStr = etValue2.getText().toString();
        boolean isValid = true;

        if (TextUtils.isEmpty(weightStr)) {
            inputLayout1.setError("Gold weight is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(priceStr)) {
            inputLayout2.setError("Gold price is required");
            isValid = false;
        }

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select the type of gold", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    // No changes needed for your menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Calculate your gold zakat with this app: https://github.com/2023663494-lang/ZakatGoldCalculatorApps/tree/master");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Zakat Gold Calculator");
            startActivity(Intent.createChooser(shareIntent, "Share App"));
            return true;
        } else if (itemId == R.id.item_about) {
            Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
