package com.example.smd_a1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements CredentialsDialog.DialogListener{

    FirebaseAuth firebaseAuth;
    RelativeLayout login;

    String myPhone;
    String MyName;
    AppUserSave appUserSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appUserSave = new AppUserSave(this);
        firebaseAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.loginBtn);
        TextView signupPage = findViewById(R.id.signUpPage);
        TextView OfflineLoginPage = findViewById(R.id.offlinePageId);

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            ArrayList<String> creds = appUserSave.Retrieve(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            String the_Email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            myPhone = creds.get(0);
            MyName = creds.get(1);

            if(myPhone.equals("") || MyName.equals(""))
            {
                OpenDialog();
            }

            if(myPhone.length() >= 10 && !MyName.isEmpty() )
            {
                appUserSave.Update(the_Email,myPhone,MyName);
            }
            else
            {
                Toast.makeText(this, "Credential not valid. CLose App and start again", Toast.LENGTH_SHORT).show();
                return;
            }


            startActivity(new Intent(Login.this,Conversation.class)
                    .putExtra("PhoneSend",myPhone)
                    .putExtra("NameSend",MyName));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OpenDialog();
                SignIn();
            }
        });

        signupPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,SIgnUp.class));
            }
        });

        OfflineLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Offline_login.class));
            }
        });
    }

    public void SignIn()
    {
        EditText email = findViewById(R.id.Email_Id);
        EditText password = findViewById(R.id.Password_Id);


        String Email = email.getText().toString();
        String Password = password.getText().toString();

        ArrayList<String> getValues = appUserSave.Retrieve(Email);

        myPhone = getValues.get(0);
        MyName = getValues.get(1);

        if (myPhone.equals("") || MyName.equals(""))
        {
            OpenDialog();


            /*if(myPhone.equals("") || myPhone.length() < 10 || MyName.equals(""))
            {
                return;
            }*/
        }

        firebaseAuth.signInWithEmailAndPassword(Email,Password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        if(!appUserSave.Update(Email,myPhone,MyName))
                        {
                            appUserSave.Insert(Email,myPhone,MyName);
                        }

                        startActivity(new Intent(Login.this,Conversation.class)
                                .putExtra("PhoneSend",myPhone)
                                .putExtra("NameSend",MyName));
                    }
                });
    }

    public void OpenDialog()
    {
        CredentialsDialog credentialsDialog = new CredentialsDialog();
        credentialsDialog.show(getSupportFragmentManager(),"Cred Dialog");
    }

    @Override
    public void getPhoneAndName(String phone, String name) {
        myPhone = phone;
        MyName = name;

        if(myPhone.equals("") || myPhone.length() < 10 || MyName.equals(""))
        {
            Toast.makeText(this, "Phone length should be greater than 10 and Name should not be empty", Toast.LENGTH_SHORT).show();
            OpenDialog();
        }
    }
}