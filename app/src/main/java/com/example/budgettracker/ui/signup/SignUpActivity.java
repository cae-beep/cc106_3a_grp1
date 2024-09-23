package com.example.budgettracker.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.budgettracker.R;
import com.example.budgettracker.data.MyDbAdapter; // Import your database adapter
import com.example.budgettracker.ui.login.LoginActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText;
    private Button signUpButton;
    private TextView loginRedirectText;
    private MyDbAdapter dbAdapter; // Database adapter instance


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize the database adapter
        dbAdapter = new MyDbAdapter(this);



        // Bind views gbrbgege
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        signUpButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signUpButton.setOnClickListener(this::onSignUpClicked);
        loginRedirectText.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
    }

    private void onSignUpClicked(View v) {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Please enter all details");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return;
        }

        // Check if the username already exists
        if (dbAdapter.checkUserExists(username)) {
            showToast("Username already exists");
            return;
        }

        // Save user credentials in SQLite
        long result = dbAdapter.insertUser(username, password);
        if (result != -1) {
            showToast("Registration Successful");
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        } else {
            showToast("Registration Failed");
        }
    }



    private void showToast(String message) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
