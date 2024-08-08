package com.example.litdbapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentHomePageActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String rollNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        dbHelper = new DatabaseHelper(this);
        rollNo = getIntent().getStringExtra("rollNo");

        // Initialize UI components
        EditText studentName = findViewById(R.id.student_name);
        EditText parentName = findViewById(R.id.parent_name);
        EditText contactNumber = findViewById(R.id.contact_number);
        EditText email = findViewById(R.id.email);
        EditText gender = findViewById(R.id.gender);
        EditText departmentName = findViewById(R.id.department_name);
        EditText academicYear = findViewById(R.id.academic_year);
        EditText password = findViewById(R.id.password);
        Button updateButton = findViewById(R.id.update_button);

        // Load student data
        loadStudentData(studentName, parentName, contactNumber, email, gender, departmentName, academicYear, password);

        // Update button click listener
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent(studentName.getText().toString(),
                        parentName.getText().toString(),
                        contactNumber.getText().toString(),
                        email.getText().toString(),
                        gender.getText().toString(),
                        departmentName.getText().toString(),
                        academicYear.getText().toString(),
                        password.getText().toString());
            }
        });
    }

    private void loadStudentData(EditText studentName, EditText parentName, EditText contactNumber, EditText email, EditText gender, EditText departmentName, EditText academicYear, EditText password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_STUDENT, null,
                DatabaseHelper.COL_ROLL_NO + " = ?", new String[]{rollNo}, null, null, null);

        if (cursor.moveToFirst()) {
            studentName.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_STUDENT_NAME)));
            parentName.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PARENT_NAME)));
            contactNumber.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_CONTACT_NUMBER)));
            email.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EMAIL)));
            gender.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_GENDER)));
            departmentName.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DEPARTMENT_NAME)));
            academicYear.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_ACADEMIC_YEAR)));
            password.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PASSWORD)));
        }

        cursor.close();
    }

    private void updateStudent(String studentName, String parentName, String contactNumber, String email, String gender, String departmentName, String academicYear, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_STUDENT_NAME, studentName);
        values.put(DatabaseHelper.COL_PARENT_NAME, parentName);
        values.put(DatabaseHelper.COL_CONTACT_NUMBER, contactNumber);
        values.put(DatabaseHelper.COL_EMAIL, email);
        values.put(DatabaseHelper.COL_GENDER, gender);
        values.put(DatabaseHelper.COL_DEPARTMENT_NAME, departmentName);
        values.put(DatabaseHelper.COL_ACADEMIC_YEAR, academicYear);
        values.put(DatabaseHelper.COL_PASSWORD, password);

        int rowsAffected = db.update(DatabaseHelper.TABLE_STUDENT, values,
                DatabaseHelper.COL_ROLL_NO + " = ?", new String[]{rollNo});

        if (rowsAffected > 0) {
            Toast.makeText(this, "Student details updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error updating details", Toast.LENGTH_SHORT).show();
        }
    }
}
