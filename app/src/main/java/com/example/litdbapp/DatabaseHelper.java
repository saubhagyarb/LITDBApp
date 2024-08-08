package com.example.litdbapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LITDB.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ADMIN = "Admin";
    public static final String TABLE_STUDENT = "Student";

    public static final String COL_ROLL_NO = "RollNo";
    public static final String COL_STUDENT_NAME = "StudentName";
    public static final String COL_PARENT_NAME = "ParentName";
    public static final String COL_CONTACT_NUMBER = "ContactNumber";
    public static final String COL_EMAIL = "Email";
    public static final String COL_GENDER = "Gender";
    public static final String COL_DEPARTMENT_NAME = "DepartmentName";
    public static final String COL_ACADEMIC_YEAR = "AcademicYear";
    public static final String COL_PASSWORD = "Password";

    public static final String ADMIN_USERNAME = "ADMIN";
    public static final String ADMIN_PASSWORD = "lit@india";

    private static final String CREATE_TABLE_ADMIN = "CREATE TABLE " + TABLE_ADMIN + " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)";
    private static final String CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_STUDENT + " (RollNo INTEGER PRIMARY KEY, StudentName TEXT, ParentName TEXT, ContactNumber TEXT, Email TEXT, Gender TEXT, DepartmentName TEXT, AcademicYear TEXT, Password TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ADMIN);
        db.execSQL(CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);
    }
}
