package com.example.budgettracker.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.budgettracker.MainActivity;
import com.example.budgettracker.R;
import com.example.budgettracker.data.MyDbAdapter;
import com.example.budgettracker.ui.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpRedirectText;
    private MyDbAdapter myDbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signUpRedirectText = findViewById(R.id.signup_redirect);

        myDbAdapter = new MyDbAdapter(this); // Initialize MyDbAdapter

        loginButton.setOnClickListener(this::onLoginClicked);
        signUpRedirectText.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
    }

    private void onLoginClicked(View v) {
        String enteredUsername = usernameEditText.getText().toString().trim();
        String enteredPassword = passwordEditText.getText().toString().trim();

        // Validate input
        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            showToast("Please enter both username and password");
            return;
        }

        // Check credentials using MyDbAdapter
        if (myDbAdapter.checkUserCredentials(enteredUsername, enteredPassword)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            showToast("Invalid username or password");
        }
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
