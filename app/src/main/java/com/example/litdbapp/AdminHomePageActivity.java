package com.example.litdbapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.IOException;

public class AdminHomePageActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int CREATE_FILE = 1;
    private static final String TAG = "AdminHomePageActivity";
    private Button btnAddStudent, btnDownloadStudentDetails, btnExit;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btnAddStudent = findViewById(R.id.add_new_student_button);
        btnDownloadStudentDetails = findViewById(R.id.download_student_details_button);
        btnExit = findViewById(R.id.exit_button);

        dbHelper = new DatabaseHelper(this);

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomePageActivity.this, AddStudentActivity.class));
            }
        });

        btnDownloadStudentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    createFile();
                } else {
                    checkPermissionAndWrite();
                }
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomePageActivity.this, AdminLoginActivity.class));
            }
        });
    }

    private void checkPermissionAndWrite() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            downloadStudentDetailsLegacy();
        }
    }

    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, "students.csv");
        startActivityForResult(intent, CREATE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                writeCsvToUri(uri);
            }
        }
    }

    private void writeCsvToUri(Uri uri) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_STUDENT, null, null, null, null, null, null);

        try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
            if (outputStream != null) {
                outputStream.write("RollNo,StudentName,ParentName,ContactNumber,Email,Gender,DepartmentName,AcademicYear,Password\n".getBytes());
                while (cursor.moveToNext()) {
                    String row = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ROLL_NO)) + "," +
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_STUDENT_NAME)) + "," +
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PARENT_NAME)) + "," +
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_CONTACT_NUMBER)) + "," +
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EMAIL)) + "," +
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_GENDER)) + "," +
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DEPARTMENT_NAME)) + "," +
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_ACADEMIC_YEAR)) + "," +
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PASSWORD)) + "\n";
                    outputStream.write(row.getBytes());
                }
                cursor.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error writing CSV", e);
        }
    }

    private void downloadStudentDetailsLegacy() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_STUDENT, null, null, null, null, null, null);

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "students.csv");
        try (FileWriter writer = new FileWriter(file)) {
            writer.append("RollNo,StudentName,ParentName,ContactNumber,Email,Gender,DepartmentName,AcademicYear,Password\n");
            while (cursor.moveToNext()) {
                writer.append(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ROLL_NO)) + ",")
                        .append(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_STUDENT_NAME)) + ",")
                        .append(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PARENT_NAME)) + ",")
                        .append(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_CONTACT_NUMBER)) + ",")
                        .append(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EMAIL)) + ",")
                        .append(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_GENDER)) + ",")
                        .append(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DEPARTMENT_NAME)) + ",")
                        .append(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_ACADEMIC_YEAR)) + ",")
                        .append(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PASSWORD)) + "\n");
            }
            cursor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
