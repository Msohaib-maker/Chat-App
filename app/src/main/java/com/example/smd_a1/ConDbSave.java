package com.example.smd_a1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class ConDbSave implements Con_Save{

    private Context context;

    public ConDbSave(Context c)
    {
        this.context = c;
    }

    @Override
    public void save(Person p) {
        Conversation_Store conversation_store = new Conversation_Store(context);
        String person_name = p.getName();
        //Bitmap the_image = p.getPhoto1();
        String phone = p.getPhone();
        String status = String.valueOf(p.getStatus_online());

        //byte[] image = DbBitmapUtility.getBytes(the_image);

        ContentValues cv = new ContentValues();

        cv.put(conversation_store.get_Name_Col(),person_name);
        cv.put(conversation_store.get_PHONE_COL(),phone);
        cv.put(conversation_store.get_STATUS_COL(),status);


        SQLiteDatabase db = conversation_store.getWritableDatabase();

        db.insert(conversation_store.get_Table_Name(),null,cv);
    }

    @Override
    public void save(List<Person> personList) {
        for(Person p : personList)
        {
            save(p);
        }
    }

    @Override
    public List<Person> get() {
        List<Person> personList = new ArrayList<>();
        Conversation_Store conversation_store = new Conversation_Store(context);
        SQLiteDatabase db = conversation_store.getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("Select * from " + conversation_store.get_Table_Name(),null);

        if(cursor.moveToFirst())
        {
            do
            {
                int index = cursor.getColumnIndex(conversation_store.get_PHONE_COL());
                String phone = cursor.getString(index);
                index = cursor.getColumnIndex(conversation_store.get_STATUS_COL());
                String status = cursor.getString(index);
                index = cursor.getColumnIndex(conversation_store.get_Name_Col());
                @SuppressLint("Range") String name = cursor.getString(index);

                //Bitmap bitmap = DbBitmapUtility.getImage(image_data);
                if(status != null)
                {
                    personList.add(new Person(name,phone,Integer.parseInt(status)));
                }
                else
                {
                    personList.add(new Person(name,phone,0));
                }



            }
            while (cursor.moveToNext());
        }

        return personList;
    }

    @Override
    public Person get(String name) {
        return null;
    }

    public void Delete_ALL()
    {
        Conversation_Store conversation_store = new Conversation_Store(context);
        SQLiteDatabase db = conversation_store.getWritableDatabase();

        db.execSQL("Delete from " + conversation_store.get_Table_Name());
    }


    public void UpdateConversation(Person p)
    {
        Conversation_Store conversation_store = new Conversation_Store(context);
        SQLiteDatabase db = conversation_store.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(conversation_store.get_STATUS_COL(),String.valueOf(p.getStatus_online()));

        db.update(conversation_store.get_Table_Name(),cv,conversation_store.get_PHONE_COL() + "=?",new String[]{p.getPhone()});
        db.close();
    }

}
