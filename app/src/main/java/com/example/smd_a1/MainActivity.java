package com.example.smd_a1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Message> messageArrayList = new ArrayList<>();
    private ImageView back;
    private CardView send;
    //private CardView receive;
    private EditText Message_Box;
    private MsgDbSave msgDbSave;
    private ConstraintLayout constraintLayout;
    private SwipeListener swipeListener;

    private String phoneNo;
    private String myName;
    private String RecvPhone;
    private String Recv_Name;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private String ChatRoomId;

    ConnectivityManager cm;
    NetworkInfo nInfo;
    boolean connected;


    // Firebase DAO
    saveFirebaseDAO firebaseDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDAO = new saveFirebaseDAO();


        recyclerView = findViewById(R.id.message_recycleview_id);
        send = findViewById(R.id.send_Id);
        constraintLayout = findViewById(R.id.cc_Id);
        back = findViewById(R.id.back_id);
        TextView ChatterName = findViewById(R.id.ReceiverNameId);




        // --------------------------
        // Firebase Initializations
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("ChatRoom");

        // --------------------------

        // getting the Connectivity service
        cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        // checking network status
        nInfo = cm.getActiveNetworkInfo();
        connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();



        //receive = findViewById(R.id.receive_msg_id);
        Message_Box = findViewById(R.id.message_box_Id);

        // Initializing the Msg Saver Database
        msgDbSave = new MsgDbSave(this);

        MyMessageModel myMessageModel = new ViewModelProvider(this).get(MyMessageModel.class);

        //messageArrayList = myMessageModel.getMessages();

        String person_id = getIntent().getStringExtra("PersonId");
        phoneNo = getIntent().getStringExtra("PhoneNumber");
        myName = getIntent().getStringExtra("MyName");
        RecvPhone = getIntent().getStringExtra("ReceiverNum");
        Recv_Name = getIntent().getStringExtra("ReceiverName");

        // initialize ChatroomId
        // ---------------------------------

        if(Math.abs(RecvPhone.length() - phoneNo.length()) > 0)
        {
            if(RecvPhone.length() > phoneNo.length())
            {
                ChatRoomId = RecvPhone + phoneNo;
            }
            else
            {
                ChatRoomId = phoneNo + RecvPhone;
            }
        }
        else
        {
            int in=0;
            for(in=0;in<RecvPhone.length();in++)
            {
                if(Integer.parseInt(String.valueOf(phoneNo.charAt(in))) > Integer.parseInt(String.valueOf(RecvPhone.charAt(in))))
                {
                    ChatRoomId = phoneNo + RecvPhone;
                    break;
                }
                else if(Integer.parseInt(String.valueOf(phoneNo.charAt(in))) < Integer.parseInt(String.valueOf(RecvPhone.charAt(in))))
                {
                    ChatRoomId = RecvPhone + phoneNo;
                    break;
                }

            }
        }

        //-------------------------------------

        ChatterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ChatterProfile.class)
                        .putExtra("ChatterId",person_id)
                        .putExtra("MyPhoneNo",phoneNo)
                        .putExtra("MyUserName",myName)
                        .putExtra("ChatterPhoneNo",RecvPhone)
                        .putExtra("ChatterName",Recv_Name)
                        .putExtra("ChatRoomCommId",ChatRoomId));
            }
        });


        ChatterName.setText(Recv_Name);

        Log.d("Cog", phoneNo  + " " + myName + " " + RecvPhone + " " + Recv_Name);

        messageArrayList = msgDbSave.get(person_id);


        swipeListener = new SwipeListener(constraintLayout);



        // send button to send the message
        send.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                String time1 = getTime();

                String my_content = Message_Box.getText().toString();
                Message_Box.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Message_Box.getWindowToken(), 0);
                Message New_Msg;

                nInfo = cm.getActiveNetworkInfo();
                connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                if(connected)
                {
                    New_Msg = new Message(myName,my_content, time1,"",phoneNo,RecvPhone,1);
                    StoreMsgInFirebaseDataBase(New_Msg);
                    msgDbSave.save(New_Msg,person_id);
                }
                else
                {
                    New_Msg = new Message(myName,my_content, time1,"",phoneNo,RecvPhone,0);
                    msgDbSave.save(New_Msg,person_id);
                }


                messageArrayList.add(New_Msg);
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
            }
        });


        // back button to switch to previous activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Conversation.class)
                        .putExtra("NameSend",myName)
                        .putExtra("PhoneSend",phoneNo));

            }
        });




        if(!connected)
        {
            // retrieve locally
            messageArrayList = msgDbSave.get(person_id);
        }
        messageAdapter = new MessageAdapter(messageArrayList,this,phoneNo,RecvPhone);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);
        recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);

        // Initialize Network Check Broadcast receiver
        MsgNetworkCheck msgNetworkCheck = new MsgNetworkCheck();
        msgNetworkCheck.getParas(messageArrayList,msgDbSave,ChatRoomId,person_id,messageAdapter,recyclerView);
        registerReceiver(msgNetworkCheck,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));







    }

    private void StoreMsgInFirebaseDataBase(Message m)
    {
        String PushKey = databaseReference.push().getKey();

        // send and receive
        assert PushKey != null;
        databaseReference.child(ChatRoomId)
                .child(PushKey).setValue(m);



    }

    private int convertStrToInt(String RecvPhone)
    {
        int num = 0;

        for(int i=0;i<RecvPhone.length();i++)
        {
            if(RecvPhone.charAt(i) >= '0' && RecvPhone.charAt(i) <= '9')
            {
                num = (num*10) + Integer.parseInt(String.valueOf(RecvPhone.charAt(i)));
            }
        }

        return num;
    }

    public String getTime()
    {
        Date c1 = Calendar.getInstance().getTime();

        SimpleDateFormat df1 = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        return df1.format(c1);
    }

    private class SwipeListener implements View.OnTouchListener
    {

        GestureDetector gestureDetector;

        SwipeListener(View v)
        {
            int threshold = 100;
            int velocity_threshold = 100;

            GestureDetector.SimpleOnGestureListener simpleOnGestureListener =
                    new GestureDetector.SimpleOnGestureListener()
                    {
                        @Override
                        public boolean onDown(MotionEvent e) {
                            return super.onDown(e);
                        }

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                            float Diffx = e1.getX() - e1.getX();

                            try
                            {

                                if(Math.abs(Diffx)>threshold && Math.abs(velocityX)>velocity_threshold)
                                {
                                    if(Diffx>0)
                                    {
                                        // back functionality
                                        startActivity(new Intent(MainActivity.this,Conversation.class));
                                    }
                                }
                            }
                            catch (Exception e)
                            {

                            }

                            return false;
                        }
                    };

            gestureDetector = new GestureDetector(simpleOnGestureListener);
            v.setOnTouchListener(this);
        }







        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
    }
}