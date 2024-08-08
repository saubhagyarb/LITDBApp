package com.example.litdbapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddStudentActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        dbHelper = new DatabaseHelper(this);

        EditText rollNo = findViewById(R.id.roll_no);
        EditText studentName = findViewById(R.id.student_name);
        EditText academicYear = findViewById(R.id.academic_year);
        EditText departmentName = findViewById(R.id.department_name);
        EditText password = findViewById(R.id.password);
        Button addStudentButton = findViewById(R.id.add_student_button);

        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent(rollNo.getText().toString(), studentName.getText().toString(), academicYear.getText().toString(), departmentName.getText().toString(), password.getText().toString());
            }
        });
    }

    private void addStudent(String rollNo, String studentName, String academicYear, String departmentName, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ROLL_NO, Integer.parseInt(rollNo));
        values.put(DatabaseHelper.COL_STUDENT_NAME, studentName);
        values.put(DatabaseHelper.COL_ACADEMIC_YEAR, academicYear);
        values.put(DatabaseHelper.COL_DEPARTMENT_NAME, departmentName);
        values.put(DatabaseHelper.COL_PASSWORD, password);

        long newRowId = db.insert(DatabaseHelper.TABLE_STUDENT, null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error adding student", Toast.LENGTH_SHORT).show();
        }
    }
}
