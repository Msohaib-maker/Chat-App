package com.example.smd_a1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MsgDbSave implements MsgDAO{

    private Context context;

    public MsgDbSave(Context context1)
    {
        this.context = context1;
    }

    @Override
    public void save(Message m, String Person_Id) {
        Conversation_Store conversation_store = new Conversation_Store(context);
        SQLiteDatabase db = conversation_store.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(conversation_store.get_NAME1_COL(),m.getName());
        contentValues.put(conversation_store.get_CONTENT_COL(),m.getContent());
        contentValues.put(conversation_store.get_TIME_COL(),m.getTime());
        contentValues.put(conversation_store.get_PERSON_ID_COL(),Person_Id);
        contentValues.put(conversation_store.get_TYPE_COL(),m.getIs_sender());
        contentValues.put(conversation_store.get_Sender_PHONE_COL(),m.getSenderNum());
        contentValues.put(conversation_store.get_Receiver_Phone_COl(),m.getReceiverNum());
        contentValues.put(conversation_store.get_THE_STATUS(),m.getStatus());

        db.insert(conversation_store.get_TABLE1_NAME(),null,contentValues);
    }

    @Override
    public void save(List<Message> messageList, String Person_Id) {

        for(Message m : messageList)
        {
            save(m,Person_Id);
        }

    }

    @Override
    public ArrayList<Message> get(String Person_Id) {
        Conversation_Store conversation_store = new Conversation_Store(context);
        SQLiteDatabase db = conversation_store.getReadableDatabase();
        ArrayList<Message> messageList = new ArrayList<>();

        @SuppressLint("Recycle") Cursor cur = db.rawQuery("Select * from " + conversation_store.get_TABLE1_NAME() + " where "
                + conversation_store.get_PERSON_ID_COL() + " = " + Person_Id,null);

        if(cur.moveToFirst())
        {
            do{
                int index = cur.getColumnIndex(conversation_store.get_NAME1_COL());
                String Name = cur.getString(index);
                index = cur.getColumnIndex(conversation_store.get_CONTENT_COL());
                String Content = cur.getString(index);
                index = cur.getColumnIndex(conversation_store.get_TIME_COL());
                String time = cur.getString(index);
                index = cur.getColumnIndex(conversation_store.get_TYPE_COL());
                String type = cur.getString(index);
                index = cur.getColumnIndex(conversation_store.get_Sender_PHONE_COL());
                String Sender_Phone = cur.getString(index);
                index = cur.getColumnIndex(conversation_store.get_Receiver_Phone_COl());
                String Receiver_Phone = cur.getString(index);
                index = cur.getColumnIndex(conversation_store.get_THE_STATUS());
                String the_Status = cur.getString(index);

                int Type = 2;
                int online_check = 0;
                try
                {
                    Type = Integer.parseInt(type);
                    online_check = Integer.parseInt(the_Status);
                }
                catch (Exception e)
                {

                }

                Message m = new Message(Name,Content,time,"",Sender_Phone,Receiver_Phone,online_check);
                messageList.add(m);


            }while(cur.moveToNext());
        }

        return messageList;
    }

    public void Delete_ALL()
    {
        Conversation_Store conversation_store = new Conversation_Store(context);
        SQLiteDatabase db = conversation_store.getWritableDatabase();

        db.execSQL("Delete from " + conversation_store.get_TABLE1_NAME());
    }

    public void DeleteSpecificChat(String ChatPhone)
    {
        Conversation_Store conversation_store = new Conversation_Store(context);
        SQLiteDatabase db = conversation_store.getWritableDatabase();

        db.delete(conversation_store.get_TABLE1_NAME(),conversation_store.get_Receiver_Phone_COl() + "=?",new String[]{ChatPhone});

    }

    public void DeleteSpecificChat1(String id)
    {
        Conversation_Store conversation_store = new Conversation_Store(context);
        SQLiteDatabase db = conversation_store.getWritableDatabase();

        db.delete(conversation_store.get_TABLE1_NAME(),conversation_store.get_PERSON_ID_COL() + "=?",new String[]{id});

    }

    public void Update_Specific_Chat_Status(String person_id)
    {
        Conversation_Store conversation_store = new Conversation_Store(context);
        SQLiteDatabase db = conversation_store.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(conversation_store.get_STATUS_COL(),"1");

        String whereClause = conversation_store.get_STATUS_COL() + "=?" + " AND " + conversation_store.get_PERSON_ID_COL() + "=?";

        String[] values = {"0",person_id};

        db.update(conversation_store.get_TABLE1_NAME(),contentValues,whereClause,values);

    }




}
