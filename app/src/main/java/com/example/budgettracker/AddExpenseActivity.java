package com.example.budgettracker;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.budgettracker.data.MyDbAdapter;
import com.example.budgettracker.databinding.ActivityAddExpenseBinding;

import java.util.Calendar;
import java.util.UUID;

public class AddExpenseActivity extends AppCompatActivity {
    ActivityAddExpenseBinding binding;
    private String type;
    private ExpenseModel expenseModel;
    private MyDbAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbAdapter = new MyDbAdapter(this);

        type = getIntent().getStringExtra("type");
        expenseModel = (ExpenseModel) getIntent().getSerializableExtra("model");

        if (type == null) {
            type = expenseModel.getType();
            binding.amount.setText(String.valueOf(expenseModel.getAmount()));
            binding.category.setText(expenseModel.getCategory());
            binding.note.setText(expenseModel.getNote());
        }

        if (type.equals("Income")) {
            binding.incomeRadio.setChecked(true);
        } else {
            binding.expenseRadio.setChecked(true);
        }

        binding.incomeRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "Income";
            }
        });
        binding.expenseRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "Expense";
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if (expenseModel == null) {
            menuInflater.inflate(R.menu.buttom_menu, menu);
        } else {
            menuInflater.inflate(R.menu.buttom_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.saveExpense) {
            if (type != null) {
                createExpense();
            } else {
                updateExpense();
            }
            return true;
        }
        if (id == R.id.deleteExpense) {
            deleteExpense();
        }
        return false;
    }

    private void deleteExpense() {
        long l = dbAdapter.insertExpense(expenseModel.getExpenseId());
        finish();
    }

    private void createExpense() {
        String expenseId = UUID.randomUUID().toString();
        String amount = binding.amount.getText().toString();
        String note = binding.note.getText().toString();
        String category = binding.category.getText().toString();

        boolean incomeChecked = binding.incomeRadio.isChecked();
        if (incomeChecked) {
            type = "Income";
        } else {
            type = "Expense";
        }

        if (amount.trim().length() == 0) {
            binding.amount.setError("Empty");
            return;
        }
        ExpenseModel expenseModel = new ExpenseModel(expenseId, note, category, type, Long.parseLong(amount), Calendar.getInstance().getTimeInMillis(), "user_id");

        long id = dbAdapter.insertExpense(expenseModel);
        if (id > 0) {
            Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void updateExpense() {
        String expenseId = expenseModel.getExpenseId();
        String amount = binding.amount.getText().toString();
        String note = binding.note.getText().toString();
        String category = binding.category.getText().toString();

        boolean incomeChecked = binding.incomeRadio.isChecked();
        if (incomeChecked) {
            type = "Income";
        } else {
            type = "Expense";
        }

        if (amount.trim().length() == 0) {
            binding.amount.setError("Empty");
            return;
        }
        ExpenseModel model = new ExpenseModel(expenseId, note, category, type, Long.parseLong(amount), expenseModel.getTime(), "user_id");

        if (dbAdapter.updateExpense(model)) {
            Toast.makeText(this, "Expense updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update expense", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}