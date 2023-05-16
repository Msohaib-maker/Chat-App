package com.example.smd_a1;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MsgNetworkCheck extends BroadcastReceiver {
    private boolean isConnected = false;
    private ArrayList<Message> messageArrayList;
    private MsgDbSave msgDbSave;
    private String ChatRoomId;
    private String person_id;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private MessageAdapter messageAdapter;
    private RecyclerView MsgRecyclerView;


    @Override
    public void onReceive(Context context, Intent intent) {
        firebaseDatabase =FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("ChatRoom");
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        DoAction();
    }

    private void DoAction()
    {
        if(isConnected)
        {
            // retrieve from firebase

            try
            {
                databaseReference.child(ChatRoomId)
                        .addValueEventListener(new ValueEventListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messageArrayList.clear();
                                ArrayList<Message> messageArrayList1 = msgDbSave.get(person_id);
                                int i = 0;

                                for(Message m : messageArrayList1)
                                {
                                    if(m.getStatus() == 0)
                                    {
                                        messageArrayList1.get(i).setStatus(1);
                                        m.setStatus(1);
                                        StoreMsgInFirebaseDataBase(m);
                                    }
                                    i++;
                                }

                                msgDbSave.Update_Specific_Chat_Status(person_id);

                                for(DataSnapshot snapshot1 : snapshot.getChildren())
                                {
                                    messageArrayList.add(snapshot1.getValue(Message.class));
                                }

                                msgDbSave.DeleteSpecificChat1(person_id);
                                msgDbSave.save(messageArrayList,person_id);



                                messageAdapter.notifyDataSetChanged();
                                MsgRecyclerView.scrollToPosition(messageAdapter.getItemCount()-1);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            }
            catch (Exception e)
            {
                // handle exceptions here
            }

        }
    }


    private void StoreMsgInFirebaseDataBase(Message m)
    {
        String PushKey = databaseReference.push().getKey();

        // send and receive
        assert PushKey != null;
        databaseReference.child(ChatRoomId)
                .child(PushKey).setValue(m);
    }

    public void getParas(ArrayList<Message> messages,MsgDbSave msgDb,String chatRoomId,String P_Id,MessageAdapter adapter,RecyclerView msgRecyclerView)
    {
        this.messageArrayList = messages;
        this.msgDbSave = msgDb;
        this.ChatRoomId = chatRoomId;
        this.person_id = P_Id;
        this.messageAdapter = adapter;
        this.MsgRecyclerView = msgRecyclerView;
    }


    public boolean isConnected() {
        return isConnected;
    }
}
