package com.example.smd_a1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MySettings extends AppCompatActivity {

    ConDbSave conDbSave;
    MsgDbSave msgDbSave;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);
        TextView phonePick = findViewById(R.id.phonePickId);
        TextView NamePick = findViewById(R.id.NamePickId);
        TextView EmailPick = findViewById(R.id.EmailPickId);

        phonePick.setText(getIntent().getStringExtra("MyPhoneNumber"));
        NamePick.setText(getIntent().getStringExtra("NameAlso"));
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            String email = EmailPick.getText().toString();
            EmailPick.setText(email);
        }
        else
        {
            EmailPick.setText("Not Paired");
        }



        conDbSave = new ConDbSave(this);
        msgDbSave = new MsgDbSave(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        Button deleteAllBtn = findViewById(R.id.deleteAllBtn);
        Button BackBtn = findViewById(R.id.backBtn);

        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conDbSave.Delete_ALL();
                msgDbSave.Delete_ALL();
                try
                {
                    String phoneNum = getIntent().getStringExtra("MyPhoneNumber");
                    databaseReference.child(phoneNum).removeValue();
                }
                catch (Exception e)
                {
                    // handling will be done later
                }
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = getIntent().getStringExtra("MyPhoneNumber");
                String Name = getIntent().getStringExtra("NameAlso");
                startActivity(new Intent(MySettings.this,Conversation.class)
                        .putExtra("PhoneSend",phoneNum)
                        .putExtra("NameSend",Name));
            }
        });

    }
}