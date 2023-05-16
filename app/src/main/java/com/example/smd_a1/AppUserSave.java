package com.example.smd_a1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class AppUserSave extends SQLiteOpenHelper {

    private static final int App_DB_VER = 2;
    private static final String APP_DB_NAME = "AppSave.db";

    private static final String EMAIL_COl = "Email";
    private static final String PHONE_COL = "Phone";
    private static final String USER_NAME_COL = "UserName";
    private static final String APP_TABLE_NAME = "PersonalInfo";
    private static final String ID_COL =  "MyId";



    public AppUserSave(@Nullable Context context) {
        super(context, APP_DB_NAME, null, App_DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "Create table " + APP_TABLE_NAME + " ( "
                    + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + EMAIL_COl + " TEXT, "
                    + PHONE_COL + " TEXT, "
                    + USER_NAME_COL + " TEXT)";

        sqLiteDatabase.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table if exists " + APP_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void Insert(String email,String phone,String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(EMAIL_COl,email);
        contentValues.put(PHONE_COL,phone);
        contentValues.put(USER_NAME_COL,name);

        db.insert(APP_TABLE_NAME,null,contentValues);
    }

    public boolean Update(String email,String phone,String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PHONE_COL,phone);
        contentValues.put(USER_NAME_COL,name);

        int rows = db.update(APP_TABLE_NAME,contentValues,EMAIL_COl + "=?",new String[]{email});

        return rows > 0;
    }

    public ArrayList<String> Retrieve(String email)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cur = db.rawQuery("SELECT * FROM " + APP_TABLE_NAME + " WHERE " + EMAIL_COl + " = '" + email + "'", null);

        ArrayList<String> StrList = new ArrayList<>();

        if (cur.moveToFirst()) {
            int index = cur.getColumnIndex(PHONE_COL);
            String phone = cur.getString(index);
            index = cur.getColumnIndex(USER_NAME_COL);
            String name = cur.getString(index);

            if(phone != null && name!=null)
            {
                StrList.add(phone);
                StrList.add(name);
            }
            else
            {
                StrList.add("");
                StrList.add("");
            }
        }
        else
        {
            StrList.add("");
            StrList.add("");
        }


        return StrList;

    }

}



