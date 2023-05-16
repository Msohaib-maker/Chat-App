package com.example.smd_a1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Conversation extends AppCompatActivity implements AdapterClickListener {

    private RecyclerView conversation_recycleView;
    private Conversation_Adapter conversation_adapter;
    private List<Person> personList = new ArrayList<>();
    private ConDbSave conDbSave;
    private CardView Add_contact;
    private ImageView settingsAct;
    private static final int REQUEST_READ_CONTACTS_PERMISSION = 0;

    private String ownPhoneNum;
    private String the_phone_num;
    private String the_Name;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;

    AppUserSave appUserSave;

    // internet status
    ConnectivityManager cm;
    NetworkInfo nInfo;

    // request code for contact app
    private static final int REQUEST_CONTACT = 1;

    // Firebase save DAO
    saveFirebaseDAO firebaseDAO;

    private ImageView wifi_status;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        // Firebase DAO initialize
        firebaseDAO = new saveFirebaseDAO();


        wifi_status = findViewById(R.id.wifi_stat_Id);
        TextView the_number = findViewById(R.id.phone_of_per_Id);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        databaseReference1 = firebaseDatabase.getReference("AppUsers");
        appUserSave = new AppUserSave(this);



        settingsAct = findViewById(R.id.Settings);

        registerForContextMenu(settingsAct);

        conversation_recycleView = findViewById(R.id.conversation_recycleView_Id);
        Add_contact = findViewById(R.id.cardView);
        conDbSave = new ConDbSave(this);


        // retrieve phone number and name from local database
        the_phone_num = getIntent().getStringExtra("PhoneSend");
        the_Name = getIntent().getStringExtra("NameSend");

        Log.d("Personals", the_phone_num +  " " + the_Name);




        // offline retrieval
        try
        {
            personList = conDbSave.get();
        }
        catch (Exception e)
        {
            personList = new ArrayList<>();
        }

        NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();
        networkChangeReceiver.getParas(personList,conDbSave,the_phone_num,wifi_status);
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        // Internet check
        cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        conversation_adapter = new Conversation_Adapter(personList,this,this);
        conversation_recycleView.setLayoutManager(new LinearLayoutManager(this));
        conversation_recycleView.setAdapter(conversation_adapter);

        // Contact Intent
        Intent iContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        // add new contact
        Add_contact.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                startActivityForResult(iContact,REQUEST_CONTACT);
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.display_menu,menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.the_settings:
                startActivity(new Intent(Conversation.this,MySettings.class)
                        .putExtra("MyPhoneNumber",the_phone_num)
                        .putExtra("NameAlso",the_Name));
                return true;
            case R.id.the_themes:
                Toast.makeText(this, "Themes", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CONTACT && data != null)
        {
            Uri contactUri = data.getData();

            // Specify which fields you want your
            // query to return values for
            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};

            // Perform your query - the contactUri
            // is like a "where" clause here
            Cursor cursor = this.getContentResolver()
                    .query(contactUri, null, null, null, null);
            try
            {
                // Double-check that you
                // actually got results
                if (cursor.getCount() == 0) return;

                // Pull out the first column
                // of the first row of data
                // that is your contact's name
                cursor.moveToFirst();

                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                String trimmedPhone = TrimmedPhoneNum(phone);

                // duplicate check
                for(Person per : personList)
                {
                    if(per.getPhone().equals(trimmedPhone))
                    {
                        Toast.makeText(this, "Contact is already added in your app", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                conversation_adapter.notifyDataSetChanged();
                if(the_phone_num != null)
                {
                    nInfo = cm.getActiveNetworkInfo();
                    boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                    if(connected)
                    {
                        // online
                        String key = databaseReference.push().getKey();
                        assert key != null;
                        databaseReference.child(the_phone_num)
                                .child(key).child("Phone").setValue(trimmedPhone);
                        Person p = new Person(name,trimmedPhone,1);
                        personList.add(p);
                        conDbSave.save(p);



                    }
                    else
                    {
                        // offline
                        Person p = new Person(name,trimmedPhone,0);
                        personList.add(p);
                        conDbSave.save(p);
                    }



                }
            }
            finally
            {
                cursor.close();
            }
        }
    }

    private String TrimmedPhoneNum(String phone)
    {

        String trimPhone = phone.replaceAll("\\D", "");
        return trimPhone;
    }

    private Bitmap getImage()
    {
        Drawable drawable = getResources().getDrawable(R.drawable.bubbly);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public void Click(int pos) {
        startActivity(new Intent(Conversation.this,MainActivity.class)
                .putExtra("PersonId",String.valueOf(pos))
                .putExtra("PhoneNumber",the_phone_num)
                .putExtra("MyName",the_Name)
                .putExtra("ReceiverName",personList.get(pos).getName())
                .putExtra("ReceiverNum",personList.get(pos).getPhone()));
    }
}