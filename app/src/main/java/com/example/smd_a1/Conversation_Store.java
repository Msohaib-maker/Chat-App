package com.example.smd_a1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Conversation_Store extends SQLiteOpenHelper {


    private static final int DB_VERSION = 5;
    private static final String DB_Name = "Chat.db";

    // Conversation save table
    private static final String TABLE_NAME = "Conversation";
    private static final String ID_COL = "ID";
    private static final String NAME_COL = "NAME";
    private static final String IMAGE_COL = "IMAGE";
    private static final String PHONE_COL = "PhoneNum";
    private static final String SAVE_STATUS = "Status";


    // Message save table
    private static final String TABLE_NAME1 = "Message";
    private static final String ID1_COL = "ID";
    private static final String CONTENT_COL = "CONTENT";
    private static final String TIME_COL = "TIME";
    private static final String PERSON_ID_COL = "PERSON_ID";
    private static final String TYPE_COL = "TYPE";
    private static final String NAME1_COL = "NAME";
    private static final String SENDER_PHONE_COL = "SENDER";
    private static final String RECEIVER_PHONE_COL = "RECEIVER";
    private static final String THE_STATUS = "STATUS";

    public Conversation_Store(@Nullable Context context) {
        super(context, DB_Name, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "Create TABLE " + TABLE_NAME + " ( "
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT, " +
                PHONE_COL + " TEXT, " +
                SAVE_STATUS + " TEXT, " +
                IMAGE_COL + " BLOB)";

        String query1 = "Create TABLE " + TABLE_NAME1 + " ( "
                + ID1_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME1_COL + " TEXT, "
                + CONTENT_COL + " TEXT, "
                + TIME_COL + " TEXT, "
                + PERSON_ID_COL + " TEXT, "
                + TYPE_COL + " TEXT, "
                + SENDER_PHONE_COL + " TEXT, "
                + RECEIVER_PHONE_COL + " TEXT, "
                + THE_STATUS + " TEXT)";


        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        if(i < 1)
        {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
            onCreate(sqLiteDatabase);
        }
        if(i<2)
        {
            String TABLE2 = "Kop";
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE2);
            String query = "Create TABLE " + TABLE2 + " ( UI TEXT )";
            sqLiteDatabase.execSQL(query);
        }
        if(i<3)
        {
            String addPhoneColumn = "Alter table " + TABLE_NAME + " add " + PHONE_COL + " TEXT ";
            sqLiteDatabase.execSQL(addPhoneColumn);

        }
        if(i<4)
        {
            String addStatusColumn = "Alter table " + TABLE_NAME + " add " + SAVE_STATUS + " TEXT ";
            sqLiteDatabase.execSQL(addStatusColumn);

        }
        if(i<5)
        {
            String addSenderPhoneCol = "Alter table " + TABLE_NAME1 + " add " + SENDER_PHONE_COL + " TEXT ";
            String addReceiverPHoneCol = "Alter table " + TABLE_NAME1 + " add " + RECEIVER_PHONE_COL + " TEXT ";
            String addOnlineStatusCOl = "Alter table " + TABLE_NAME1 + " add " + THE_STATUS + " TEXT ";
            sqLiteDatabase.execSQL(addSenderPhoneCol);
            sqLiteDatabase.execSQL(addReceiverPHoneCol);
            sqLiteDatabase.execSQL(addOnlineStatusCOl);
        }



    }

    public String get_Name_Col()
    {
        return NAME_COL;
    }

    public String get_Image_Col()
    {
        return IMAGE_COL;
    }

    public String get_Table_Name()
    {
        return TABLE_NAME;
    }

    public String get_TABLE1_NAME()
    {
        return TABLE_NAME1;
    }

    public String get_NAME1_COL()
    {
        return NAME1_COL;
    }

    public String get_CONTENT_COL()
    {
        return CONTENT_COL;
    }

    public String get_TIME_COL()
    {
        return TIME_COL;
    }

    public String get_PERSON_ID_COL()
    {
        return PERSON_ID_COL;
    }

    public String get_TYPE_COL()
    {
        return TYPE_COL;
    }

    public String get_PHONE_COL()
    {
        return PHONE_COL;
    }

    public String get_STATUS_COL()
    {
        return SAVE_STATUS;
    }

    public String get_Sender_PHONE_COL()
    {
        return SENDER_PHONE_COL;
    }

    public String get_Receiver_Phone_COl()
    {
        return RECEIVER_PHONE_COL;
    }

    public String get_THE_STATUS()
    {
        return THE_STATUS;
    }


}
