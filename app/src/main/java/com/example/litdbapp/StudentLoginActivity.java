package com.example.litdbapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentLoginActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        dbHelper = new DatabaseHelper(this);

        EditText rollNo = findViewById(R.id.roll_no);
        EditText academicYear = findViewById(R.id.academic_year);
        EditText password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateStudent(rollNo.getText().toString(), academicYear.getText().toString(), password.getText().toString());
            }
        });
    }

    private void validateStudent(String rollNo, String academicYear, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_STUDENT, null,
                DatabaseHelper.COL_ROLL_NO + " = ? AND " + DatabaseHelper.COL_ACADEMIC_YEAR + " = ? AND " + DatabaseHelper.COL_PASSWORD + " = ?",
                new String[]{rollNo, academicYear, password}, null, null, null);

        if (cursor.moveToFirst()) {
            Intent intent = new Intent(StudentLoginActivity.this, StudentHomePageActivity.class);
            intent.putExtra("rollNo", rollNo);
            startActivity(intent);
        } else {
            Toast.makeText(StudentLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }
}
