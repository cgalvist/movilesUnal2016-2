package cgt.SQLite.DB;

import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactoDBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "employees.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_EMPLOYEES = "employees";
    public static final String COLUMN_ID = "empId";
    public static final String COLUMN_NAME = "firstname";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PAS = "pas";
    public static final String COLUMN_DEPT= "dept";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_EMPLOYEES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_URL + " TEXT, " +
                    COLUMN_PHONE + " NUMERIC, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PAS + " TEXT, " +
                    COLUMN_DEPT + " NUMERIC " +
                    ")";


    public ContactoDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EMPLOYEES);
        db.execSQL(TABLE_CREATE);
    }
}
