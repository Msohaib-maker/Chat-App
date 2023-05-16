package com.example.smd_a1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Offline_login extends AppCompatActivity {

    RelativeLayout offline_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_login);
        offline_btn = findViewById(R.id.SignInBtn);
        TextView online_page = findViewById(R.id.online_signIn);

        offline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoggedIn();
            }
        });

        online_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Offline_login.this,Login.class));
            }
        });
    }

    private void LoggedIn()
    {
        EditText phone = findViewById(R.id.EditPhoneId);
        EditText name = findViewById(R.id.EditNameId);

        String Name = name.getText().toString();
        String Phone = phone.getText().toString();

        if(Phone.length() < 10)
        {
            phone.requestFocus();
            phone.setError("please enter valid phone number");
            return;
        }

        if(Name.isEmpty())
        {
            name.requestFocus();
            name.setError("Please enter something in this field");
            return;
        }

        startActivity(new Intent(Offline_login.this,Conversation.class)
                .putExtra("PhoneSend",Phone)
                .putExtra("NameSend",Name));

    }
}