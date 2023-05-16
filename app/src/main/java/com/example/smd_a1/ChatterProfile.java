package com.example.smd_a1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatterProfile extends AppCompatActivity {

    MsgDbSave msgDbSave;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatter_profile);
        msgDbSave = new MsgDbSave(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("ChatRoom");

        // Receiving all the intent data
        String personId = getIntent().getStringExtra("ChatterId");
        String MyPhone = getIntent().getStringExtra("MyPhoneNo");
        String MyName = getIntent().getStringExtra("MyUserName");
        String ReceiverPhone = getIntent().getStringExtra("ChatterPhoneNo");
        String ReceiverName = getIntent().getStringExtra("ChatterName");
        String ChatterRoomId = getIntent().getStringExtra("ChatRoomCommId");


        // Setting Chatter Name and Phone

        TextView ChatterName = findViewById(R.id.ChatterNameId);
        ChatterName.setText(ReceiverName);
        TextView ChatterPhone = findViewById(R.id.textView6);
        ChatterPhone.setText(ReceiverPhone);


        // Chat delete button and back button

        Button chatDeleteBtn = findViewById(R.id.DeleteChatBtnId);
        Button BackBtn = findViewById(R.id.BackBtnId);

        chatDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgDbSave.DeleteSpecificChat(ReceiverPhone);
                databaseReference.child(ChatterRoomId).removeValue();
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatterProfile.this,MainActivity.class)
                        .putExtra("PersonId",personId)
                        .putExtra("PhoneNumber",MyPhone)
                        .putExtra("MyName",MyName)
                        .putExtra("ReceiverNum",ReceiverPhone)
                        .putExtra("ReceiverName",ReceiverName));
            }
        });


    }
}