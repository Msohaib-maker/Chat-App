package com.example.smd_a1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SIgnUp extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView loginPageBtn;
    RelativeLayout registerBtn;
    AppUserSave appUserSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        appUserSave = new AppUserSave(this);
        loginPageBtn = findViewById(R.id.login_id);
        registerBtn = findViewById(R.id.button);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("AppUsers");

        TextView offlineSignIn = findViewById(R.id.offlineId);
        offlineSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SIgnUp.this,Offline_login.class));
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        SwitchPage();
        PermissionRequest();
    }

    private void PermissionRequest()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
        }
    }

    public void SwitchPage()
    {
        loginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SIgnUp.this,Login.class));
            }
        });
    }

    public void register()
    {
        EditText email,password,phone,UserName;
        email = findViewById(R.id.Emailid);
        password = findViewById(R.id.PasswordId);
        phone = findViewById(R.id.PhoneId);
        UserName = findViewById(R.id.UserNameId);

        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Phone = phone.getText().toString();
        String MyUserName = UserName.getText().toString();


        if(Email.isEmpty())
        {
            email.requestFocus();
            return;
        }
        if (Password.isEmpty())
        {
            password.requestFocus();
            return;
        }


        if(Phone.length() < 10)
        {
            phone.requestFocus();
            return;
        }


        if(MyUserName.isEmpty())
        {
            UserName.requestFocus();
            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SIgnUp.this, "Success", Toast.LENGTH_SHORT).show();
                            String key = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                            appUserSave.Insert(Email,Phone,MyUserName);
                            databaseReference.child(key)
                                    .child("Phone").setValue(Phone);
                            databaseReference.child(key)
                                    .child("name").setValue(MyUserName);

                            startActivity(new Intent(SIgnUp.this,Conversation.class)
                                    .putExtra("PhoneSend",Phone)
                                    .putExtra("NameSend",MyUserName));


                        }
                    }
                });
    }
}